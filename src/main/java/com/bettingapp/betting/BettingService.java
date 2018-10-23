package com.bettingapp.betting;

import com.bettingapp.common.model.RequestDetails;
import com.bettingapp.common.model.ResponseDetails;
import com.bettingapp.service.Service;
import com.bettingapp.session.SessionService;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 21/10/18
 * Time: 11:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class BettingService implements Service {

    private static BettingService instance;

    private SessionService sessionService;

    private BettingService(){
        this.sessionService = SessionService.getInstance();
    }

    public static BettingService getInstance(){
        if(instance == null){
            instance = new BettingService();
        }
        return instance;
    }

    @Override
    public ResponseDetails serveRequest(RequestDetails requestDetails) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
