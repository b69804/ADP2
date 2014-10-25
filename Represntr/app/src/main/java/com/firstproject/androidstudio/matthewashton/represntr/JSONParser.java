package com.firstproject.androidstudio.matthewashton.represntr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

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
                String personAPICall = testObject.getString("api_uri");

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
                eachPerson.setApiCall(personAPICall);
                congressList.add(eachPerson);
                CONGRESS_MAP.put(uniID, eachPerson);
            }
            return congressList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static CongressMember parsePerson(String result){

        CongressMember eachPerson;

        try {
            JSONObject jObj = new JSONObject(result);
            JSONArray newObj = jObj.getJSONArray("results");
            JSONObject ar = newObj.getJSONObject(0);

            String firstName = ar.getString("first_name");
            String lastName = ar.getString("last_name");
            String tomID = ar.getString("thomas_id");
            String personDOB = ar.getString("date_of_birth");
            String personWebsite = ar.getString("url");
            int thomasID = Integer.parseInt(tomID);
            JSONArray obj = ar.getJSONArray("roles");
            JSONObject roles = obj.getJSONObject(0);
            String personParty = roles.getString("party");
            String personState = roles.getString("state");
            String houseOrSenate = roles.getString("chamber");
            String personMissedVotes = roles.getString("missed_votes_pct");
            String personVotesWithParty = roles.getString("votes_with_party_pct");
            String personBills = roles.getString("bills_sponsored");
            String personLastVote = ar.getString("most_recent_vote");

            String fullName = firstName + " " + lastName;
            eachPerson = new CongressMember(thomasID, tomID);
            eachPerson.setName(fullName);
            eachPerson.setHouseOrSenate(houseOrSenate);
            eachPerson.setDob(personDOB);
            eachPerson.setWebsite(personWebsite);
            eachPerson.setUniqueID(tomID);
            eachPerson.setParty(personParty);
            eachPerson.setState(personState);
            eachPerson.setMissedVotes(personMissedVotes);
            eachPerson.setVotesWithParty(personVotesWithParty);
            eachPerson.setBillsSponsored(personBills);
            eachPerson.setLastVote(personLastVote);
            return eachPerson;
        }

        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Bill> parseBill(String result){
        try {

            JSONObject jObj = new JSONObject(result);
            JSONArray newObj = jObj.getJSONArray("results");
            JSONObject ar = newObj.getJSONObject(0);
            JSONArray obj = ar.getJSONArray("bills");
            List<Bill> billList = new ArrayList<Bill>();

            for (int i = 0; i < obj.length(); i++) {
                JSONObject testObject = obj.getJSONObject(i);
                String billTitle = testObject.getString("title");
                String billNumber = testObject.getString("number");
                String billCommittee = testObject.getString("committees");
                String billUpdate = testObject.getString("latest_major_action_date");
                String billURI = testObject.getString("bill_uri");
                String billIntroduced = testObject.getString("introduced_date");
                Bill eachBill = new Bill();
                eachBill.setTitle(billTitle);
                eachBill.setNumber(billNumber);
                eachBill.setIntroduced(billIntroduced);
                eachBill.setBillURI(billURI);
                eachBill.setCommittee(billCommittee);
                eachBill.setLastUpdate(billUpdate);
                billList.add(eachBill);
            }
            return billList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


}
