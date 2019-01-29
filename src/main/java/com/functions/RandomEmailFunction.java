package com.functions;

import com.utils.RandomUtils;

public class RandomEmailFunction implements Function {

    @Override
    public String execute(String[] args) {
        return RandomUtils.getEmail(10, 10);
    }

    @Override
    public String getReferenceKey() {
        return "randomEmail";
    }

}
