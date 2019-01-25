package Http;

import Model.AllModel;
import Tools.ExtentUtils;
import Tools.MyLogger;
import Tools.SignUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.ResponseSpecification;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.testng.annotations.*;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static Tools.DataUntils.timeDate;
import static Tools.MyLogger.initLogger;
import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;


public class Requests2 {

    public static List<AllModel> testcase;
    public static Response response;
    public String checkKey;
    public String selectkey;
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

    public static JSONObject config;
    public static JSONArray testCaseList;
    public static String host;
    public static JSONObject headers;


    //全局变量
    public static HashMap<String, Object> vars = new HashMap<>();

    public static String CASEPATH;

    public static List<String> getCaseFolder(String casefolder) {
        List<String> caselist = new ArrayList<String>();
        File file = new File(casefolder);
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
        CASEPATH = System.getProperty("FILEPATH");
        List<String> caseFolder = getCaseFolder(CASEPATH);
        Object[] objects = (Object[]) caseFolder.toArray();
        logger.log_info("获取用例");

        // 测试数据
        return Arrays.asList(objects);// 将数组转换成集合返回
    }

    public ExtentUtils eu = new ExtentUtils(extent, extentTest);

    /**
     * 加载测试数据
     */
    public static void loadAll(String file) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        Map<String, Object> testData = (Map<String, Object>) yaml.load(new FileInputStream(new File(file)));

        config = JSONObject.fromObject(testData.get("config"));
        testCaseList = JSONArray.fromObject(testData.get("testSuit"));
    }

    /**
     * 添加config变量至全局变量
     */
    private static void addGlobalVar() {
        JSONObject variables = config.getJSONObject("variables");
        Iterator<String> it = variables.keys();
        while (it.hasNext()) {
            String key = it.next();
            String value = variables.getString(key);
            vars.put(key, value);
        }
    }

    /**
     * 处理Request全局数据
     */
    private static void addRequest() {
        JSONObject request = config.getJSONObject("request");
        request.getString("host");
        headers = request.getJSONObject("headers");
    }


    @BeforeClass
    public static void setup() {
        extent = new ExtentReports(reportPath, true, NetworkMode.OFFLINE);
        extentTest = extent.startTest("接口测试", "-");
        logger = new MyLogger(extent, extentTest);
        logger.log_info("初始化全局参数");
        rb.expectResponseTime(lessThan(1000L));
        rs = rb.build();

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        initLogger().setLevel(Level.ALL);
        RestAssured.useRelaxedHTTPSValidation();
        responseFilters();
    }

    @BeforeSuite
    public static void beforeTest() throws FileNotFoundException {
        loadAll(CASEPATH);
        addGlobalVar();
        addRequest();
    }

    /**
     * 响应拦截器
     */
    public static void responseFilters() {
        filters((new Filter() {
                    public Response filter(FilterableRequestSpecification requestSpec,
                                           FilterableResponseSpecification responseSpec, FilterContext ctx) {
                        Response response = ctx.next(requestSpec, responseSpec);
                        Response newResponse = new ResponseBuilder().clone(response)
                                .setContentType(ContentType.JSON)
                                .build();
                        logger.log_info("开启响应拦截器");
                        return newResponse;
                    }
                })
        );
    }


    @DataProvider(name = "testCaseData")
    public Iterator<Object[]> dataProvider() throws FileNotFoundException {
        extent = new ExtentReports(reportPath, true, NetworkMode.OFFLINE);
        extentTest = extent.startTest("接口测试", "-");
        logger = new MyLogger(extent, extentTest);

        prepareData();
        loadAll(CASEPATH);
        return testCaseList();
    }

    @Test(dataProvider = "testCaseData")
    public void run(JSONObject testCase) throws IOException {
        logger.log_info("开始测试!");
        String testName = testCase.getString("name");
        String uri = testCase.getJSONObject("request").getString("uri");
        String method = testCase.getJSONObject("request").getString("method");
        JSONObject headers = testCase.getJSONObject("request").getJSONObject("headers");
        JSONObject body = testCase.getJSONObject("request").getJSONObject("body");
        String notSignsParams = testCase.getJSONObject("request").getString("notSignsParams");
        JSONArray extract = testCase.getJSONArray("extract");
        JSONArray validate = testCase.getJSONArray("validate");

        logger.log_info("接口名称:" + testName);
        logger.log_info("请求方式:" + method);
        logger.log_info("请求uri:" + uri);
        logger.log_info("Headers:" + headers);
        logger.log_info("body:" + body);
        logger.log_info("notSignsParams:" + notSignsParams);

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> mHeaders = mapper.readValue(headers.toString(), HashMap.class);

        HashMap<String, Object> mData = mapper.readValue(body.toString(), HashMap.class);
        //替换变量
        mData = replaceParam(mData);
        //签名
        String key;
        if (!StringUtils.isEmpty("notSignsParams")) {
            key = SignUtil.getSign2(mData, Arrays.asList(notSignsParams.split(",")), body.getString("appcode"));
        } else {
            key = SignUtil.getSign2(mData, null, body.getString("appcode"));
        }
        mData.put("sign", key);

        if (method.equalsIgnoreCase("POST")) {
            response = (Response) given().headers(mHeaders)
                    .body(mData).when().post(uri).then().extract();
        } else if (method.equalsIgnoreCase("GET")) {
            response = (Response) given().headers(headers).
                    params(mData).when().get(uri).then().extract();
        }
        validateResponse(response, validate);
        extractResponse(response, extract);
    }

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

    private void validateResponse(Response response, JSONArray validate) {
        Iterator vIt = validate.iterator();
        while (vIt.hasNext()) {
            JSONObject v = (JSONObject) vIt.next();
            v.getString("check");
            v.getString("comparator");
            v.getOrDefault("expect", null);
            logger.log_info("断言类型:" + v.getString("check"));

            if (v.getString("check").equalsIgnoreCase("status_code")) {
                selectAssert(v.getString("comparator"), response.statusCode(), v.getOrDefault("expect", null));
            } else {
                logger.log_info("响应解析的值:" + response.getBody().jsonPath().getString(v.getString("check")));
                selectAssert(v.getString("comparator"), response.getBody().jsonPath().getString(v.getString("check")), v.getOrDefault("expect", null));
            }
        }
    }

    private void extractResponse(Response response, JSONArray extract) {
        Iterator vIt = extract.iterator();
        while (vIt.hasNext()) {
            JSONObject v = (JSONObject) vIt.next();
            Iterator<String> it = v.keys();
            while (it.hasNext()) {
                String key = it.next();
                String value = v.getString(key);
                if (value.startsWith("body")) {
                    value = response.getBody().jsonPath().getString(value.split("body.")[1]);
                } else if (value.startsWith("header")) {
                    value = response.getHeader(value.split("header.")[1]);
                } else if (value.startsWith("cookies")) {
                    value = response.getCookie(value.split("cookies.")[1]);
                }
                logger.log_info(String.format("变量%s提取值:%s", key, value));
                vars.put(key, value);
            }
        }
    }

    @AfterClass
    public static void teardown() throws IOException {
    }


    /**
     * 选择不同类型的断言方法
     *
     * @param key
     * @param check
     * @param expect
     */
    public void selectAssert(String key, Object check, Object expect) {
        logger.log_info("实际值:" + check);
        logger.log_info("预期值:" + expect);
        if (key.equals("eq")) {
            Assert.assertEqual(check, expect);
        } else if (key.equals("gt")) {
            Assert.assertGreaterthan(Integer.valueOf(check.toString()), Integer.valueOf(expect.toString()));
        } else if (key.equals("lt")) {
            Assert.assertLessthan(Integer.valueOf(check.toString()), Integer.valueOf(expect.toString()));
        }
    }

}
