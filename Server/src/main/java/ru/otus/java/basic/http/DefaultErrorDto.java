package ru.otus.java.basic.http;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DefaultErrorDto {
    private String code;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    public DefaultErrorDto(String code, String message) {
        this.code = code;
        this.message = message;
        this.date = new Date();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DefaultErrorDto() {
    }
}
