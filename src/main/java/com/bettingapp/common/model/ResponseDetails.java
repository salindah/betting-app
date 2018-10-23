package com.bettingapp.common.model;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 23/10/18
 * Time: 8:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class ResponseDetails {

    public static int OK = 200;

    public static int INTERNAL_ERROR = 500;

    public static int METHOD_NOT_ALLOWED = 405;

    public ResponseDetails(int code, String body){
        this.code = code;
        this.body = body;
    }

    private int code;

    private String body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
