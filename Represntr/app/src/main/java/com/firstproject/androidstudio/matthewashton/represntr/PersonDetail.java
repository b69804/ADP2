package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PersonDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        detailFrag dFrag = new detailFrag();
        Bundle bun = getIntent().getExtras();
        String passedAPI = bun.getString("apiCall");
        Bundle bundle = new Bundle();
        bundle.putString("apiCall", passedAPI);
        dFrag.setArguments(bundle);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, dFrag)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.person_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static class detailFrag extends Fragment {

        String apiPersonData;
        String apiCall;
        String memberID;
        CongressMember selectedCongressPerson;
        TextView name;
        TextView state;
        TextView houseOrSenate;
        TextView dob;
        TextView party;
        TextView billsSponsored;
        TextView votesWithParty;
        TextView missedVotes;
        TextView lastVote;
        Button website;
        Button bills;
        ListView theirBills;
        Boolean yesOrNo;
        List<Bill> listOfTheirBills = new ArrayList<Bill>();

        public detailFrag() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_person_detail, container, false);
            Bundle b = getArguments();
            String passAPI = b.getString("apiCall");
            apiCall = passAPI.concat("?api-key=6173918a265302ce206200f5d9d3b18e:4:69646428");
            yesOrNo = true;
            requestData(apiCall);
            name = (TextView)rootView.findViewById(R.id.individualName);
            state = (TextView)rootView.findViewById(R.id.individualState);
            dob = (TextView)rootView.findViewById(R.id.dob);
            party = (TextView)rootView.findViewById(R.id.individualParty);
            billsSponsored = (TextView)rootView.findViewById(R.id.billsSponsored);
            votesWithParty = (TextView)rootView.findViewById(R.id.votesWithParty);
            missedVotes = (TextView)rootView.findViewById(R.id.votesMissed);
            lastVote = (TextView)rootView.findViewById(R.id.lastVote);
            website = (Button)rootView.findViewById(R.id.website);
            website.setBackgroundColor(Color.parseColor("#d8bb96"));
            bills = (Button)rootView.findViewById(R.id.currentSponsoredBills);
            bills.setText("Show Bills they Sponsor");
            bills.setBackgroundColor(Color.parseColor("#96b3d8"));
            theirBills = (ListView)rootView.findViewById(R.id.listOfSponsoredBills);
            theirBills.setVisibility(View.INVISIBLE);
            bills.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    theirBills.setVisibility(View.VISIBLE);
                    displaySenateBills();
                }
            });
            houseOrSenate = (TextView)rootView.findViewById(R.id.individualCongressBranch);
            return rootView;
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        }

        public void showPerson(){
            name.setText(selectedCongressPerson.getName());
            state.setText(selectedCongressPerson.getState());
            dob.setText(selectedCongressPerson.getDob());
            houseOrSenate.setText(selectedCongressPerson.getHouseOrSenate());
            party.setText(selectedCongressPerson.getParty());
            billsSponsored.setText(selectedCongressPerson.getBillsSponsored());
            votesWithParty.setText(selectedCongressPerson.getVotesWithParty());
            missedVotes.setText(selectedCongressPerson.getMissedVotes());
            lastVote.setText(selectedCongressPerson.getLastVote());
            memberID = selectedCongressPerson.getMemberID();
            yesOrNo = false;
            requestData("http://api.nytimes.com/svc/politics/v3/us/legislative/congress/members/" + memberID + "/bills/updated.json?api-key=6173918a265302ce206200f5d9d3b18e:4:69646428");
            website.setText(selectedCongressPerson.getWebsite());

            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(getActivity(), PersonWebView.class);
                    intent2.putExtra("website", selectedCongressPerson.getWebsite());
                    startActivity(intent2);
                }
            });
        }

        private void displaySenateBills() {
            Bill[] billThing = new Bill[listOfTheirBills.size()];
            int billCount = 0;
            for (Bill bill : listOfTheirBills) {
                billThing[billCount] = new Bill(bill.getNumber(), bill.getTitle());
                billCount++;
            }
            ArrayAdapterItem adapterItem = new ArrayAdapterItem(getActivity(), R.layout.list_view_row_item, billThing);
            theirBills.setAdapter(adapterItem);
        }

        private void requestData(String uri){
            ApiTask task = new ApiTask();
            task.execute(uri);
        }

        private class ApiTask extends AsyncTask<String, String, String> {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(String... params){
                String content = HTTPManager.getData(params[0]);
                return content;
            }

            @Override
            protected void onPostExecute(String result) {
                if(yesOrNo){
                    apiPersonData = result;
                    selectedCongressPerson = JSONParser.parsePerson(result);
                    showPerson();
                } else {
                    listOfTheirBills = JSONParser.parseBill(result);
                }

            }
        }
    }
}
