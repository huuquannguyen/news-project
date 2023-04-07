package com.qtiger.news.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNKNOWN_ERROR("UNKNOWN_ERROR", "Lỗi không xác định"),

    FILE_EXTENSION_NOT_MATCH("FILE_EXTENSION_NOT_MATCH", "File không phù hợp");

    private String code;
    private String message;
    ErrorCode (String code, String message) {
        this.code = code;
        this.message = message;
    }
}
