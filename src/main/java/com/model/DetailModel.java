package com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DetailModel {

    private String name;

    private RequestModel request;

    private List<ExtractModel> extract;

    private List<ValidateModel> validate;

}
