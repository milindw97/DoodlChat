package com.mapps.doodlchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class CustomAdapter extends ArrayAdapter<TwoString> {


    public CustomAdapter(Context context, List<TwoString> info) {
        super(context, R.layout.custom_row, info);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater minflater = LayoutInflater.from(getContext());
        View customview = minflater.inflate(R.layout.custom_row, parent, false);

        TwoString nameItem = getItem(position);
        TextView Name = (TextView)customview.findViewById(R.id.Name);
        TextView Status = (TextView)customview.findViewById(R.id.Status);
        ImageView userImage = (ImageView)customview.findViewById(R.id.userImage);

        Name.setText(nameItem.getName());
        Status.setText(nameItem.getStatus());

        return customview;
    }
}
