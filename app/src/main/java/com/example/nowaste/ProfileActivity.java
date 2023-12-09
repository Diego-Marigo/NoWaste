package com.example.nowaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    // pagina del profilo
    private TextView mUsername;
    private TextView mEmail;
    private ImageView profilePic;
    FirebaseUser user;
    private ImageButton settingsBtn;
    private ImageButton alertBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // nascondo la barra in alto
        getSupportActionBar().hide();

        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        profilePic = findViewById(R.id.profile_picture);
        user = FirebaseAuth.getInstance().getCurrentUser();
        settingsBtn = findViewById(R.id.settings_icon);
        alertBtn = findViewById(R.id.alert_icon);

        mUsername.setText(user.getUid()); // cambiare con username, bisogna salvarlo quando si fa l'accesso
        mEmail.setText(user.getEmail());

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.setOnMenuItemClickListener(ProfileActivity.this);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                //popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
       /* switch (item.getItemId()) {
            case R.id.edit_profile:
                //TODO open edit profile page
                return true;
            case R.id.settings:
                //TODO open notification settings
                return true;
            case R.id.logout:
                //TODO back to login page

                return true;
            default:
                return false;
        }*/
        return true;
    }
}