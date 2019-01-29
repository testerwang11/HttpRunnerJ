package com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.HashMap;

@Getter
@Setter
@ToString
public class ConfigModel {
    private String name;

    private HashMap<String, Object> variables;

    private RequestModel request;


}
