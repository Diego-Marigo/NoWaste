package com.example.nowaste;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * RegistrationActivity:
 * Activity che si occupa della registrazione di un nuovo utente.
 *
 * @author martinaragusa
 * @since 1.0
 */
public class RegistrationActivity extends AppCompatActivity {

    /**
     * Elementi UI della pagina.
     */
    EditText mUsername;
    EditText mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    /**
     * Metodo chiamato all'avvio dell'applicazione.
     * Controlla se l'utente è già autenticato e, in caso affermativo, apre la pagina principale.
     */
    @Override
    public void onStart() {
        super.onStart();
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
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUsername = findViewById(R.id.emailAddress);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String username = mUsername.getText().toString();

                /*
                l'autenticazione di firebase funziona solo con email e non username
                non so se ha senso tenere che si inserisca anche lo username
                 */
                if(TextUtils.isEmpty(username)) {
                    mUsername.setError("Username is required");
                    return;
                }
                if(TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    // TODO cambiare userId, per ora gli passo username
                                    writeNewUser(username, username, email);
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    /**
     * Classe che rappresenta un utente all'interno del database.
     */
    @IgnoreExtraProperties
    public class User {
        public String username, email;

        /**
         * Costruttore vuoto.
         */
        public User() {
        }

        /**
         * Costruttore per la creazione di un oggetto di tipo Utente.
         * @param username Username dell'utente
         * @param email Indirizzo email dell'utente
         */
        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }

    /**
     * Metodo che scrive un nuovo utente all'interno del database Firebase.
     * @param userId ID univoco dell'utente
     * @param username Username dell'utente
     * @param email Indirizzo email dell'utente
     */
    public void writeNewUser(String userId, String username, String email) {
        User user = new User(username, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
}