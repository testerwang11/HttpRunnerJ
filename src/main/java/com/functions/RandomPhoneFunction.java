package com.functions;

import com.utils.RandomUtils;

public class RandomPhoneFunction implements Function {

    @Override
    public String execute(String[] args) {
        return RandomUtils.getTel();
    }

    @Override
    public String getReferenceKey() {
        return "random_phone";
    }

}
