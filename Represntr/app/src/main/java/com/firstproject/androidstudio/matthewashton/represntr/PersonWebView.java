package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.net.URL;
import android.webkit.WebView;
import java.net.MalformedURLException;



public class PersonWebView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_web_view);
        PersonWebViewFrag webFrag = new PersonWebViewFrag();
        Bundle websiteBundle = getIntent().getExtras();
        String passedWebsite = websiteBundle.getString("website");
        Bundle fragBundle = new Bundle();
        fragBundle.putString("website", passedWebsite);
        webFrag.setArguments(fragBundle);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, webFrag)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.person_web_view, menu);
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

    public static class PersonWebViewFrag extends Fragment {

        WebView personWebView;

        public PersonWebViewFrag() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_person_web_view, container, false);
            personWebView = (WebView)rootView.findViewById(R.id.webView);
            Bundle b = getArguments();
            String passedWebsite = b.getString("website");
            try {
                URL url = new URL(passedWebsite);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            personWebView.loadUrl(passedWebsite);
            return rootView;
        }
    }
}
