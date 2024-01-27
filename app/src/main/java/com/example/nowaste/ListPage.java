package com.example.nowaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class ListPage extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference mDatabase;
    private BottomNavigationView bottomNavigationView;

    private ImageButton aggiungiAlimentoBtn;
    private String idLista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();

        // recupero l'id della lista in cui sono tramite l'Intent
        Intent intentOld = getIntent();
        idLista = intentOld.getStringExtra("idLista");

        aggiungiAlimentoBtn = findViewById(R.id.addAlimentoBtn);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.invisible);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.profile){
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        });

        aggiungiAlimentoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddAlimentoDailog();
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
        mDatabase.child("Alimenti").child(idLista).setValue(alimento).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ListPage.this, "Alimento aggiunto correttamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ListPage.this, "L'alimento non è stato aggiunto correttamente", Toast.LENGTH_SHORT).show();

            }
        });
    }
    /**
     * Metodo che mostra una finestra di dialogo per la creazione di una nuova lista di alimenti.
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
                        ListPage.this,
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
                    Toast.makeText(ListPage.this, "Il nome non può essere vuoto", Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.dismiss();
                //creo l'alimento
                writeNewAlimento(alimento, qnty, scadenza);
            }
        });
    }
}