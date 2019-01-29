package com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.HashMap;

@Getter
@Setter
@ToString
public class RequestModel {
    private String host;
    private String uri;
    private String method;
    private HashMap<String, Object> headers;
    private HashMap<String, Object> body;
    private String notSignsParams;
}
