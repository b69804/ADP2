package com.firstproject.androidstudio.matthewashton.represntr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewashton on 10/12/14.
 */

import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.CONGRESS_MAP;
public class JSONParser {



    public static List<CongressMember> parseFeed(String result){

        try {
            JSONObject jObj = new JSONObject(result);
            JSONArray newObj = jObj.getJSONArray("results");
            JSONObject ar = newObj.getJSONObject(0);
            String hOrS = ar.getString("chamber");
            JSONArray obj = ar.getJSONArray("members");
            List<CongressMember> congressList = new ArrayList<CongressMember>();

            for (int i = 0; i < obj.length(); i++) {
                JSONObject testObject = obj.getJSONObject(i);
                String firstName = testObject.getString("first_name");
                String lastName = testObject.getString("last_name");
                String uniID = testObject.getString("id");
                String personParty = testObject.getString("party");
                String personState = testObject.getString("state");
                String personMissedVotes = testObject.getString("missed_votes_pct");
                String personVotesWithParty = testObject.getString("votes_with_party_pct");

                String fullName = firstName + " " + lastName;
                CongressMember eachPerson = new CongressMember(i, uniID);
                eachPerson.setName(fullName);
                eachPerson.setHouseOrSenate(hOrS);
                eachPerson.setUniqueID(uniID);
                eachPerson.setRandomID(i);
                eachPerson.setParty(personParty);
                eachPerson.setState(personState);
                eachPerson.setMissedVotes(personMissedVotes);
                eachPerson.setVotesWithParty(personVotesWithParty);
                congressList.add(eachPerson);
                CONGRESS_MAP.put(uniID, eachPerson);
            }
            return congressList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<CongressMember> parseHouseFeed(String result){

        try {
            JSONObject jObj = new JSONObject(result);
            JSONArray newObj = jObj.getJSONArray("results");
            JSONObject ar = newObj.getJSONObject(0);

            JSONArray obj = ar.getJSONArray("members");
            List<CongressMember> congressList = new ArrayList<CongressMember>();

            for (int i = 0; i < obj.length(); i++) {
                JSONObject testObject = obj.getJSONObject(i);
                String firstName = testObject.getString("first_name");
                String lastName = testObject.getString("last_name");
                String uniID = testObject.getString("id");
                String personParty = testObject.getString("party");
                String personState = testObject.getString("state");
                String personMissedVotes = testObject.getString("missed_votes_pct");
                String personVotesWithParty = testObject.getString("votes_with_party_pct");

                String fullName = firstName + " " + lastName;
                CongressMember eachPerson = new CongressMember(i, uniID);
                eachPerson.setName(fullName);
                eachPerson.setHouseOrSenate("House");
                eachPerson.setUniqueID(uniID);
                eachPerson.setRandomID(100 + i);
                eachPerson.setParty(personParty);
                eachPerson.setState(personState);
                eachPerson.setMissedVotes(personMissedVotes);
                eachPerson.setVotesWithParty(personVotesWithParty);
                congressList.add(eachPerson);
                CONGRESS_MAP.put(uniID, eachPerson);
            }
            return congressList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


}
