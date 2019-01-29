package com.core;


import com.alibaba.fastjson.JSON;
import com.listener.ExtentTestNGIReporterListener;
import com.model.TestDataModel;
import com.utils.CommonUtils;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import java.util.ArrayList;
import java.util.List;


public class Run {

    private static String FILEPATH;
    private static String REPORTPATH;
    private static String LEVEL;
    private static boolean NEEDHELP = false;
    private static boolean DEBUG = true;


    public static void main(String[] args) {
        if (DEBUG) {
            debugRun();
        } else {
            executeParameter(args);
        }

    }


    /**
     * 源码运行
     *
     * @throws Exception
     */

    private static void debugRun() {
        String rootPath = System.getProperty("user.dir");
        //FILEPATH = rootPath + "/src/main/java/Case2/post_temp1.yaml";
        FILEPATH = rootPath + "/src/main/java/Case2/";

        REPORTPATH = rootPath + "/src/main/java/Report";
        System.setProperty("FILEPATH", FILEPATH);
        System.setProperty("REPORTPATH", REPORTPATH);
        System.out.println("debugRun");
        TestNG testNG = new TestNG();
        testNG.setOutputDirectory("target/test-output");
        testNG.addListener(new ExtentTestNGIReporterListener());
        testNG.setXmlSuites(makeSuites());
        testNG.run();
    }

    private static List<XmlSuite> makeSuites() {
        List<TestDataModel> testCaseList = CommonUtils.getTestDataModel(System.getProperty("FILEPATH"));
        List<XmlSuite> suites = new ArrayList<>();
        for (TestDataModel testDataModel : testCaseList) {
            XmlSuite suite = new XmlSuite();
            suite.setName(testDataModel.getConfig().getName());
            XmlTest test = new XmlTest(suite);
            test.setName("T");
            test.addParameter("configStr", JSON.toJSONString(testDataModel.getConfig()));
            test.addParameter("testCaseListStr", JSON.toJSONString(testDataModel.getTestSuit()));

            List<XmlClass> classes = new ArrayList<>();
            classes.add(new XmlClass(Requests.class));
            test.setXmlClasses(classes);
            suites.add(suite);
        }
        return suites;
    }

    /**
     * 执行参数
     *
     * @param args
     * @throws Exception
     */

    private static void executeParameter(String[] args) {
        int optSetting = 0;
        for (; optSetting < args.length; optSetting++) {
            if ("-f".equals(args[optSetting])) {
                FILEPATH = args[++optSetting];
            } else if ("-r".equals(args[optSetting])) {
                REPORTPATH = args[++optSetting];
            } else if ("-v".equals(args[optSetting])) {
                LEVEL = args[++optSetting];
            } else if ("-h".equals(args[optSetting])) {
                NEEDHELP = true;
                System.out.println("-f:测试用例路径");
                System.out.println("-r:报告文件夹");
                System.out.println("-v:日志等级");
                break;
            }
        }
        System.setProperty("FILEPATH", FILEPATH);
        System.setProperty("REPORTPATH", REPORTPATH);
        TestNG testNG = new TestNG();
        testNG.setOutputDirectory("target/test-output");

        testNG.addListener(new ExtentTestNGIReporterListener());
        testNG.setXmlSuites(makeSuites());
        testNG.run();
    }


}
