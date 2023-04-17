package com.example.zzapdiz.share;

import lombok.Getter;

@Getter
public class ResponseBody <T>{

    private final int statusCode;
    private final String statusMessage;
    private final T data;

    public ResponseBody(StatusCode statusCode, T data){
        this.statusCode = statusCode.getStatusCode();
        this.statusMessage = statusCode.getStatusMessage();
        this.data = data;
    }
}
