package com.example.nowaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * SettingsActivity:
 * Activity per le impostazioni: qui è possibile modificare le informazioni del proprio profilo
 * e la frequenza della ricezione delle notifiche riguardo la scadenza degli alimenti.
 *
 * @author martinaragusa
 * @since 1.0
 */
public class SettingsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String uid;
    TextView editUsername, editPassword;
    Button daysBeforeBtn;
    ProgressDialog pd;
    private BottomNavigationView bottomNavigationView;

    /**
     * Metodo chiamato all'avvio dell'activity.
     *
     * @param savedInstanceState Oggetto che contiene dati forniti in precedenza in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.fragment_settings);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        ^ nel caso volessi usare l'altra pagina delle impostazioni che ho in locale (se c'è tempo per implementarla bene)*/

        setContentView(R.layout.activity_settings);

        editUsername = findViewById(R.id.edit_username);
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        editPassword = findViewById(R.id.change_password);
        daysBeforeBtn = findViewById(R.id.days_beforeBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = firebaseDatabase.getReference("users");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //mettere che nessuno dei due sia selezionato
        bottomNavigationView.setSelectedItemId(R.id.invisible);

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

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Changing Password");
                showPasswordChangeDailog();
            }
        });

        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Updating Username");
                editUsername("username");
            }
        });

        daysBeforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.setOnMenuItemClickListener(SettingsActivity.this);
                popupMenu.getMenuInflater().inflate(R.menu.popup_notifications_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });
    }

    /**
     * Metodo che gestisce i click sugli elementi del menu.
     *
     * @param menuItem Elemento del menu selezionato
     * @return True se l'evento è stato gestito, False altrimenti
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        // TODO bisogna settare che in base a ciò che si sceglie si riceverà la notifica
        // per ogni alimento immagino ci sia un campo che definisce la cadenza con cui si riceve la notifica - sarà da modificare quello
        if(menuItem.getItemId() == R.id.item1){
            daysBeforeBtn.setText(menuItem.getTitle());
            return true;
        } else if(menuItem.getItemId() == R.id.item2){
            daysBeforeBtn.setText(menuItem.getTitle());
            return true;
        } else if(menuItem.getItemId() == R.id.item3){
            daysBeforeBtn.setText(menuItem.getTitle());
            return true;
        } else if(menuItem.getItemId() == R.id.item4){
            daysBeforeBtn.setText(menuItem.getTitle());
            return true;
        } else if(menuItem.getItemId() == R.id.item5){
            daysBeforeBtn.setText(menuItem.getTitle());
            return true;
        }
        return false;
    }

    /**
     * Metodo che mostra una finestra di dialogo per la modifica della password.
     */
    private void showPasswordChangeDailog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update_password, null);
        final EditText oldpass = view.findViewById(R.id.oldpasslog);
        final EditText newpass = view.findViewById(R.id.newpasslog);
        Button editpass = view.findViewById(R.id.updatepass);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldp = oldpass.getText().toString().trim();
                String newp = newpass.getText().toString().trim();
                if (TextUtils.isEmpty(oldp)) {
                    Toast.makeText(SettingsActivity.this, "Current Password can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(newp)) {
                    Toast.makeText(SettingsActivity.this, "New Password can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.dismiss();
                updatePassword(oldp, newp);
            }
        });
    }

    /**
     * Metodo per modificare la password salvata.
     * Controlla se la vecchia password è stata inserita correttamente e, in caso affermativo, la aggiorna con quella nuova.
     * @param oldp Vecchia password salvata
     * @param newp Nuova password
     */
    private void updatePassword(String oldp, final String newp) {
        pd.show();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldp);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(doHashing(newp))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // cambiare password nel db
                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put("password", doHashing(newp));

                                        databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();

                                                // field updated correctly
                                                Toast.makeText(SettingsActivity.this, "Password changed correctly", Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(SettingsActivity.this, "Unable to update", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(SettingsActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Metodo che aggiorna l'indirizzo email.
     * @param key
     */
    private void editUsername(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update " + key);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(this);
        editText.setHint("Enter " + key);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // field updated correctly
                            Toast.makeText(SettingsActivity.this, key + " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(SettingsActivity.this, "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (key.equals("email")) {
                        final DatabaseReference databaser = FirebaseDatabase.getInstance().getReference("Posts");
                        Query query = databaser.orderByChild("uid").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = databaser.getKey();
                                    dataSnapshot1.getRef().child("email").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(SettingsActivity.this, "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * Calcola l'hash MD5 della password per salvarla nel database.
     *
     * @param password password di cui fare l'hash
     * @return stringa rappresentante l'hash MD5 della password in input
     * @throws NoSuchAlgorithmException
     */
    public static String doHashing(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());

            byte[] result = messageDigest.digest();
            StringBuilder str = new StringBuilder();

            for(Byte b : result) {
                str.append(String.format("%02x", b));
            }

            return str.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
