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
import androidx.appcompat.widget.SearchView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity:
 * Activity che costituisce la pagina principale dell'applicazione.
 * Qui sono visualizzate le liste degli alimenti.
 *
 * @author martinaragusa
 * @since 2.0
 */
public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    /*
    schermata iniziale in cui si vedono le liste
     */
    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference mDatabase;
    private BottomNavigationView bottomNavigationView;
    private ImageButton settingsBtn,addListBtn;
    private SearchView searchView;
    private Button alimentiScaduti, alimentiInScadenza;
    RecyclerView listOfCustomLists;
    Adapter myAdapter;
    ArrayList<com.example.nowaste.Liste> list;
    ValueEventListener eventListener;
    String idLista;
    //lista di tutti gli alimenti
    ArrayList<AlimentoItem> listaAlimenti = new ArrayList<>();


    /**
     * Metodo onCreate chiamato all'avvio dell'activity.
     *
     * @param savedInstanceState Oggetto che contiene dati forniti in precedenza in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        user = auth.getCurrentUser();
        settingsBtn = findViewById(R.id.settings_icon);
        myAdapter = new Adapter(this, listaAlimenti);
/*

        mDatabase.child("Alimenti").child(user.getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AlimentoItem a = snapshot.getValue(AlimentoItem.class);
                listaAlimenti.add(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        // ricerca di un alimento
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });


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

        ListaAdapter adapter;
        listOfCustomLists = findViewById(R.id.listOfLists);
        listOfCustomLists.setHasFixedSize(true);
        listOfCustomLists.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<ListaItem> customLists = new ArrayList<>();
        adapter = new ListaAdapter(customLists, this);
        listOfCustomLists.setAdapter(adapter);

        DatabaseReference referenceListe = FirebaseDatabase.getInstance().getReference("Liste").child(user.getUid());
        eventListener = referenceListe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customLists.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    ListaItem dataClass = itemSnapshot.getValue(ListaItem.class);
                    //dataClass.setKey(itemSnapshot.getKey());
                    customLists.add(dataClass);
                }//
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Errore nel recupero dei dati", Toast.LENGTH_SHORT).show();
            }
        });

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
                idLista = String.valueOf(1); // imposto ad 1 l'id per la lista degli alimenti scaduti
                Intent intent = new Intent(getApplicationContext(), CustomListView.class);
                intent.putExtra("idLista", idLista);
                intent.putExtra("nomeLista", "Alimenti scaduti");
                startActivity(intent);
                finish();
            }
        });

        alimentiInScadenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // apre pagina dei cibi in scadenza
                idLista = String.valueOf(2); // imposto a 2 l'id per la lista degli alimenti in scadenza
                Intent intent = new Intent(getApplicationContext(), CustomListView.class);
                intent.putExtra("idLista", idLista);
                intent.putExtra("nomeLista", "Alimenti scaduti");
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Metodo per effettuare la ricerca. Permette di creare una lista in base all'input dell'utente.
     * @param text Input dell'utente.
     */
    private void filterList(String text) {
        //mi creo la lista che sarà da mostrare con i risultati della ricerca
        ArrayList<AlimentoItem> filteredList = new ArrayList<>();


        for(AlimentoItem a : listaAlimenti) {
            if(a.getNomeAlimento().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(a);
            }
        }
        if(filteredList.isEmpty()) {
            Toast.makeText(MainActivity.this, "Nessun risultato", Toast.LENGTH_SHORT).show();
        } else {
            //myAdapter.setFilteredList(filteredList);
            /* nell'adapter (degli alimenti - da creare) faccio un metodo
            public void setFilteredList(List<Alimenti> filteredList){
                this.itemList = filteredList;
                notifyDataSetChanged();
            }
             */

        }
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
        idLista = listId;
        mDatabase.child("Liste").child(userId).child(listId).setValue(listaAlimenti).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}