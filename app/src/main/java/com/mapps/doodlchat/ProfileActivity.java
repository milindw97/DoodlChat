package com.mapps.doodlchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 97 ;
    FirebaseAuth firebaseAuth;
    TextView nameView, statusView;
    ImageView dpView;
    EditText editName;
    Button updateName;
    Uri filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        dpView = (ImageView) findViewById(R.id.dpView);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Getting Info");
        dialog.show();

        firebaseAuth = FirebaseAuth.getInstance();

        Uri dp = firebaseAuth.getCurrentUser().getPhotoUrl();
        if (dp != null) {
            dpView.setImageURI(dp);
        }

        nameView = (TextView) findViewById(R.id.nameView);
        updateName = (Button) findViewById(R.id.updateName);
        nameView.setText(firebaseAuth.getCurrentUser().getDisplayName());


        editName = (EditText) findViewById(R.id.editName);
        statusView = (TextView) findViewById(R.id.statusView);

        final DatabaseReference[] ref = {FirebaseDatabase.getInstance().getReference("/users")};

        ref[0].addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    DoodlUser user = postSnapshot.getValue(DoodlUser.class);

                    Log.d("name",postSnapshot.getKey());
                    if(user.getemail().equals(firebaseAuth.getCurrentUser().getEmail())){
                        statusView.setText(user.getstatus());
                        dialog.cancel();
                        break;
                    }
                    else {
                        dialog.cancel();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                                            dpView = (ImageView)findViewById(R.id.dpView);
                                            Bitmap bm = null;
                                            try {
                                                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            dpView.setImageBitmap(bm);
                                            Toast.makeText(ProfileActivity.this,"Updated Image",Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            Toast.makeText(ProfileActivity.this,"Error Updating!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
            }

        }
    }

    public void editInfo(View view) {

        if(view.getId() == R.id.dpView){

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,"Choose an Image"),PICK_IMAGE_REQUEST);
        }
        else if (view.getId() == R.id.nameView) {
            nameView.setVisibility(view.GONE);
            editName.setVisibility(view.VISIBLE);
            updateName.setVisibility(view.VISIBLE);
        }
        else if (view.getId() == R.id.statusView) {

        }
        else if (view.getId() == R.id.updateName) {

            final String name = editName.getText().toString();

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if(user!=null) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        user.updateProfile(profileUpdate).
                                addOnCompleteListener(
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                Toast.makeText(ProfileActivity.this,"Updated Name",Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(ProfileActivity.this,"Error Updating!",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                );
                    }
            nameView.setText(name);
            nameView.setVisibility(view.VISIBLE);
            editName.setVisibility(view.GONE);
            updateName.setVisibility(view.GONE);
        }
    }
}
