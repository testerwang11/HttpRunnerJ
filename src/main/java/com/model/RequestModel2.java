package com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class RequestModel2 {
    private String host;
    private Map<String, Object> headers;
}
