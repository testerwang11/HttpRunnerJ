package com.functions;

import com.utils.RandomUtils;

public class RandomStrFunction implements Function {

    @Override
    public String execute(String[] args) {
        int len = args.length;
        if (len > 0) {
            return RandomUtils.getRandomStr(Integer.valueOf(args[0]), Integer.valueOf(args[0]));
        } else {
            return RandomUtils.getRandomStr(16, 16);
        }
    }

    @Override
    public String getReferenceKey() {
        return "randomStr";
    }

}
