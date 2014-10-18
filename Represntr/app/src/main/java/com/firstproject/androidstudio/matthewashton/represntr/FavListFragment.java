package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import com.firstproject.androidstudio.matthewashton.represntr.dummy.DummyContent;
import static com.firstproject.androidstudio.matthewashton.represntr.CongressMember.CONGRESS_PEOPLE;

public class FavListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

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
        try {
            FileInputStream fis = getActivity().openFileInput("houseFile.txt");
            BufferedInputStream br = new BufferedInputStream(fis);
            StringBuffer b = new StringBuffer();
            while (br.available() != 0) {
                char c = (char) br.read();
                b.append(c);
            }
            String testString = b.toString();
            CongressMember inpList = new Gson().fromJson(testString, CongressMember.class);
            CONGRESS_PEOPLE.add(inpList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favlist_list, container, false);

        mListView = (ListView) view.findViewById(R.id.congressList);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
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
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
