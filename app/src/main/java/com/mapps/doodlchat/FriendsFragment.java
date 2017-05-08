package com.mapps.doodlchat;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {

    public FriendsFragment() {
        // Required empty public constructor
    }

    FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends,container,false);

        final Button Settings = (Button)v.findViewById(R.id.settingsButton);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            getActivity().finish();
            startActivity(new Intent(getActivity(),signin.class));
        }

        String[] NamesA = {"PersonA","PersonB","PersonC","PersonD","PersonE","PersonF","PersonG","PersonH"};
        String[] Status = {"StatusA","StatusB","StatusC","StatusD","StatusE","StatusF","StatusG","StatusH"};

        ArrayList<TwoString> data = new ArrayList<>();

        int i = 0;
        while(i != NamesA.length) {

            TwoString values = new TwoString(NamesA[i], Status[i]);
            data.add(i,values);
            i++;
        }

        ListAdapter mAdapter = new CustomAdapter(getActivity(), data);
        final ListView mListView = (ListView) v.findViewById(R.id.allFriends);

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

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SettingsActivity.class));
            }
        });

        return v;
    }

}

