package com.example.nowaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * ProfileActivity:
 * Activity per la pagina del profilo.
 * Questa classe contiene tutti i metodi che sono utilizzati all'interno della pagina del profilo.
 *
 * @author martinaragusa
 * @since 1.0
 */
public class ProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    /**
     * Elementi UI della pagina e user attualmente autenticato.
     */
    private TextView mUsername;
    private TextView mEmail;
    private ImageView profilePic;
    private BottomNavigationView bottomNavigationView;
    FirebaseUser user;
    private ImageButton settingsBtn, alertBtn, listsBtn;
    private DatabaseReference mDatabase;

    /**
     * Metodo onCreate chiamato all'avvio dell'activity.
     *
     * @param savedInstanceState Oggetto che contiene dati forniti in precedenza in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        profilePic = findViewById(R.id.profile_picture);
        user = FirebaseAuth.getInstance().getCurrentUser();
        settingsBtn = findViewById(R.id.settings_icon);
        alertBtn = findViewById(R.id.alert_icon);
        listsBtn = findViewById(R.id.lists_icon);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        /*mDatabase.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Failed.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    mUsername.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });*/
/*
        mUsername= mDatabase.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                }
                else {

                }
            }
        });*/
        mUsername.setText(user.getDisplayName()); // TODO cambiare con username, devo leggerlo dal db
        mEmail.setText(user.getEmail());

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.setOnMenuItemClickListener(ProfileActivity.this);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });

        listsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomListView.class);
                startActivity(intent);
                finish();
            }
        });

        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // si apre la pagina in cui si vedono tutte le notifiche delle scadenze
                Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Metodo che gestisce i click sugli elementi del menu.
     *
     * @param item Elemento del menu selezionato
     * @return True se l'evento è stato gestito, False altrimenti
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if(item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }
}