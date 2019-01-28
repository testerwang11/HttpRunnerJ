package com.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/***
 * 封装常用断言
 */


public class Assert {

    public  static void assertEqual(Object check,Object expect){
        assertThat(check, equalTo(expect));
    }

    public  static void assertLessthan(int check,int expect){
        assertThat(check, lessThan(expect));
    }

    public  static void assertGreaterthan(int check,int expect){
        assertThat(check, greaterThan(expect));
    }


}
