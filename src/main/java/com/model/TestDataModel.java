package com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TestDataModel {

    private ConfigModel config;

    private List<TestCaseModel> testSuit;

}
