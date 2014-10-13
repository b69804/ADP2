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
    private int randomID;

    public CongressMember (int id, String congressID){
        id = randomID;
        congressID = uniqueID;
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
}
