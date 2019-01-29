package com.functions;


import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFunction implements Function{

	@Override
	public String execute(String[] args) {
		if (args.length == 0 || StringUtils.isEmpty(args[0])) {
			return String.format("%s", new Date().getTime());
		} else {
			return getCurrentDate(args);
		}
	}

	private String getCurrentDate(String ... pattern) {
	    int day = 0;
	    if(pattern.length==2){
            day = Integer.valueOf(pattern[1]);
        }
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, day);
        SimpleDateFormat format = new SimpleDateFormat(pattern[0]);
		String str = format.format(c.getTime());
		return str;
	}
	
	@Override
	public String getReferenceKey() {
		return "date";
	}

}
