package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import static com.firstproject.androidstudio.matthewashton.represntr.Bill.HOUSE_BILLS;
import static com.firstproject.androidstudio.matthewashton.represntr.Bill.SENATE_BILLS;

public class CurrentBillsFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ProgressDialog prog;
    private int progressBarStatus = 0;
    Boolean houseOrSenate;
    Boolean yesOrNo;
    Button houseBills;
    Button senateBills;
    String billAPI;

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;

    public static CurrentBillsFragment newInstance(int sectionNumber) {
        CurrentBillsFragment fragment = new CurrentBillsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CurrentBillsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callHouseBills();
        prog = new ProgressDialog(getActivity());
        prog.setMessage("Getting the Most Recently Updated Bills in Congress...");
        prog.setIndeterminate(true);
        prog.show();
        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100){
                    try {
                        Thread.sleep(1000);
                        progressBarStatus += 20;

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    prog.dismiss();
                }
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currentbills, container, false);
        houseBills = (Button)view.findViewById(R.id.houseBillButton);
        houseBills.setBackgroundColor(Color.parseColor("#96b3d8"));
        houseBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                houseBills.setBackgroundColor(Color.parseColor("#2E67B2"));
                senateBills.setBackgroundColor(Color.parseColor("#d8bb96"));
                displayHouseBills();
                yesOrNo = true;
            }
        });
        senateBills = (Button)view.findViewById(R.id.senateBillButton);
        senateBills.setBackgroundColor(Color.parseColor("#d8bb96"));
        senateBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senateBills.setBackgroundColor(Color.parseColor("#b2792e"));
                houseBills.setBackgroundColor(Color.parseColor("#96b3d8"));
                displaySenateBills();
                yesOrNo = false;
            }
        });
        mListView = (AbsListView) view.findViewById(R.id.listOfBills);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (yesOrNo){
                    Bill selectedBill = HOUSE_BILLS.get(position);
                    billAPI = selectedBill.getBillURI();
                } else {
                    Bill selectedBill = SENATE_BILLS.get(position);
                    billAPI = selectedBill.getBillURI();
                }
                FragmentManager fMag = getFragmentManager();

                fMag.beginTransaction()
                        .add(R.id.container, BillDetail.newInstance(billAPI))
                        .addToBackStack("billStack")
                        .remove(CurrentBillsFragment.this)
                        .commit();
            }
        });
        return view;
    }

    public void callHouseBills(){
        houseOrSenate = true;
        requestData("http://api.nytimes.com/svc/politics/v3/us/legislative/congress/113/house/bills/introduced.json?api-key=6173918a265302ce206200f5d9d3b18e:4:69646428");
    }

    public void callSenateBills(){
        houseOrSenate = false;
        requestData("http://api.nytimes.com/svc/politics/v3/us/legislative/congress/113/senate/bills/introduced.json?api-key=6173918a265302ce206200f5d9d3b18e:4:69646428");
    }

    private void displaySenateBills() {
        if(SENATE_BILLS != null) {
            Bill[] billThing = new Bill[SENATE_BILLS.size()];
            int billCount = 0;
            for (Bill bill : SENATE_BILLS) {
                billThing[billCount] = new Bill(bill.getNumber(), bill.getTitle());
                billCount++;
                }
            ArrayAdapterItem adapterItem = new ArrayAdapterItem(getActivity(), R.layout.list_view_row_item, billThing);
            mListView.setAdapter(adapterItem);
        } else {
            showAlert();
        }
    }

    private void displayHouseBills() {
        if (HOUSE_BILLS != null){
            Bill[] billThing = new Bill[HOUSE_BILLS.size()];
            int billCount = 0;
            for (Bill bill : HOUSE_BILLS) {
                billThing[billCount] = new Bill(bill.getNumber(), bill.getTitle());
                billCount++;
                }
            ArrayAdapterItem adapterItem = new ArrayAdapterItem(getActivity(), R.layout.list_view_row_item, billThing);
            mListView.setAdapter(adapterItem);
        } else {
            showAlert();
        }
    }

    public void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Uh-Oh!");
        alertDialogBuilder
                .setMessage("Something Went Wrong with the API Call.")
                .setCancelable(false)
                .setPositiveButton("Please try again.",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
        }
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
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
            if (content == null){
                showAlert();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            if (houseOrSenate) {
                HOUSE_BILLS = JSONParser.parseBill(result);
                callSenateBills();
            } else {
                SENATE_BILLS = JSONParser.parseBill(result);
            }
        }
    }
}
