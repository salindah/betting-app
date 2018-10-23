package com.bettingapp.service;

import com.bettingapp.common.model.RequestDetails;
import com.bettingapp.common.model.ResponseDetails;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 22/10/18
 * Time: 12:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Service {

    public ResponseDetails serveRequest(RequestDetails requestDetails);
}
