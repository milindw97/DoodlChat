package com.mapps.doodlchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.data;

public class signup extends AppCompatActivity implements View.OnClickListener {

    EditText email,pass;
    Button register, fbregister;
    LoginButton loginButton;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Firebase.setAndroidContext(this);
        mCallbackManager = CallbackManager.Factory.create();

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.button);
        fbregister = (Button) findViewById(R.id.fbsignup);
        loginButton = (LoginButton) findViewById(R.id.hiddenlogin);

        pass.setTransformationMethod(new PasswordTransformationMethod());

        register.setOnClickListener(this);
        fbregister.setOnClickListener(this);

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View v) {

        if (v.getId() == R.id.button) {
            final String password = pass.getText().toString();
            final String emailid = email.getText().toString();

            if (password.isEmpty()) {
                pass.setError("Field is Required!");
                pass.requestFocus();
            } else if (emailid.isEmpty() || !emailid.contains("@")) {
                email.setError("Email Id Invalid!");
                email.requestFocus();
            } else {

                progressDialog.setMessage("Registering Your Account");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(emailid, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Intent i = new Intent(signup.this, DetailsActivity.class);
                                    i.putExtra("email", emailid);

                                    startActivity(i);

                                    Toast.makeText(signup.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(signup.this, "Registration Error! Try Again!", Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }

                            }
                        });


            }
        }

        else if(v.getId() == R.id.fbsignup) {
                loginButton.performClick();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(signup.this, "Welcome Back" + user.getDisplayName() + "   !", Toast.LENGTH_SHORT).show();
                            if (user.getDisplayName().equals("")) {
                                Intent i = new Intent(signup.this, DetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            } else {
                                Toast.makeText(signup.this, "Welcome to DoodlChat", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(signup.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                    }
                    }
                });
    }

}
