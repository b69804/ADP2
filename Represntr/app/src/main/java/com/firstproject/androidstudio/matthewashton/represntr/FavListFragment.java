package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.util.JsonReader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Type;

import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.SENATE_PEOPLE;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.HOUSE_PEOPLE;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.HOUSE_NAMES;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.SENATE_NAMES;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.MAP_OF_HOUSE;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.MAP_OF_SENATE;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.data;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.houseMap;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.favList;


public class FavListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    Button house;
    Button senate;
    String apiCall;
    Boolean senateOrHouse;

    public static FavListFragment newInstance(int sectionNumber) {
        FavListFragment fragment = new FavListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FavListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPeople();
        createHouseList();
        createSenateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favlist_list, container, false);
        mListView = (ListView) view.findViewById(R.id.congressList);
        house = (Button)view.findViewById(R.id.houseButton);
        senate = (Button)view.findViewById(R.id.senateButton);
        house.setFocusable(true);
        senate.setFocusable(false);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHouse();
                senate.setFocusable(false);

            }
        });
        senate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                house.setFocusable(false);
                displaySenate();

            }
        });
        displayHouse();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (senateOrHouse){
                    CongressMember favCongress = HOUSE_PEOPLE.get(position);
                    apiCall = favCongress.getApiCall();
                } else {
                    CongressMember favSenate = SENATE_PEOPLE.get(position);
                    apiCall = favSenate.getApiCall();
                }
                Intent intent3 = new Intent(getActivity(), PersonDetail.class);
                intent3.putExtra("apiCall", apiCall);
                startActivity(intent3);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ((RandomPerson) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            System.out.println("Clicked on the list.");
            CongressMember favCongress = HOUSE_PEOPLE.get(position);
            String apiCall = favCongress.getApiCall();
            Intent intent3 = new Intent(getActivity(), PersonDetail.class);
            intent3.putExtra("apiCall", apiCall);
            mListener.onFragmentInteraction(intent3);
            startActivity(intent3);
        }
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void createHouseList(){
        HOUSE_NAMES.clear();
        for (int i = 0; i < HOUSE_PEOPLE.size(); i++){
            CongressMember eachPerson = HOUSE_PEOPLE.get(i);
            String personID = eachPerson.getUniqueID();
            MAP_OF_HOUSE.put(personID, eachPerson);
            HOUSE_NAMES.add(eachPerson.getName());
        }
    }

    public void createSenateList(){
        SENATE_NAMES.clear();
        for (int i = 0; i < SENATE_PEOPLE.size(); i++){
            CongressMember eachPerson = SENATE_PEOPLE.get(i);
            String personID = eachPerson.getUniqueID();
            MAP_OF_SENATE.put(personID, eachPerson);
            SENATE_NAMES.add(eachPerson.getName());
        }
    }

    public void displayHouse(){
        data.clear();
        for(Map.Entry<String, CongressMember> entry : MAP_OF_HOUSE.entrySet()){
            CongressMember value = entry.getValue();
            houseMap.put("Name", value.getName());
            houseMap.put("State", value.getState());
        }
        data.add(houseMap);
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, HOUSE_NAMES);
        mListView.setAdapter(adapter);
        senateOrHouse = true;
    }

    public void displaySenate(){
        data.clear();
        for(Map.Entry<String, CongressMember> entry : MAP_OF_HOUSE.entrySet()){
            CongressMember value = entry.getValue();
            houseMap.put("Name", value.getName());
            houseMap.put("State", value.getState());
        }
        data.add(houseMap);
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, SENATE_NAMES);
        mListView.setAdapter(adapter);
        senateOrHouse = false;
    }

    public void getPeople(){
        //favList.clear();
        /*try {
            FileInputStream fis = getActivity().openFileInput("congressFile.txt");
            BufferedInputStream br = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (br.available() != 0) {
                char c = (char) br.read();
                b.append(c);
            }
            String testString = b.toString();
            if (testString.isEmpty()){
                System.out.println("No Person Added.");
            } else {
                System.out.println(testString);
                //Type type = new TypeToken<List<CongressMember>>(){}.getType();
                //CongressMember[] inpList = new Gson().fromJson(testString, CongressMember[].class);
                JsonParser parser = new JsonParser();
                JsonArray jArray = parser.parse(testString).getAsJsonArray();
                for(JsonElement obj : jArray ){
                    CongressMember inpList = new Gson().fromJson(obj, CongressMember.class);
                    if (inpList.getHouseOrSenate().equals("House")) {
                        HOUSE_PEOPLE.add(inpList);
                    } else if (inpList.getHouseOrSenate().equals("Senate")) {
                        SENATE_PEOPLE.add(inpList);
                    }
                }
                for (CongressMember congressMember : inpList) {
                    if (congressMember.getHouseOrSenate().equals("House")) {
                        HOUSE_PEOPLE.add(congressMember);
                    } else if (congressMember.getHouseOrSenate().equals("Senate")) {
                        SENATE_PEOPLE.add(congressMember);
                    }
                }
                String clear = "";
                FileOutputStream fos = getActivity().openFileOutput("congressFile.txt", Context.MODE_PRIVATE);
                fos.write(clear.getBytes());
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if (favList.isEmpty()){
            System.out.println("No Person Added.");
        } else {
            for (CongressMember congressMember : favList) {
                if (congressMember.getHouseOrSenate().equals("House")) {
                    HOUSE_PEOPLE.add(congressMember);
                } else if (congressMember.getHouseOrSenate().equals("Senate")) {
                    SENATE_PEOPLE.add(congressMember);
                }
            }
            favList.clear();
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Intent intent);

    }

}
