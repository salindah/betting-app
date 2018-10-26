package com.bettingapp.betting.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 26/10/18
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class BetOfferTest {

    @Test
    public void addCustomerStakeTest() {

        int betOfferId = 888;
        int customerId = 1234;
        int stake = 2500;

        BetOffer betOffer = new BetOffer(betOfferId);

        betOffer.addCustomerStake(customerId, stake);

        Assert.assertNotNull(betOffer.getCustomerStakeMap());
        Assert.assertEquals(betOffer.getCustomerStakeMap().size(), 1);
    }
}
