package com.mapps.doodlchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{

    EditText first,last,username;
    ImageView myDp;
    Button addDp, addDetails;
    int PICK_IMAGE_REQUEST = 97;
    String emailid;
    Uri filepath;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent j = getIntent();
        emailid = j.getStringExtra("email");

        first = (EditText) findViewById(R.id.firstname);
        last = (EditText) findViewById(R.id.lastname);
        username = (EditText) findViewById(R.id.username);
        addDp = (Button) findViewById(R.id.addDp);
        addDetails = (Button) findViewById(R.id.addDetails);

        addDp.setOnClickListener(this);
        addDetails.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null){
            filepath = data.getData();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user!=null) {
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(filepath)
                        .build();

                user.updateProfile(profileUpdate).
                        addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            myDp = (ImageView)findViewById(R.id.myDp);
                                            Bitmap bm = null;
                                            try {
                                                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            myDp.setImageBitmap(bm);
                                            Toast.makeText(DetailsActivity.this,"Updated Image",Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            Toast.makeText(DetailsActivity.this,"Error Updating!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
            }

        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.addDetails) {
            final String firstname = first.getText().toString();
            final String lastname = last.getText().toString();
            final String uname = username.getText().toString();

            if (firstname.isEmpty()) {
                first.setError("Field is Required!");
                first.requestFocus();
            } else if (uname.isEmpty()) {
                username.setError("Field is Required!");
                username.requestFocus();
            } else if (lastname.isEmpty()) {
                last.setError("Field is Required!");
                last.requestFocus();
            } else {

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();

                DoodlUser user = new DoodlUser(firstname+" "+lastname, firebaseAuth.getCurrentUser().getEmail(), uname);

                userRef.child("users").child(firstname+""+lastname).setValue(user);

                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(firstname+" "+lastname)
                        .build();

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                firebaseUser.updateProfile(profileUpdate);

                Intent i = new Intent(this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);;
                startActivity(i);
            }
        }

        else if(v.getId() == R.id.addDp){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,"Choose an Image"),PICK_IMAGE_REQUEST);
        }
    }
}
