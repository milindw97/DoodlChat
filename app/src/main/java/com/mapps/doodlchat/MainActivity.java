package com.mapps.doodlchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            Intent i = new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

    }

    public void userinput(View v){
        if(v.getId()==R.id.signinbutton) {
            Intent i = new Intent(this, signin.class);
            startActivity(i);
        }
        if(v.getId()==R.id.signupbutton) {
            Intent j = new Intent(this, signup.class);
            startActivity(j);
        }
    }
}
