package com.bettingapp.betting;

import com.bettingapp.betting.model.BetOffer;
import com.bettingapp.common.model.RequestDetails;
import com.bettingapp.common.model.ResponseDetails;
import com.bettingapp.service.Service;
import com.bettingapp.session.SessionService;
import com.bettingapp.session.model.Session;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

    private ConcurrentHashMap<Integer, BetOffer> betOfferMap = new ConcurrentHashMap<>();

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

        ResponseDetails response = null;
        try {
            if( requestDetails.getAction().equals("stake")){
                response = addStake(requestDetails);
            } else if (requestDetails.getAction().equals("highstakes")){
                response = getHighestStakesList(requestDetails);
            }
        } catch (Exception e) {
            response = new ResponseDetails(ResponseDetails.INTERNAL_ERROR, "An error occurred while accessing the betting service.");
            e.printStackTrace();
        }
        return response;
    }

    public ResponseDetails addStake(RequestDetails requestDetails) throws Exception{

        ResponseDetails response = null;
        String sessionId = requestDetails.getQueryParameterMap().get("sessionkey");
        if(sessionId != null){
            Session session = sessionService.getSession(sessionId);
            if(session != null){
                int betOfferId = requestDetails.getId();
                BetOffer betOffer = betOfferMap.get(betOfferId);
                if(betOffer == null){
                    betOffer = new BetOffer(betOfferId);
                    betOfferMap.put(betOfferId, betOffer);
                }
                betOffer.addCustomerStake(session.getCustomerId(), Integer.valueOf(requestDetails.getContent()));
                response = new ResponseDetails(ResponseDetails.OK, "");
            } else {
                //Implies session has been expired.
                response = new ResponseDetails( ResponseDetails.INTERNAL_ERROR, "Session has been time out");
            }
        }

        betOfferMap.entrySet().forEach(System.out::println);
        return response;
    }

    public ResponseDetails getHighestStakesList(RequestDetails requestDetails) throws Exception{

        ResponseDetails response = null;
        int betOfferId = requestDetails.getId();
        BetOffer betOffer = betOfferMap.get(betOfferId);

        if(betOffer != null){
            String highestStakeList = betOffer.getHighestStakeList();
            response = new ResponseDetails(ResponseDetails.OK, highestStakeList);
        } else {
            response = new ResponseDetails(ResponseDetails.OK, "");
        }
        return response;
    }
}
