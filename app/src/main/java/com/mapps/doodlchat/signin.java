package com.mapps.doodlchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class signin extends AppCompatActivity implements View.OnClickListener {

    EditText email, pass;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    LoginButton fblogin;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mCallbackManager = CallbackManager.Factory.create();
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            Intent i = new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        progressDialog = new ProgressDialog(this);

        email = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.password);
        pass.setTransformationMethod(new PasswordTransformationMethod());
        Button login = (Button) findViewById(R.id.sign_in_button);
        Button flogin = (Button) findViewById(R.id.fblogin);
        fblogin = (LoginButton) findViewById(R.id.loginbutton);

        login.setOnClickListener(this);
        flogin.setOnClickListener(this);

        fblogin.setReadPermissions("email", "public_profile");
        fblogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
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

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.sign_in_button) {
            final String emailid = email.getText().toString();
            final String password = pass.getText().toString();

            if (emailid.isEmpty()) {
                email.setError("Username Required!");
                email.requestFocus();
            } else if (password.isEmpty()) {
                pass.setError("Password Required!");
                pass.requestFocus();
            } else {

                progressDialog.setMessage("Signing In");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(emailid, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent i = new Intent(signin.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    Toast.makeText(signin.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(signin.this, "Login Failed. Try Again!", Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            }
                        });
            }
        }

        else if(view.getId()== R.id.fblogin) {
            fblogin.performClick();
        }
    }

    public void handleFacebookAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(signin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        else {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(signin.this,"Welcome Back" + user.getDisplayName() + "   !",Toast.LENGTH_SHORT).show();
                            if(user.getDisplayName().equals("")){
                                Intent i = new Intent(signin.this, DetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }

                            else {
                                Intent i = new Intent(signin.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        }
                    }
                });
    }
}