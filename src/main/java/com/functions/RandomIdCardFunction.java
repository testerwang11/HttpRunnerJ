package com.functions;

import com.utils.RandomUtils;

public class RandomIdCardFunction implements Function {

    @Override
    public String execute(String[] args) {
        return RandomUtils.getIdCard();
    }

    @Override
    public String getReferenceKey() {
        return "randomIdCard";
    }

}
