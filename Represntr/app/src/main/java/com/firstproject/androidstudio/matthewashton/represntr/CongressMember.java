package com.firstproject.androidstudio.matthewashton.represntr;

import java.io.Serializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CongressMember implements Serializable {

    private String name;
    private String houseOrSenate;
    private String uniqueID;
    private String state;
    private String party;
    private String missedVotes;
    private String votesWithParty;
    private String dob;
    private String website;
    private String lastVote;
    private String billsSponsored;
    private String apiCall;
    private String memberID;
    private int randomID;

    public CongressMember (String congressName, String congressState){
        this.name = congressName;
        this.state = congressState;
    };


    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getHouseOrSenate() {
        return houseOrSenate;
    }

    public void setHouseOrSenate(String description) {
        this.houseOrSenate = description;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String id) {
        this.uniqueID = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getMissedVotes() {
        return missedVotes;
    }

    public void setMissedVotes(String missedVotes) {
        this.missedVotes = missedVotes;
    }

    public String getVotesWithParty() {
        return votesWithParty;
    }

    public void setVotesWithParty(String votesWithParty) {
        this.votesWithParty = votesWithParty;
    }

    public static Map<String, CongressMember> CONGRESS_MAP = new HashMap<String, CongressMember>();

    public int getRandomID() {
        return randomID;
    }

    public void setRandomID(int randomID) {
        this.randomID = randomID;
    }

    public String getApiCall() {
        return apiCall;
    }

    public void setApiCall(String apiCall) {
        this.apiCall = apiCall;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLastVote() {
        return lastVote;
    }

    public void setLastVote(String lastVote) {
        this.lastVote = lastVote;
    }

    public String getBillsSponsored() {
        return billsSponsored;
    }

    public void setBillsSponsored(String billsSponsored) {
        this.billsSponsored = billsSponsored;
    }

    public static List<CongressMember> SENATE_PEOPLE = new ArrayList<CongressMember>();
    public static List<CongressMember> HOUSE_PEOPLE = new ArrayList<CongressMember>();
    public static Map<String, CongressMember> MAP_OF_HOUSE= new HashMap<String, CongressMember>();
    public static Map<String, CongressMember> MAP_OF_SENATE= new HashMap<String, CongressMember>();
    public static List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    public static Map<String,String> houseMap = new HashMap<String, String>(2);
    public static List<String> HOUSE_NAMES = new ArrayList<String>();
    public static List<String> SENATE_NAMES = new ArrayList<String>();
    public static List<CongressMember> favList = new ArrayList<CongressMember>() {};

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }
}
