package com.listener;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MyTransformer implements IAnnotationTransformer {

    private static String description;

    private ITestAnnotation iTestAnnotation;

    private Class aClass;

    private Constructor constructor;

    private Method method;


    public static void setDescription(String description2) {
        description = description2;
    }

    @Override
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {
        System.out.println(method.getName());
        if ("run".equals(method.getName())) {
            iTestAnnotation.setDescription(description);
            //System.out.println("设置description2:"+description);

        }
    }


}
