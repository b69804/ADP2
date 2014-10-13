package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class RandomPerson extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_person);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
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
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.random_person, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        String apiData;
        List<CongressMember> SenateList;
        List<CongressMember> HouseList;
        List<CongressMember> cList = new ArrayList<CongressMember>() {};
        CongressMember testMember;
        TextView personName;
        TextView personState;
        TextView personHouseOrSenate;
        Button next;
        Button addToFavs;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((RandomPerson) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            requestData("http://api.nytimes.com/svc/politics/v3/us/legislative/congress/113/senate/members/current.json?api-key=6173918a265302ce206200f5d9d3b18e:4:69646428");
            requestData("http://api.nytimes.com/svc/politics/v3/us/legislative/congress/113/house/members/current.json?api-key=6173918a265302ce206200f5d9d3b18e:4:69646428");
        }

        public void createFile() throws IOException{
            FileOutputStream fos = getActivity().openFileOutput("congressData.txt", Context.MODE_PRIVATE);
            fos.write(apiData.getBytes());
            fos.close();
        }

        public void readFile() throws IOException{
            FileInputStream fis = getActivity().openFileInput("congressData.txt");
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (bis.available() != 0){
                char c = (char) bis.read();
                b.append(c);
            }
            apiData = b.toString();
            bis.close();
            fis.close();
        }

        public void getPerson(){
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(540);
            System.out.println(randomInt);
            testMember = cList.get(randomInt);
        }

        public void display(){
            System.out.println(cList.size());
            personName.setText(testMember.getName());
            personState.setText(testMember.getState());
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
                apiData = result;
                try {
                    createFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SenateList = JSONParser.parseFeed(result);
                System.out.println(SenateList);
                cList.addAll(SenateList);

            }
        }
    }
}
