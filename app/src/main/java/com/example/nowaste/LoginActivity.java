package com.example.nowaste;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * LoginActivity:
 * Activity per l'accesso di un utente già registrato.
 *
 * @author martinaragusa
 * @since 1.0
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Elementi UI della pagina
     */
    EditText mEmail;
    EditText mPassword;
    Button mLoginBtn;
    Button mRegisterBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    /**
     * Elementi per GoogleSignIn
     */
    SignInButton googleSignInButton;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mSignInClient;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    /**
     * Metodo chiamato all'avvio dell'applicazione.
     * Controlla se l'utente è già autenticato e, in caso affermativo, apre la pagina principale.
     */
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

    /**
     * Metodo chiamato all'avvio dell'activity.
     *
     * @param savedInstanceState Oggetto che contiene dati forniti in precedenza in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail = findViewById(R.id.emailAddress);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);
        mRegisterBtn = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progressBar);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestIdToken("625365294441-3oe0jpfr35i14pobsqhmovdvsm1heomu.apps.googleusercontent.com") // preso da google-services.json
                .requestEmail().build();

        mSignInClient = GoogleSignIn.getClient(this, options);
        mAuth = FirebaseAuth.getInstance();
        googleSignInButton = findViewById(R.id.google_sign_in_button);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: begin Google signin");
                Intent intent = mSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    //login andato a buon fine e posso andare nella pagina principale
                                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    /**
     *
     * @param requestCode Codice della richiesta
     * @param resultCode Codice del risultato
     * @param data Intent contenente i dati restituiti
     */
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

    /**
     * Metodo per l'autenticazione tramite un account Google.
     * @param account Account Google ottenuto dall'intent di accesso con Google
     */
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
                    Toast.makeText(LoginActivity.this, "Account created: " + email, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onSuccess: existing user");
                    Toast.makeText(LoginActivity.this, "existing account: " + email, Toast.LENGTH_SHORT).show();
                }

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
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