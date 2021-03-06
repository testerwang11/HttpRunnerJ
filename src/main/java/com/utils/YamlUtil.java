package com.utils;

import com.model.TestDataModel;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class YamlUtil {

    public static TestDataModel loadData() throws Exception {
        //HashMap<String, Object> caseDatas = new HashMap<>();
        Yaml yaml = new Yaml();
        //TestDataModel me = yaml.loadAs(new FileInputStream(new File(System.getProperty("user.dir")+"/src/main/resources/post_temp3.yaml")), TestDataModel.class);
        TestDataModel me = yaml.loadAs(new FileInputStream(System.getProperty("FILEPATH")), TestDataModel.class);

        //config 提取
        /*ConfigModel config = JSON.parseObject(JSON.toJSONString(me.getConfig()), ConfigModel.class);
        System.out.printf(JSON.toJSONString(me.getConfig()));
        System.out.println(config.getName());
        System.out.println(config.getVariables());
        System.out.println(config.getRequest().getHost());
        //me.getConfig()
        //testcase提取
        System.out.println(me.getTestSuit().get(0));*/

        return me;
    }

    public static TestDataModel loadData(String casePath) throws FileNotFoundException {
        return  new Yaml().loadAs(new FileInputStream(casePath), TestDataModel.class);
    }


    private void printMap(Map map, int count){
        Set set = map.keySet();
        for(Object key: set){

            Object value = map.get(key);

            for(int i=0; i<count; i++){
                System.out.print("    ");
            }

            if(value instanceof Map) {

                System.out.println(key+":");
                printMap((Map)value, count+1);//嵌套
            }else if(value instanceof List){

                System.out.println(key+":");
                for(Object obj: (List)value){
                    for(int i=0; i<count; i++){
                        System.out.print("    ");
                    }
                    System.out.println("    - "+obj.toString());
                }
            }else{

                System.out.println(key + ": " + value);
            }
        }
    }



    public static void main(String[] args) throws Exception {
        //new YamlUtil().loadData();
        new YamlUtil().loadData();

    }
}
