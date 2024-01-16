package com.example.nowaste;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
        settingsBtn = findViewById(R.id.settings_icon);
        searchBtn = findViewById(R.id.search_icon);
        addListBtn = findViewById(R.id.addListBtn);

        if(user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

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

        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mostro un pop up per aggiungere il nome della lista
                showAddListDailog(); // dentro qui poi verrà chiamato il metodo per aggiungerla al db
                //TODO poi dovrà essere mostrata la lista nell'elenco
                //credo quando avrà successo quindi OnSuccess() (?)
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
        /*
        una lista avrà un nome e un id; poi avrà l'userid dell'utente che crea la lista
         */
        public String nomeLista, userId;

        /**
         * Costruttore vuoto.
         */
        public Liste() {
        }

        /**
         * Costruttore per la creazione di un oggetto di tipo Utente.
         * @param nomeLista Nome della lista dell'utente
         */
        public Liste(String nomeLista, String userId) {
            this.nomeLista = nomeLista;
            this.userId = userId;
        }
    }

    /**
     * Metodo che scrive una nuova lista all'interno del database Firebase.
     * @param userId ID univoco dell'utente
     * @param nomeLista Nome della lista
     */
    public void writeNewList(String userId, String nomeLista) {
        MainActivity.Liste listaAlimenti = new MainActivity.Liste(nomeLista, userId);

        mDatabase.child("Liste").child(userId).setValue(listaAlimenti);
    }

    /**
     * Metodo che mostra una finestra di dialogo per la creazione di una nuova lista di alimenti.
     */
    private void showAddListDailog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_new_list, null);
        final EditText nomeLista = view.findViewById(R.id.nomeListalog);
        Button confirm = view.findViewById(R.id.confirm_button);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = nomeLista.getText().toString().trim();
                if (TextUtils.isEmpty(listName)) {
                    Toast.makeText(MainActivity.this, "List name can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.dismiss();
                //creo la lista di alimenti
                writeNewList(user.getUid(), listName);
                Toast.makeText(MainActivity.this, "Lista creata correttamente", Toast.LENGTH_SHORT).show();
                // una volta che è stata creata dovrà essere visualizzata
            }
        });
    }

    /*
    dovrò avere un metodo che, quando faccio la onCreate della pagina MainActivity,
     recupera dal db le liste dell'utente e le dovrà mostrare.
     */
}