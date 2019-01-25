package Http;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;


public class Run2 {

    private static String FILEPATH;
    private static String REPORTPATH;
    private  static String  LEVEL;
    private static boolean NEEDHELP = false;
    private static boolean DEBUG = true;



    public static void main(String[] args) {
        if (DEBUG){
            debugRun();
        }else {
           executeParameter(args);
        }

    }


    /**
     * 源码运行
     * @throws Exception
     */
    private static void debugRun(){
        String rootPath = System.getProperty("user.dir");
        FILEPATH = rootPath + "/src/main/java/Case/post_temp3.yaml";
        REPORTPATH = rootPath + "/src/main/java/Report";
        System.setProperty("FILEPATH",FILEPATH);
        System.setProperty("REPORTPATH",REPORTPATH);
        System.out.println("debugRun");
        TestNG testNG = new TestNG();
        TestListenerAdapter listener = new TestListenerAdapter();
        testNG.addListener(listener);
        testNG.setTestClasses(new Class[]{Requests2.class});
        testNG.run();
    }


    /**
     * 执行参数
     * @param args
     * @throws Exception
     */
    private static void executeParameter(String[] args){
        int optSetting = 0;
        for (; optSetting < args.length; optSetting++) {
            if ("-f".equals(args[optSetting])) {
                FILEPATH = args[++optSetting];
            }else if ("-r".equals(args[optSetting])) {
                REPORTPATH = args[++optSetting];
            } else if ("-v".equals(args[optSetting])) {
                LEVEL= args[++optSetting];
            }else if ("-h".equals(args[optSetting])) {
                NEEDHELP = true;
                System.out.println("-f:测试用例路径");
                System.out.println("-r:报告文件夹");
                System.out.println("-v:日志等级");
                break;
            }
        }
        System.setProperty("FILEPATH",FILEPATH);
        System.setProperty("REPORTPATH",REPORTPATH);
        new JUnitCore().run(Request.classes(Requests2.class));
    }


}

