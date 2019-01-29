package com.utils;

import com.alibaba.fastjson.JSON;
import com.model.TestDataModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static com.utils.YamlUtil.loadData;

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
        List<String> casePathlist = new ArrayList<String>();
        File file = new File(casefolder);
        File[] files;
        if (file.exists()) {
            if (file.isDirectory()) {
                files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    casePathlist.add(String.valueOf(files[i]));
                }
            } else {
                casePathlist.add(casefolder);
            }
        } else {
            System.out.println(String.format("不存在文件夹:", casefolder));
        }
        return casePathlist;
    }

    public static List<TestDataModel> getTestDataModel(String casefolder) {
        List<TestDataModel> testDatalist = new ArrayList<>();

        List<String> casePathList = getCaseFolder(casefolder);
        for(String path:casePathList){
            try {
                testDatalist.add(loadData(path));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return testDatalist;
    }


    public static Collection prepareData() {
        String CASEPATH = System.getProperty("FILEPATH");
        List<String> caseFolder = getCaseFolder(CASEPATH);
        Object[] objects = (Object[]) caseFolder.toArray();

        return Arrays.asList(objects);// 将数组转换成集合返回
    }

    /**
     * 把一个字符串转换成bean对象
     * @param str
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

}
