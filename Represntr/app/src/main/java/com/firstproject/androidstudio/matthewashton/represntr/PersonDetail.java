package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class detailFrag extends Fragment {

        String apiPersonData;
        String apiCall;
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

        public detailFrag() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_person_detail, container, false);
            Bundle b = getArguments();
            String passAPI = b.getString("apiCall");
            apiCall = passAPI.concat("?api-key=6173918a265302ce206200f5d9d3b18e:4:69646428");

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
                apiPersonData = result;
                selectedCongressPerson = JSONParser.parsePerson(result);
                showPerson();
            }
        }
    }
}
