package com.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Reporter;

import java.util.HashMap;

import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class ClientUitl {

    public static RequestSpecBuilder rsb = new RequestSpecBuilder();
    public static ResponseSpecBuilder rb = new ResponseSpecBuilder();

    public static ResponseSpecification rs;


    static {
        //RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
        responseFilters();
        rb.expectResponseTime(lessThan(1000L));
        rs = rb.build();
    }

    public static void responseFilters() {
        filters((new Filter() {
                    public Response filter(FilterableRequestSpecification requestSpec,
                                           FilterableResponseSpecification responseSpec, FilterContext ctx) {

                        Response response = ctx.next(requestSpec, responseSpec);
                        Reporter.log("开启响应拦截器");
                        Reporter.log("请求地址:"+ requestSpec.getURI());
                        Reporter.log("发送参数:"+ requestSpec.getRequestParams().toString());
                        Reporter.log("响应结果:"+ response.getBody().asString());
                        Response newResponse = new ResponseBuilder().clone(response)
                                .setContentType(ContentType.JSON)
                                .build();
                        return newResponse;
                    }
                })
        );
    }


    public static Response post(String url, HashMap<String, Object> headers, HashMap<String, Object> data) {
        //return (Response) given().headers(headers).formParams(data).when().post(url).then().extract();
        return (Response) given().headers(headers).params(data).when().post(url).then().extract();

    }

    public static Response get(String url, HashMap<String, Object> headers, HashMap<String, Object> data) {
        return (Response) given().headers(headers).params(data).when().get(url).then().extract();
    }


}
