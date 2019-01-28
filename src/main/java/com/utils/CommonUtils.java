package com.utils;

import java.io.File;
import java.util.*;

public class CommonUtils {
    /**
     * 合并多个map
     * @param maps
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     */
    public static <K, V> Map mergeMaps(Map<K, V>... maps) {
        Class clazz = maps[0].getClass(); // 获取传入map的类型
        Map<K, V> map = null;
        try {
            map = (Map) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0, len = maps.length; i < len; i++) {
            map.putAll(maps[i]);
        }
        return map;
    }

    /**
     * 遍历用例目录，获取用例文件路径
     * @param casefolder
     * @return
     */
    public static List<String> getCaseFolder(String casefolder) {
        List<String> caselist = new ArrayList<String>();
        File file = new File(casefolder);
        File[] files;
        if (file.exists()) {
            if (file.isDirectory()) {
                files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    caselist.add(String.valueOf(files[i]));
                }
            } else {
                caselist.add(casefolder);
            }
        } else {
            System.out.println(String.format("不存在文件夹:", casefolder));
        }
        return caselist;
    }

    public static Collection prepareData() {
        String CASEPATH = System.getProperty("FILEPATH");
        List<String> caseFolder = getCaseFolder(CASEPATH);
        Object[] objects = (Object[]) caseFolder.toArray();

        return Arrays.asList(objects);// 将数组转换成集合返回
    }


}
