package com.core;

import com.model.TestDataModel;
import com.utils.CommonUtils;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Demo1 {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @BeforeSuite
    public static void beforeSuite() {
        System.out.println("beforeSuite");

    }

    @BeforeTest
    public static void beforeTest() {
        System.out.println("beforeTest");

    }

    @BeforeMethod
    public static void beforeMethod() {
        System.out.println("beforeMethod");
    }

    @DataProvider(name = "testCaseData")
    public Object[] dataProvider() {
        System.out.println("dataProvider");
        String[] a = new String[1];
        a[0] = "a";
        return a;
    }

    @Test(dataProvider = "testCaseData")
    public void test(String a) {

    }
}
