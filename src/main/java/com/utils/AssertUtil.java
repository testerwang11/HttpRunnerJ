package com.utils;

import org.hamcrest.Matcher;
import org.testng.Reporter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/***
 * 封装常用断言
 */


public class AssertUtil {

    /**
     *
     * @param key nq,gt,lt,eq
     * @param actual
     * @param expect
     */
    public static  void selectAssert(String key, String actual, String expect) {
        Reporter.log("检查响应结果");
        key = key.toLowerCase();
        switch (key){
            case "eq":
                assertThat(actual, equalTo(expect));
                break;
            case "gt":
                assertThat(actual, greaterThan(expect));
                break;
            case "lt":
                assertThat(actual, lessThan(expect));
                break;
            case "nq":
                assertThat(actual, not(expect));
                break;
            case "ct":
                assertThat(actual, hasToString(expect));
                break;
            case "nnv":
                assertThat(actual, notNullValue());
                break;
                default:
                    System.out.println("暂不支持该类型断言!"+key);
                    assertFalse(true);
                    break;
        }
    }







}
