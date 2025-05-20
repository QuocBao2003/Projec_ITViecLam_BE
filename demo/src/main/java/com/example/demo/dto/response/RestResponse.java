package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RestResponse<T> {
    private int statusCode;
    private String error;
//message có the là String, hoặc arrayList
    private Object message;
    private T data;

}
