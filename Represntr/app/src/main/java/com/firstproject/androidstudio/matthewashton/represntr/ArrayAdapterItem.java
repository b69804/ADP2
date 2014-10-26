package com.firstproject.androidstudio.matthewashton.represntr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterItem extends ArrayAdapter<Bill> {

    Context mContext;
    int layoutResourceId;
    Bill[] data = null;

    public ArrayAdapterItem(Context mContext, int layoutResourceId, Bill[] data) {
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
            Bill aBill = data[position];
            TextView listItem1 = (TextView) convertView.findViewById(R.id.listItem1);
            TextView listItem2 = (TextView) convertView.findViewById(R.id.listItem2);

            listItem1.setText(aBill.getTitle());
            listItem2.setText(aBill.getNumber());

            return convertView;

    }
}

