package com.example.nowaste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginProviderActivity extends AppCompatActivity {

    SignInButton signInButton;
    Button signOutButton;
    TextView statusTextView;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mSignInClient;
    FirebaseAuth mAuth;
    //https://www.geeksforgeeks.org/google-signing-using-firebase-authentication-in-android-using-java/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_provider);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mSignInClient = GoogleSignIn.getClient(this, options);

        signInButton = findViewById(R.id.google_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /*private void signIn() {
        Intent intent = mSignInClient.getSignInIntent();
        startActivity(intent, RC_SIGN_IN);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful()) {
                GoogleSignInAccount acct = task.getResult();
            } else {
                // TODO accesso non andato a buon fine
            }
        }
    }
}