package Http;

import Model.AllModel;
import Model.TestCaseModel;
import Tools.ExtentUtils;
import Tools.MyLogger;
import Tools.SignUtil;
import com.model.*;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;
import com.utils.ClientUitl;
import com.utils.YamlUtils;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.hamcrest.Matcher;
import org.testng.ITestContext;
import org.testng.annotations.*;
import java.io.File;
import java.util.*;
import static Tools.DataUntils.timeDate;
import static Tools.MyLogger.initLogger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;


public class Requests2 {

    public static Response response;

    public Object check;
    public Object value;
    public Object exp;
    public String ContentTypeJson = "application/json";
    public String ContentTypeFrom = "application/x-www-form-urlencoded";

    public static RequestSpecBuilder rsb = new RequestSpecBuilder();
    public static ResponseSpecBuilder rb = new ResponseSpecBuilder();

    public static ResponseSpecification rs;

    public static String LEVEL = "ALL";
    private static ExtentReports extent;
    private static String reportPath = String.format(System.getProperty("REPORTPATH")
            + "/reports/report_%s.html", timeDate());
    public static MyLogger logger;
    public static ExtentTest extentTest;
    public static File[] files;


    //全局变量
    public static HashMap<String, Object> vars = new HashMap<>();

    //保存响应结果，传递后续使用
    public static List<Response> response_list = new ArrayList<>();


    public ExtentUtils eu = new ExtentUtils(extent, extentTest);

    private static ConfigModel config;


    private static void addGlobalVar() {
        HashMap<String, Object> vars = config.getVariables();

    }

    @BeforeClass
    public static void setup() {
        extent = new ExtentReports(reportPath, true, NetworkMode.OFFLINE);
        logger = new MyLogger(extent, extentTest);
        logger.log_info("初始化全局参数");
        rb.expectResponseTime(lessThan(1000L));
        rs = rb.build();
        initLogger().setLevel(Level.ALL);

    }

    @BeforeSuite
    public static void beforeTest() {
        //addGlobalVar();
        //addRequest();
    }

    @BeforeMethod
    public void beforeMethod(ITestContext context) {
        extentTest = extent.startTest("接口测试", "-");

    }


    @DataProvider(name = "testCaseData")
    public Iterator<Object> dataProvider() throws Exception {
        List<Object> item = new ArrayList<Object>();

        //extent = new ExtentReports(reportPath, true, NetworkMode.OFFLINE);
        //extentTest = extent.startTest("接口测试", "-");
        //logger = new MyLogger(extent, extentTest);
        TestDataModel testDataModel = YamlUtils.loadData();
        config = testDataModel.getConfig();
        Iterator it = testDataModel.getTestSuit().iterator();
        while (it.hasNext()) {
            TestCaseModel testCaseModel = (TestCaseModel) it.next();
            item.add(testCaseModel.getTest());
        }
        return item.iterator();
    }

    @Test(dataProvider = "testCaseData")
    public void run(DetailModel detailModel) {

        //System.out.println("detailModel = [" + detailModel.getName() + "]");

        HashMap<String, Object> oData = detailModel.getRequest().getBody();
        //替换变量
        oData = replaceParam(oData);

        //签名
        String key;
        if (!StringUtils.isEmpty(detailModel.getRequest().getNotSignsParams())) {
            key = SignUtil.getSign2(oData, Arrays.asList(detailModel.getRequest().getNotSignsParams().split(",")), oData.get("appcode").toString());
        } else {
            key = SignUtil.getSign2(oData, null, oData.get("appcode").toString());
        }
        oData.put("sign", key);

        if (detailModel.getRequest().getMethod().equalsIgnoreCase("POST")) {
            response = ClientUitl.post(config.getRequest().getHost()+"/"+detailModel.getRequest().getUri(), detailModel.getRequest().getHeaders(), oData);
        } else if (detailModel.getRequest().getMethod().equalsIgnoreCase("GET")) {
            response =  ClientUitl.get(config.getRequest().getHost()+"/"+detailModel.getRequest().getUri(), detailModel.getRequest().getHeaders(), oData);
        }

        response_list.add(response);

        validateResponse(response, detailModel.getValidate());
        extractResponse(response, detailModel.getExtract());
    }

    /**
     * 替换body中变量
     * @param body
     * @return
     */
    private HashMap<String, Object> replaceParam(HashMap<String, Object> body) {
        Iterator<String> iterator = body.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            System.out.println(key);
            String value = body.get(key).toString();
            if (value.startsWith("$.")) {
                logger.log_info("处理变量:" + value);
                body.put(key, vars.get("sessionID"));
            }
        }
        return body;
    }

    public void validateResponse(Response response, List<ValidateModel> validates) {
        logger.log_info("断言!");
        String checkKey;
        for(ValidateModel validate:validates){
            checkKey = validate.getCheck();
            if(checkKey.equalsIgnoreCase("status_code")){
                selectAssert(validate.getComparator(), response.getStatusCode(), validate.getExpect());
            }else{
                if(checkKey.startsWith("body")){
                    //selectAssert(validate.getComparator(), response.getBody().jsonPath().getString(checkKey.substring(5, checkKey.length()-1)), validate.getExpect());
                }else if(checkKey.startsWith("header")){
                    //selectAssert(validate.getComparator(), response.header(checkKey.substring(5, checkKey.length()-1)), validate.getExpect());
                }
            }
        }
    }



    private void extractResponse(Response response, List<ExtractModel> extracts) {
        logger.log_info("提取变量");

        String extractKey;
        String vname;
        for(ExtractModel extract:extracts){
            vname = extract.getName();
            extractKey = extract.getPath();
            if(extractKey.equalsIgnoreCase("status_code")){
                vars.put(vname, response.getStatusCode());
            }else{
                if(extractKey.startsWith("body")){
                    vars.put(vname, response.getBody().jsonPath().getString(extractKey.substring(5)));
                }else if(extractKey.startsWith("header")){
                    vars.put(vname, response.header(extractKey.substring(7)));
                }
            }
        }
        System.out.println(vars.size());
    }

    @AfterClass
    public static void teardown() {
        extent.close();
        extent.flush();
    }




    public void selectAssert(String key, Object check, Object expect) {
        logger.log_info("实际值:" + check);
        logger.log_info("预期值:" + expect);
        if (key.equals("eq")) {
            Assert.assertEqual(check, expect);
        } else if (key.equals("gt")) {
            //return assertThat(check, greaterThan(expect));

        } else if (key.equals("lt")) {
            Assert.assertLessthan(Integer.valueOf(check.toString()), Integer.valueOf(expect.toString()));
        }
    }

}
