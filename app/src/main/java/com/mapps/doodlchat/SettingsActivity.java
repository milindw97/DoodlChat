package com.mapps.doodlchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Button Logout,Profile,About,AddFriends;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this,signin.class));
        }

        Logout = (Button)findViewById(R.id.logoutButton);
        Profile = (Button)findViewById(R.id.profileButton);
        AddFriends = (Button)findViewById(R.id.addFriendsButton);
        About = (Button)findViewById(R.id.aboutButton);

        Logout.setOnClickListener(this);
        Profile.setOnClickListener(this);
        AddFriends.setOnClickListener(this);
        About.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.logoutButton){

            LoginManager.getInstance().logOut();
            firebaseAuth.signOut();
            finish();
            Intent i = new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        else if(v.getId() == R.id.addFriendsButton){
            startActivity(new Intent(this, AddFriendsActivity.class));
        }

        else if(v.getId() == R.id.profileButton){
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }
}
