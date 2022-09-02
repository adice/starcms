package com.starrysky.starcms.util;

/**
 * @ClassName UploadResponse
 * @Description
 * @Author adi
 * @Date 2022-08-05 14:50
 */
public class FileUploadResponse {
    private int code;
    private String msg;
    private String url;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
