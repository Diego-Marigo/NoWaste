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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();
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
     * @param userId ID univoco dell'utente
     * @param nomeLista Nome della lista
     */
 /*   public void writeNewList(String nomeAlimento, int quantity, Date dataScadenza, String listId) {
        Alimenti alimento = new Alimenti(nomeAlimento, quantity, dataScadenza, listId);


        String listId = mDatabase.push().getKey();
        mDatabase.child("Liste").child(listId).setValue(listaAlimenti).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ListPage.this, "Lista creata correttamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ListPage.this, "La lista non è stata creata correttamente", Toast.LENGTH_SHORT).show();

            }
        });
    }*/
    /**
     * Metodo che mostra una finestra di dialogo per la creazione di una nuova lista di alimenti.
     */
    private void showAddAlimentoDailog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_new_alimento, null);
        final EditText nomeLista = view.findViewById(R.id.nomeListalog); // TODO cambiare nome
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
        // TODO ancora da modificare
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = nomeLista.getText().toString().trim();
                if (TextUtils.isEmpty(listName)) {
                    Toast.makeText(ListPage.this, "List name can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.dismiss();
                //creo la lista di alimenti
                //writeNewList(user.getUid(), listName);
            }
        });
    }
}