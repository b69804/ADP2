package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;

import android.app.ActionBar;
import android.app.IntentService;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;
import android.widget.Button;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.favList;

public class RandomPerson extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_person);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FavListFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CurrentBillsFragment.newInstance(position + 1))
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Feedback.newInstance(position + 1))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.random_person, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        String apiData;
        String fullAPIData;
        List<CongressMember> SenateList;
        List<CongressMember> cList = new ArrayList<CongressMember>() {};
        CongressMember testMember;
        TextView personName;
        TextView personState;
        TextView personHouseOrSenate;
        Button next;
        Button addToFavs;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_random_person, container, false);
            personName = (TextView)rootView.findViewById(R.id.personName);
            personState = (TextView)rootView.findViewById(R.id.personState);
            personHouseOrSenate = (TextView)rootView.findViewById(R.id.personCongress);
            addToFavs = (Button)rootView.findViewById(R.id.like);
            addToFavs.setVisibility(View.INVISIBLE);
            next = (Button)rootView.findViewById(R.id.dislike);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPerson();
                    display();
                    addToFavs.setVisibility(View.VISIBLE);
                }
            });
            addToFavs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        saveToCongressList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent1 = new Intent(getActivity(), PersonDetail.class);
                    intent1.putExtra("apiCall", testMember.getApiCall());
                    startActivity(intent1);
                }
            });

            return rootView;
        }

        private void saveToCongressList() throws IOException{
            favList.add(testMember);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((RandomPerson) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));

        }

        @Override
        public void onSaveInstanceState(Bundle outState){
            super.onSaveInstanceState(outState);
            outState.putBoolean("saved", true);
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            try {
                if(savedInstanceState !=null){
                System.out.println("Saved Instance State is not null.");
                } else {

                    readFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("File does not exist.");
                requestData("http://api.nytimes.com/svc/politics/v3/us/legislative/congress/113/senate/members/current.json?api-key=6173918a265302ce206200f5d9d3b18e:4:69646428");
                requestData("http://api.nytimes.com/svc/politics/v3/us/legislative/congress/113/house/members/current.json?api-key=6173918a265302ce206200f5d9d3b18e:4:69646428");
            }
        }

        public void createSenateFile() throws IOException{
            FileOutputStream fos = getActivity().openFileOutput("APISenateData.txt", Context.MODE_PRIVATE);
            fos.write(apiData.getBytes());
            fos.close();
        }

        public void createHouseFile() throws IOException{
            FileOutputStream fos = getActivity().openFileOutput("APIHouseData.txt", Context.MODE_PRIVATE);
            fos.write(fullAPIData.getBytes());
            fos.close();
        }

        public void readFile() throws IOException{

            FileInputStream fis = getActivity().openFileInput("APISenateData.txt");
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (bis.available() != 0) {
                char c = (char) bis.read();
                b.append(c);
            }
            apiData = b.toString();
            bis.close();
            fis.close();
            SenateList = JSONParser.parseFeed(apiData);
            cList.addAll(SenateList);

            fis = getActivity().openFileInput("APIHouseData.txt");
            bis = new BufferedInputStream(fis);
            b = new StringBuffer();
            while (bis.available() != 0) {
                char c = (char) bis.read();
                b.append(c);
            }
            fullAPIData = b.toString();
            bis.close();
            fis.close();
            SenateList = JSONParser.parseFeed(fullAPIData);
            cList.addAll(SenateList);
        }

        public void getPerson(){
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(540);

            testMember = cList.get(randomInt);
        }

        public void display(){

            personName.setText(testMember.getName());
            personState.setText(testMember.getState());
            personHouseOrSenate.setText(testMember.getHouseOrSenate());
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
                if (apiData == null) {
                    apiData = result;
                    try {
                        createSenateFile();
                        System.out.println("Created Senate File");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    fullAPIData = result;
                    try {
                        createHouseFile();
                        System.out.println("Created House File");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                SenateList = JSONParser.parseFeed(result);
                System.out.println(SenateList);
                cList.addAll(SenateList);
            }
        }
    }

}
