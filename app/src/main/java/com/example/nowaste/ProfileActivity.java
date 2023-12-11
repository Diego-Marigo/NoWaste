package com.example.nowaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    // pagina del profilo
    private TextView mUsername;
    private TextView mEmail;
    private ImageView profilePic;
    FirebaseUser user;
    private ImageButton settingsBtn, alertBtn, listsBtn;

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

        mUsername.setText(user.getUid()); // cambiare con username, bisogna salvarlo quando si fa l'accesso
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
    }

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