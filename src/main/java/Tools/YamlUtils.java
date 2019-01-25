package Tools;

import Model.AllModel;

import Model.ConfigModel;
import net.sf.json.JSONArray;
import org.junit.Assert;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class YamlUtils {

   /* public static void read() throws FileNotFoundException {
        File dumpFile = new File(System.getProperty("user.dir") + "/src/main/java/Case/post_temp3.yaml");
        System.out.println(dumpFile.exists());
        TestModel test = Yaml.loadType(dumpFile, TestModel.class);
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println(test.getName());
    }*/

    public void loadall() {
        Yaml yaml = new Yaml(new Constructor(ConfigModel.class));
        File f = new File(System.getProperty("user.dir") + "/src/main/java/Case/post_temp3.yaml");

        //Iterable<Object> result = yaml.loadAll(new FileInputStream(f));
        /*Map result = (Map) yaml.load(new FileInputStream(f));
        System.out.println(result.get("config"));
        System.out.println(result.get("testSuit"));*/

        ConfigModel ret = (ConfigModel) yaml.load(this.getClass().getClassLoader().getResourceAsStream("post_temp4.yaml"));
        Assert.assertNotNull(ret);
        Assert.assertEquals("MI", ret.getName());

   /*     for (Object obj : result) {
            System.out.println();
            System.out.println(obj);
        }*/
    }

    public void loadall2() {

        Yaml yaml = new Yaml();
        Map<String, Object> testData = (Map<String, Object>) yaml.load(this.getClass().getClassLoader().getResourceAsStream("post_temp3.yaml"));

        System.out.println(testData);
        List<String> config = Arrays.asList(testData.get("config").toString().split(","));
        System.out.println(config);
        JSONArray testCaseList = JSONArray.fromObject(testData.get("testSuit"));
        System.out.println(testCaseList);
        //Arrays.asList(testData.get("testSuit").toString().split(","));

        //System.out.println(testData.get("testSuit"));


    }

    public static void main(String[] args) throws FileNotFoundException {
        new YamlUtils().loadall2();
    }
}
