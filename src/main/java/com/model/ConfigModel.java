package com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class ConfigModel {
    private String name;

    private HashMap<String, Object> variables;

    private RequestModel2 request;


}
