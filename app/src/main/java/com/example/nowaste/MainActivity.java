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
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MainActivity:
 * Activity che costituisce la pagina principale dell'applicazione.
 * Qui sono visualizzate le liste degli alimenti.
 */
public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    /*
    schermata iniziale in cui si vedono le liste
     */
    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference mDatabase;
    private BottomNavigationView bottomNavigationView;
    private ImageButton settingsBtn, searchBtn, addListBtn;
    private Button alimentiScaduti, alimentiInScadenza;
    RecyclerView recyclerView;
    Adapter myAdapter;
    ArrayList<com.example.nowaste.Liste> list;

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
        alimentiScaduti = findViewById(R.id.btn_cibi_scaduti);
        alimentiInScadenza = findViewById(R.id.btn_alimenti_in_scadenza);

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
        recyclerView = findViewById(R.id.listOfLists);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        list = new ArrayList<>();
        myAdapter = new Adapter(this, list);
        recyclerView.setAdapter(myAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    com.example.nowaste.Liste lista = dataSnapshot.getValue(com.example.nowaste.Liste.class);
                    list.add(lista);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/



        // mostro le liste, se l'utente ne ha
        //showLists();
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
            }
        });

        alimentiScaduti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // apre pagina dei cibi scaduti
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alimentiInScadenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // apre pagina dei cibi in scadenza
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
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



    /**
     * Metodo che scrive una nuova lista all'interno del database Firebase.
     * @param userId ID univoco dell'utente
     * @param nomeLista Nome della lista
     */
    public void writeNewList(String userId, String nomeLista) {
        com.example.nowaste.Liste listaAlimenti = new com.example.nowaste.Liste(nomeLista, userId);

        String listId = mDatabase.push().getKey();
        mDatabase.child("Liste").child(listId).setValue(listaAlimenti).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "Lista creata correttamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "La lista non è stata creata correttamente", Toast.LENGTH_SHORT).show();

            }
        });
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
            }
        });
    }

    /*
    dovrò avere un metodo che, quando faccio la onCreate della pagina MainActivity,
     recupera dal db le liste dell'utente e le dovrà mostrare.

     avere una lista in cui vengono memorizzate le liste?

    private void showLists() {

        getData();

    }

    private void getData(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                textView.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}