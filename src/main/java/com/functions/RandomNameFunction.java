package com.functions;

import com.utils.RandomUtils;

public class RandomNameFunction implements Function {

    @Override
    public String execute(String[] args) {
        return RandomUtils.getChineseName();
    }

    @Override
    public String getReferenceKey() {
        return "randomName";
    }

}
