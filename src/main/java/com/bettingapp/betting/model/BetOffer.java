package com.bettingapp.betting.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 24/10/18
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class BetOffer {

    private int betOfferId;

    private ConcurrentHashMap<Integer, CustomerRecord> customerStakeMap;

    public BetOffer(int id){
        this.betOfferId = id;
        this.customerStakeMap = new ConcurrentHashMap<>();
    }

    public int getBetOfferId() {
        return betOfferId;
    }

    public void setBetOfferId(int betOfferId) {
        this.betOfferId = betOfferId;
    }

    public ConcurrentHashMap<Integer, CustomerRecord> getCustomerStakeMap() {
        return customerStakeMap;
    }

    public void setCustomerStakeMap(ConcurrentHashMap<Integer, CustomerRecord> customerStakeMap) {
        this.customerStakeMap = customerStakeMap;
    }

    public void addCustomerStake(int customerId, int stake){

        CustomerRecord record = customerStakeMap.get(customerId);
        if(record == null){
            record = new CustomerRecord(customerId);
        }
        record.addStake(stake);
        customerStakeMap.put(customerId, record);
    }

    public synchronized String getHighestStakeList() throws Exception{

        if(customerStakeMap.isEmpty()){
             return "";
        }
        List<CustomerRecord> recordList = customerStakeMap.values().stream()
                .collect(Collectors.toList());

        Collections.sort(recordList, (CustomerRecord r1, CustomerRecord r2) -> r2.getHighestStake() - r1.getHighestStake());
        return toCSV(recordList);
    }

    public String toCSV(List<CustomerRecord> recordList){

        if(recordList.isEmpty()){
            return "";
        }
        final StringBuilder stringBuilder = new StringBuilder();
        recordList.stream()
                .filter( record -> recordList.indexOf(record) < 2)
                .forEach(record -> stringBuilder.append(",").append(record.toCSV()));

        return stringBuilder.toString().replaceFirst(",", "");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BetOffer{");
        sb.append("betOfferId=").append(betOfferId);
        sb.append(", customerStakeMap=").append(customerStakeMap);
        sb.append('}');
        return sb.toString();
    }
}
