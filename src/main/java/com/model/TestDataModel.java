package com.model;

import Model.TestCaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class TestDataModel {

    private ConfigModel config;

    private List<TestCaseModel> testSuit;

}
