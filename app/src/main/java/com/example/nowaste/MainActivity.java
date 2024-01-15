package com.example.nowaste;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * MainActivity:
 * Activity che costituisce la pagina principale dell'applicazione.
 * Qui sono visualizzate le liste degli alimenti.
 */
public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    /*
    schermata iniziale in cui si vedono le liste

    -> bisognerà creare tabella delle liste e tabella degli alimenti all'interno del db
     */
    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference mDatabase;
    TextView textView;
    Button logout;
    private BottomNavigationView bottomNavigationView;
    private ImageButton settingsBtn, searchBtn, addListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        user = auth.getCurrentUser();
        //logout = findViewById(R.id.logoutBtn);
        //textView = findViewById(R.id.user_details);
        settingsBtn = findViewById(R.id.settings_icon);
        searchBtn = findViewById(R.id.search_icon);
        addListBtn = findViewById(R.id.addListBtn);

        if(user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } /*else {
            textView.setText(user.getEmail());
        }*/

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.profile){
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        });
/*
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO implementare la ricerca
            }
        });
*/
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.show();
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


    /**
     * Classe che rappresenta le Liste all'interno del database.
     */
    @IgnoreExtraProperties
    public class Liste {
        //TODO cambiare gli attributi
        /*
        una lista avrà un nome e un id; poi avrà l'userid dell'utente che crea la lista
         */
        public String nomeLista;

        /**
         * Costruttore vuoto.
         */
        public Liste() {
        }

        /**
         * Costruttore per la creazione di un oggetto di tipo Utente.
         * @param nomeLista Nome della lista dell'utente
         */
        public Liste(String nomeLista) {
            this.nomeLista = nomeLista;
        }
    }

    /**
     * Metodo che scrive un nuovo utente all'interno del database Firebase.
     * @param userId ID univoco dell'utente
     * @param nomeLista Nome della lista
     */
    // TODO questo succederà quando l'utente schiaccerà il pulsante + per creare una lista
    public void writeNewList(String userId, String nomeLista) {
        MainActivity.Liste listaAlimenti = new MainActivity.Liste(nomeLista);

        mDatabase.child("Liste").child(userId).setValue(user);
    }
}