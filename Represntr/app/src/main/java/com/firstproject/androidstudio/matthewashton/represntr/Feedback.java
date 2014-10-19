package com.firstproject.androidstudio.matthewashton.represntr;



import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.net.Uri;


public class Feedback extends Fragment {

    Button feedback;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Feedback newInstance(int sectionNumber) {
        Feedback fragment = new Feedback();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public Feedback() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        feedback = (Button)view.findViewById(R.id.submitFeedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder sendIntent = ShareCompat.IntentBuilder.from(getActivity());
                sendIntent.setType("text/plain");
                sendIntent.addEmailTo("matthew.m.ashton@gmail.com");
                sendIntent.setSubject("Represntr Feedback");
                Intent sIntent = sendIntent.getIntent();
                startActivity(Intent.createChooser(sIntent, "Send Feedback..."));
            }
        });
        return view;
    }


}
