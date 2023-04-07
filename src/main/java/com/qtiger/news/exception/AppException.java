package com.qtiger.news.exception;

import com.qtiger.news.constant.ErrorCode;

public class AppException extends Exception{
    private String errorMsg;
    private String errorCode;
    private Object result;

    public AppException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorCode.getMessage();
    }

    public AppException(String code, String message, Object result){
        super(message);
        this.errorCode = code;
        this.errorMsg = message;
        this.result = result;
    }

    public AppException(String code, String message){
        super(message);
        this.errorCode = code;
        this.errorMsg = message;
    }
}
