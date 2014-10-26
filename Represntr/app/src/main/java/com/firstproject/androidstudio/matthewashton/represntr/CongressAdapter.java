package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CongressAdapter extends ArrayAdapter<CongressMember> {
    Context mContext;
    int layoutResourceId;
    CongressMember[] data = null;

    public CongressAdapter(Context mContext, int layoutResourceId, CongressMember[] data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        CongressMember cMember = data[position];
        TextView listItem1 = (TextView) convertView.findViewById(R.id.listItem1);
        TextView listItem2 = (TextView) convertView.findViewById(R.id.listItem2);

        listItem1.setText(cMember.getName());
        System.out.println(cMember.getName());
        listItem2.setText(cMember.getState());
        System.out.println(cMember.getState());

        return convertView;

    }
}
