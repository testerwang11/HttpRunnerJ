package com.core;

import com.model.TestCaseModel;
import com.utils.AssertUtil;
import com.utils.FunctionUtil;
import com.utils.SignUtil;
import com.alibaba.fastjson.JSON;
import com.model.*;
import com.utils.ClientUitl;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class Requests {

    public static Response response;

    public static ResponseSpecBuilder rb = new ResponseSpecBuilder();

    public static ResponseSpecification rs;

    private static List<TestCaseModel> testCaseList;

    //全局变量
    public static HashMap<String, Object> vars = new HashMap<>();

    //保存响应结果，传递后续使用
    public static List<Response> response_list = new ArrayList<>();

    private static ConfigModel config;

    private static Pattern mat = Pattern.compile("(?<=\\（)(\\S+)(?=\\）)");

    private static void addGlobalVar() {
        vars = config.getVariables();
    }

    @BeforeSuite
    public static void beforeSuite() {
        rb.expectResponseTime(lessThan(1000L));
        rs = rb.build();
    }

    @Parameters({"configStr", "testCaseListStr"})
    @BeforeTest
    public static void beforeTest(@Optional("") String configStr, @Optional("") String testCaseListStr) {
        System.out.println("=====BeforeTest=====");
        config = JSON.parseObject(configStr, ConfigModel.class);
        testCaseList = JSON.parseArray(testCaseListStr, TestCaseModel.class);
        addGlobalVar();
    }


    @BeforeClass
    public static void beforeClass() {
        System.out.println("=====beforeClass=====");
    }


    @BeforeMethod
    public void beforeMethod() {
        System.out.println("=====beforeMethod=====");
    }


    @DataProvider(name = "testCaseData")
    public Iterator<Object> dataProvider() {
        System.out.println("=====dataProvider=====");

        List<Object> item = new ArrayList<Object>();
        Iterator it = testCaseList.iterator();
        while (it.hasNext()) {
            TestCaseModel testCaseModel = (TestCaseModel) it.next();
            item.add(testCaseModel);
        }
        return item.iterator();
    }

    @Test(dataProvider = "testCaseData")
    public void run(TestCaseModel testCaseModel) {
        DetailModel detailModel = testCaseModel.getTest();
        //读取body
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

        //判断URL
        String url;
        if (detailModel.getRequest().getUri().startsWith("https") || detailModel.getRequest().getUri().startsWith("http")) {
            url = detailModel.getRequest().getUri();
        } else {
            url = config.getRequest().getHost() + detailModel.getRequest().getUri();
        }

        if (detailModel.getRequest().getMethod().equalsIgnoreCase("POST")) {
            response = ClientUitl.post(url, detailModel.getRequest().getHeaders(), oData);
        } else if (detailModel.getRequest().getMethod().equalsIgnoreCase("GET")) {
            response = ClientUitl.get(url, detailModel.getRequest().getHeaders(), oData);
        }

        //请求结果保存至list
        response_list.add(response);
        //验证断言结果
        validateResponse(response, detailModel.getValidate());
        //提取变量信息
        extractResponse(response, detailModel.getExtract());
    }

    /**
     * 替换body中变量
     *
     * @param body
     * @return
     */
    private HashMap<String, Object> replaceParam(HashMap<String, Object> body) {
        Iterator<String> iterator = body.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = body.get(key).toString();
            String result = null;
            if (value.startsWith("$")) {
                String temp = value.substring(2, value.length() - 1);
                //String args = mat.matcher(temp).group();

                temp = temp.replace("(", "").replace(")", "");
                System.out.println(temp);
                if (!StringUtils.isEmpty(System.getProperty(temp))) {
                    //判断是不是系统变量
                    result = System.getProperty(temp);
                } else if (FunctionUtil.isFunction(temp)) {
                    //判断是不是随机变量
                    String[] args2 = new String[0];
                    result = FunctionUtil.getValue(temp, args2);
                } else if (vars.containsKey(temp)) {
                    result = vars.get(temp).toString();
                } else {
                    Reporter.log(String.format("没有找到变量:%s,请仔细检查", value));
                }
                body.put(key, result);
                Reporter.log(String.format("替换变量%s为%s", value, result));
            }
        }
        return body;
    }

/*    private HashMap<String, Object> replaceParam2(HashMap<String, Object> body) {
        Iterator<String> iterator = body.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = body.get(key).toString();
            if (value.startsWith("$.")) {
                Reporter.log(String.format("替换变量%s为%s", value, vars.get(value.substring(2))));
                body.put(key, vars.get(value.substring(2)));
            }
        }
        return body;
    }*/

    public void validateResponse(Response response, List<ValidateModel> validates) {
        String checkKey;
        for (ValidateModel validate : validates) {
            checkKey = validate.getCheck();
            if (checkKey.equalsIgnoreCase("status_code")) {
                assertThat("断言status_code", response.getStatusCode(), equalTo(validate.getExpect()));
            } else {
                if (checkKey.startsWith("body")) {
                    AssertUtil.selectAssert(validate.getComparator(), response.getBody().jsonPath().getString(checkKey.substring(5)), validate.getExpect().toString());
                } else if (checkKey.startsWith("header")) {
                    AssertUtil.selectAssert(validate.getComparator(), response.header(checkKey.substring(5)), validate.getExpect().toString());
                }
            }
        }
    }


    private void extractResponse(Response response, List<ExtractModel> extracts) {
        String extractKey;
        String vname;
        Object value = "";
        for (ExtractModel extract : extracts) {
            vname = extract.getName();
            extractKey = extract.getPath();

            if (extractKey.equalsIgnoreCase("status_code")) {
                Reporter.log(String.format("变量名称:%s,变量值:%s", vname, response.getStatusCode()));
                value = response.getStatusCode();
            } else {
                if (extractKey.startsWith("body")) {
                    value = response.getBody().jsonPath().getString(extractKey.substring(5));
                } else if (extractKey.startsWith("header")) {
                    value = response.header(extractKey.substring(7));
                }
                Reporter.log(String.format("提取变量:%s,变量值为:%s", vname, value));
                vars.put(vname, value);
            }
        }
    }

    @AfterMethod
    public static void afterMethod() {

    }

    @AfterTest
    public static void afterTest() {


    }

    @AfterSuite
    public static void afterSuite() {

    }

    @AfterClass
    public static void teardown() {

    }

}
