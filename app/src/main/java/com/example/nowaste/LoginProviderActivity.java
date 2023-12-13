package com.example.nowaste;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginProviderActivity extends AppCompatActivity {

    SignInButton googleSignInButton;
    Button facebookSignInButton;
    Button backToLoginButton;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mSignInClient;
    FirebaseAuth mAuth;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    CallbackManager callbackManager;

    //FirebaseDatabase database;
    //https://www.geeksforgeeks.org/google-signing-using-firebase-authentication-in-android-using-java/

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // utente già autenticato -> apro la pagina principale
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_provider);

        /*
        // per facebook login
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
*/
        //database = FirebaseDatabase.getInstance();

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestIdToken("625365294441-3oe0jpfr35i14pobsqhmovdvsm1heomu.apps.googleusercontent.com") // preso da google-services.json
                .requestEmail().build();

        mSignInClient = GoogleSignIn.getClient(this, options);
        mAuth = FirebaseAuth.getInstance();
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        //facebookSignInButton = findViewById(R.id.facebook_login_button);
        backToLoginButton = findViewById(R.id.login_btn);

        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: begin Google signin");
                Intent intent = mSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        /*facebookSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //callbackManager.onActivityResult(requestCode, resultCode, data); // facebook login
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google signin intent result");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            } catch (Exception e){
                Log.d(TAG, "onActivityResult: " + e.getMessage());
            }
            }
        }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "onSuccess: logged in");

                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                String email = user.getEmail();

                if(authResult.getAdditionalUserInfo().isNewUser()) {
                    Log.d(TAG, "onSuccess: Email: Account created: \" + email" + email);
                    Toast.makeText(LoginProviderActivity.this, "Account created: " + email, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onSuccess: existing user");
                    Toast.makeText(LoginProviderActivity.this, "existing account: " + email, Toast.LENGTH_SHORT).show();
                }

                startActivity(new Intent(LoginProviderActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: login failed " + e.getMessage());
            }
        });
    }
}