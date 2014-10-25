package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BillDetail extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;
    String billURI;
    TextView billTitle;
    TextView billNum;
    TextView billUpdate;
    TextView billComm;
    TextView billSponsor;
    TextView billIntroDate;
    Bill selectedBill = new Bill();

    public static BillDetail newInstance(String param1) {
        BillDetail fragment = new BillDetail();
        Bundle args = new Bundle();
        args.putString("billURI", param1);
        fragment.setArguments(args);
        return fragment;
    }
    public BillDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            billURI = getArguments().getString("billURI").concat("?api-key=6173918a265302ce206200f5d9d3b18e:4:69646428");
            requestData(billURI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bill_detail, container, false);
        billTitle = (TextView)rootView.findViewById(R.id.selectedBillTitle);
        billNum = (TextView)rootView.findViewById(R.id.selectedBillNumber);
        billUpdate = (TextView)rootView.findViewById(R.id.selectedBillUpdate);
        billSponsor = (TextView)rootView.findViewById(R.id.SponsorsOfBill);
        billComm = (TextView)rootView.findViewById(R.id.BillCommittee);
        billIntroDate = (TextView)rootView.findViewById(R.id.BillOriginalIntroDate);

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public void displayBill(){
        billTitle.setText(selectedBill.getTitle());
        billNum.setText(selectedBill.getNumber());
        billUpdate.setText(selectedBill.getLastUpdate());
        billSponsor.setText(selectedBill.getBillSponsor());
        billComm.setText(selectedBill.getCommittee());
        billIntroDate.setText(selectedBill.getIntroduced());
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
            selectedBill = JSONParser.parseIndividualBill(result);
            displayBill();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
