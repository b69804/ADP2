package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.Color;

import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.SENATE_PEOPLE;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.HOUSE_PEOPLE;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.HOUSE_NAMES;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.SENATE_NAMES;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.MAP_OF_HOUSE;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.MAP_OF_SENATE;
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
        house.setBackgroundColor(Color.parseColor("#96b3d8"));
        senate.setBackgroundColor(Color.parseColor("#d8bb96"));
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                house.setBackgroundColor(Color.parseColor("#2E67B2"));
                senate.setBackgroundColor(Color.parseColor("#d8bb96"));
                displayHouse();
            }
        });
        senate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                house.setBackgroundColor(Color.parseColor("#96b3d8"));
                senate.setBackgroundColor(Color.parseColor("#b2792e"));
                displaySenate();
            }
        });
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
        int houseCount = 0;
        CongressMember[] memberArray = new CongressMember[HOUSE_PEOPLE.size()];
        for(CongressMember congressMember : HOUSE_PEOPLE){
            memberArray[houseCount] = new CongressMember(congressMember.getName(), congressMember.getState());
            houseCount++;
        }
        CongressAdapter adapterItem = new CongressAdapter(getActivity(), R.layout.list_view_row_item, memberArray);
        mListView.setAdapter(adapterItem);
        senateOrHouse = true;
    }

    public void displaySenate(){
        int houseCount = 0;
        CongressMember[] memberArray = new CongressMember[SENATE_PEOPLE.size()];
        for(CongressMember congressMember : SENATE_PEOPLE){
            memberArray[houseCount] = new CongressMember(congressMember.getName(), congressMember.getState());
            houseCount++;
        }
        CongressAdapter adapterItem = new CongressAdapter(getActivity(), R.layout.list_view_row_item, memberArray);
        mListView.setAdapter(adapterItem);
        senateOrHouse = false;
    }

    public void getPeople(){
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
