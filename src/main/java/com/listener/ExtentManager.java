/*
package com.listener;


import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.NetworkMode;

import static Tools.DataUntils.timeDate;

public class ExtentManager {
    private static ExtentReports extent;

    private static String reportPath = String.format(System.getProperty("REPORTPATH")
            + "/reports/report_%s.html", timeDate());

    public static ExtentReports getInstance() {
        if (extent == null)
            createInstance();
        return extent;
    }

    public static void createInstance() {
        extent = new ExtentReports(reportPath, true, NetworkMode.OFFLINE);
    }

    public static ExtentReports getExtent() {
        return extent;
    }

}
*/
