package com.model;

import Model.RequestModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class DetailModel {

    private String name;

    private RequestModel request;

    private List<ExtractModel> extract;

    private List<ValidateModel> validate;

}
