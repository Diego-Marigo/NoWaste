package com.example.nowaste;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowaste.databinding.ActivityCustomListViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Activity per la visualizzazione di una lista.
 * Questa classe contiene tutti i metodi che sono utilizzati all'interno della
 * pagina per la visualizzazione degli alimenti appartinenti alla lista.
 *
 * @author Diego M., martinaragusa
 * @since 2.0
 */
public class CustomListView extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private AppBarConfiguration appBarConfiguration;
    private ActivityCustomListViewBinding binding;
    private BottomNavigationView bottomNavigationView;

    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference mDatabase;
    private ImageButton aggiungiAlimentoBtn, settingsBtn, shareBtn;
    private String idLista;
    private TextView titolo; // nome lista da visualizzare nella pagina

    RecyclerView listOfFood;
    ValueEventListener eventListener;
    /**
     * Metodo onCreate chiamato all'avvio dell'activity.
     *
     * @param savedInstanceState Oggetto che contiene dati forniti in precedenza in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomListViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        bottomNavigationView = binding.bottomNavigationView;
        //nessuno dei due della barra sotto è selezionato
        bottomNavigationView.setSelectedItemId(R.id.invisible);

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_custom_list_view);
        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();

        // recupero l'id e il nome della lista in cui sono tramite l'Intent
        Intent intentOld = getIntent();
        idLista = intentOld.getStringExtra("idLista");

        titolo = findViewById(R.id.tv_list_name);
        titolo.setText(intentOld.getStringExtra("nomeLista"));

        aggiungiAlimentoBtn = findViewById(R.id.addAlimentoBtn);

        // TODO da decommentare (intanto lascio il pulsante per test)
        /*
        // rimuovo il pulsante per aggiungere un alimento se mi trovo in alimentiScaduti o inScadenza
        if(Integer.parseInt(idLista) == 1 || Integer.parseInt(idLista) == 2) {
            aggiungiAlimentoBtn.setVisibility(View.INVISIBLE);
        }
         */

        settingsBtn = findViewById(R.id.btn_more);
        shareBtn = findViewById(R.id.btn_share);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.profile){
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.home) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.setOnMenuItemClickListener(CustomListView.this);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });

        aggiungiAlimentoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddAlimentoDailog();
            }
        });

        // TODO vedere se modificare ciò che si condivide
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Ciao! " + user.getEmail() + " ti sta condividendo la sua lista.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        Adapter adapter;
        listOfFood = findViewById(R.id.recycler_view);
        listOfFood.setHasFixedSize(true);
        listOfFood.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Alimenti> customLists = new ArrayList<>();
        adapter = new Adapter(this, customLists);
        listOfFood.setAdapter(adapter);

        DatabaseReference referenceListe = FirebaseDatabase.getInstance().getReference("Liste").child(user.getUid());
        eventListener = referenceListe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customLists.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Alimenti dataClass = itemSnapshot.getValue(Alimenti.class);
                    //dataClass.setKey(itemSnapshot.getKey());
                    customLists.add(dataClass);
                }//
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomListView.this, "Errore nel recupero dei dati", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Metodo che scrive un nuovo alimento all'interno del database Firebase.
     * @param nomeAlimento Nome dell'alimento da aggiungere al db
     * @param quantity Quantità dell'alimento
     * @param dataScadenza Data di scadenza
     */
    public void writeNewAlimento(String nomeAlimento, int quantity, String dataScadenza) {
        Alimenti alimento = new Alimenti(nomeAlimento, quantity, dataScadenza, idLista);


        String idAlimento = mDatabase.push().getKey();
        mDatabase.child("Alimenti").child(user.getUid()).child(idAlimento).setValue(alimento).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CustomListView.this, "Alimento aggiunto correttamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomListView.this, "L'alimento non è stato aggiunto correttamente", Toast.LENGTH_SHORT).show();

            }
        });
    }
    /**
     * Metodo che mostra una finestra di dialogo per la creazione di un nuovo alimento.
     */
    private void showAddAlimentoDailog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_new_alimento, null);
        final EditText nomeAlimento = view.findViewById(R.id.nomeAlimentolog);
        ImageButton calendar = view.findViewById(R.id.calendarBtn);
        final TextView dataSelezionata = view.findViewById(R.id.dataSelezionata);
        Button confirm = view.findViewById(R.id.confirm_button);
        final EditText qnt = view.findViewById(R.id.qntInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CustomListView.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dataSelezionata.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alimento = nomeAlimento.getText().toString().trim();
                int qnty = Integer.parseInt(qnt.getText().toString());
                String scadenza = dataSelezionata.getText().toString();
                if (TextUtils.isEmpty(alimento)) {
                    Toast.makeText(CustomListView.this, "Il nome non può essere vuoto", Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.dismiss();
                //creo l'alimento
                writeNewAlimento(alimento, qnty, scadenza);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_custom_list_view);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
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