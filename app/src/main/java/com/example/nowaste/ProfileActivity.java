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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private TextView mEmail, alimentoSprecato, stats;
    private BottomNavigationView bottomNavigationView;
    FirebaseUser user;
    private ImageButton settingsBtn, alertBtn, listsBtn;
    private DatabaseReference mDatabase;
    Map<String, Integer> statistiche;

    /**
     * Metodo onCreate chiamato all'avvio dell'activity.
     *
     * @param savedInstanceState Oggetto che contiene dati forniti in precedenza in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mEmail = findViewById(R.id.email);
        user = FirebaseAuth.getInstance().getCurrentUser();
        settingsBtn = findViewById(R.id.settings_icon);
        alertBtn = findViewById(R.id.alert_icon);
        listsBtn = findViewById(R.id.lists_icon);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        alimentoSprecato = findViewById(R.id.alimentoSprecato);
        stats = findViewById(R.id.stats);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // si apre la pagina degli alimenti in scadenza
                Intent intent = new Intent(getApplicationContext(), ListaAlimentiScadenza.class);
                startActivity(intent);
                finish();
            }
        });

        // setto il testo dell'alimento più sprecato
        // mi devo recuperare gli alimenti nella lista degli alimenti scaduti
        List<Alimenti> alimentiScaduti = new ArrayList<>();
        mDatabase.child("Alimenti").child(user.getUid());
        // TODO da decommentare ma la lista per ora è vuota quindi va in eccezione
        /*mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Alimenti a = snapshot.getValue(Alimenti.class);
                if(Integer.parseInt(a.listId) == 1) {
                    alimentiScaduti.add(a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // conto le occorrenze di ciascun alimento
        Map<Alimenti, Long> occorrenze = alimentiScaduti.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        // trovo l'alimento con più occorrenze
        Alimenti alSprecato = Collections.max(occorrenze.entrySet(), Map.Entry.comparingByValue()).getKey();
        alimentoSprecato.setText(alSprecato.nomeAlimento);
*/
        // popolo la mappa con i dati di Waste Watcher Italia
        /*statistiche.put("Frutta", 25);
        statistiche.put("Insalata", 18);
        statistiche.put("Cipolla", 17);
        statistiche.put("Patate", 17);
        statistiche.put("Pane", 16);
        statistiche.put("Verdure", 16);

        int stat = 0;
        for(Alimenti a : alimentiScaduti) {
            for(String key : statistiche.keySet()) {
                if (a.nomeAlimento.equals(key)) {
                    stat++;
                }
            }
        }
        // TODO ho messo un valore a caso per ora
        stats.setText(stat * 20);*/
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