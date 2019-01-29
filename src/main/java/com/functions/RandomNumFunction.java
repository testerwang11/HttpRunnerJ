package com.functions;

import com.utils.RandomUtils;

public class RandomNumFunction implements Function {

    @Override
    public String execute(String[] args) {
        int len = args.length;
        if (len < 1) {
            return RandomUtils.getNum(10000, 99999) + "";
        } else {
            return RandomUtils.getNum(Integer.valueOf(args[0]), Integer.valueOf(args[1])) + "";
        }
    }

    @Override
    public String getReferenceKey() {
        return "randomNum";
    }

}
