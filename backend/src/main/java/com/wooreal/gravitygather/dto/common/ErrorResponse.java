package com.wooreal.gravitygather.dto.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(String error, String message){
        this.error = error;
        this.message = message;
    }
}
