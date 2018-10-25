package com.bettingapp.betting.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 24/10/18
 * Time: 10:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomerRecord {

    private int customerId;

    private List<Integer> stakeList;

    private int highestStake;

    public CustomerRecord(int customerId){

        this.customerId = customerId;
        this.stakeList = Collections.synchronizedList(new ArrayList<Integer>());
    }

    public void addStake(int stake){

        this.stakeList.add(stake);
        if(stake > highestStake){
            highestStake = stake;
        }
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Integer> getStakeList() {
        return stakeList;
    }

    public void setStakeList(List<Integer> stakeList) {
        this.stakeList = stakeList;
    }

    public int getHighestStake() {
        return highestStake;
    }

    public void setHighestStake(int highestStake) {
        this.highestStake = highestStake;
    }

    public String toCSV(){
        return this.customerId + "=" + this.highestStake;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerRecord{");
        sb.append("customerId=").append(customerId);
        sb.append(", stakeList=").append(stakeList);
        sb.append(", highestStake=").append(highestStake);
        sb.append('}');
        return sb.toString();
    }

}
