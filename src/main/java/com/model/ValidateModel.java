package com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidateModel {
    private String check;

    private String comparator;

    private Object expect;
}
