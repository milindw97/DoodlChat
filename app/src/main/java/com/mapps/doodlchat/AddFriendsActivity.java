package com.mapps.doodlchat;

import android.app.ProgressDialog;
import android.support.transition.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    Button searchUser,addUser;
    TextView test1,test2,test3,test4,test5;
    FirebaseAuth firebaseAuth;
    String id;
    DoodlUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        email = (EditText)findViewById(R.id.addByEmail);
        searchUser = (Button)findViewById(R.id.searchUser);
        addUser = (Button)findViewById(R.id.addUser);
        searchUser.setOnClickListener(this);
        addUser.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        test1 = (TextView)findViewById(R.id.test1);
        test2 = (TextView)findViewById(R.id.test2);
        test3 = (TextView)findViewById(R.id.test3);
        test4 = (TextView)findViewById(R.id.test4);
        test5 = (TextView)findViewById(R.id.test5);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.searchUser) {
            final ProgressDialog p = new ProgressDialog(this);
            p.setMessage("Searching User");
            p.show();

            final DatabaseReference[] ref = {FirebaseDatabase.getInstance().getReference("/users")};

            ref[0].addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        user = postSnapshot.getValue(DoodlUser.class);

                        if (email.getText().toString().equals(user.getemail())) {

                            test1.setText(user.getemail());
                            test3.setText(user.getname());
                            addUser.setVisibility(View.VISIBLE);
                        }
                    }
                    p.cancel();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        else if(v.getId() == R.id.addUser){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users");
            databaseReference.child(firebaseAuth.getCurrentUser().getDisplayName()).child("friends").child(user.getemail()).setValue("true");
        }
    }
}
