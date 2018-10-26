package com.bettingapp.betting;

import com.bettingapp.common.model.RequestDetails;
import com.bettingapp.common.model.ResponseDetails;
import com.bettingapp.session.SessionService;
import com.bettingapp.session.model.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 26/10/18
 * Time: 9:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class BettingServiceTest {

    private static String INTERNAL_ERROR_1 = "Session ID was not found in the request";

    private SessionService sessionService;

    private BettingService bettingService;

    @Before
    public void setUp() {
        sessionService = SessionService.getInstance();
        bettingService = BettingService.getInstance();
    }

    @Test
    public void addStakeTest() throws Exception {

        int customerId = 1234;
        int betOfferId = 888;
        Session session = sessionService.getSession(customerId);

        Assert.assertNotNull(session);
        Assert.assertNotNull(session.getSessionId());

        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setId(betOfferId);

        HashMap<String, String> queryParameterMap = new HashMap<>();
        queryParameterMap.put("sessionkey", session.getSessionId());
        requestDetails.setQueryParameterMap(queryParameterMap);
        requestDetails.setContent("4500");

        ResponseDetails response = bettingService.addStake(requestDetails);
        Assert.assertNotNull(response);
        Assert.assertEquals(ResponseDetails.OK, response.getCode());
        Assert.assertEquals("", response.getBody());
    }

    @Test
    public void addStakeInvalidTest() throws Exception {

        int betOfferId = 888;
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setId(betOfferId);

        HashMap<String, String> queryParameterMap = new HashMap<>();
        requestDetails.setQueryParameterMap(queryParameterMap);

        requestDetails.setContent("4500");

        ResponseDetails response = bettingService.addStake(requestDetails);

        Assert.assertNotNull(response);
        Assert.assertEquals(ResponseDetails.INTERNAL_ERROR, response.getCode());
        Assert.assertEquals(INTERNAL_ERROR_1, response.getBody());
    }

    @Test
    public void getHighestStakesListTest() throws Exception {

        int betOfferId = 888;
        int customerId1 = 1111;
        int customerId2 = 2222;

        // Customer 1 adding couple of stakes to a bet offer
        RequestDetails request1 = new RequestDetails();
        request1.setId(betOfferId);

        Session session1 = sessionService.getSession(customerId1);
        HashMap<String, String> parameterMap1 = new HashMap<>();
        parameterMap1.put("sessionkey", session1.getSessionId());
        request1.setQueryParameterMap(parameterMap1);
        request1.setContent("1500");
        bettingService.addStake(request1);

        request1.setContent("1800");
        bettingService.addStake(request1);


        // Customer 2 adding couple of stakes to a bet offer
        RequestDetails request2 = new RequestDetails();
        request2.setId(betOfferId);

        Session session2 = sessionService.getSession(customerId2);
        HashMap<String, String> parameterMap2 = new HashMap<>();
        parameterMap2.put("sessionkey", session2.getSessionId());
        request2.setQueryParameterMap(parameterMap2);
        request2.setContent("2500");
        bettingService.addStake(request2);

        request2.setContent("2900");
        bettingService.addStake(request2);

        //Request highest stake list
        RequestDetails highStakeListRequest = new RequestDetails();
        highStakeListRequest.setId(betOfferId);

        ResponseDetails highStakeListResponse = bettingService.getHighestStakesList(highStakeListRequest);
        Assert.assertNotNull(highStakeListResponse);
        Assert.assertEquals(ResponseDetails.OK, highStakeListResponse.getCode());
        Assert.assertNotNull(highStakeListResponse.getBody());
        Assert.assertEquals("2222=2900,1111=1800", highStakeListResponse.getBody());

    }
}
