package com.bettingapp.common.model;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 22/10/18
 * Time: 10:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestDetails {

    private int id;

    private String action;

    private String type;

    private HashMap<String, String> queryParameterMap;

    private String content;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getQueryParameterMap() {
        return queryParameterMap;
    }

    public void setQueryParameterMap(HashMap<String, String> queryParameterMap) {
        this.queryParameterMap = queryParameterMap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
