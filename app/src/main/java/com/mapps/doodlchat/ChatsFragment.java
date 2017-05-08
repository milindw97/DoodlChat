package com.mapps.doodlchat;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chats,container,false);

        String[] Names = {"PersonA","PersonB","PersonC","PersonD","PersonE","PersonF","PersonG","PersonH"};
        String[] Status = {"StatusA","StatusB","StatusC","StatusD","StatusE","StatusF","StatusG","StatusH"};

        ArrayList<TwoString> data = new ArrayList<>();

        int i = 0;
        while(i != Names.length) {
            TwoString values = new TwoString(Names[i], Status[i]);
            data.add(i,values);
            i++;
        }

        ListAdapter mAdapter = new CustomAdapter(getActivity(), data);
        final ListView mListView = (ListView)v.findViewById(R.id.allChats);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),ContactChatActivity.class);

                TwoString a = (TwoString) mListView.getItemAtPosition(position);
                i.putExtra("name",a.getName());
                i.putExtra("status",a.getStatus());
                startActivity(i);
            }
        });

        return v;
    }

}
