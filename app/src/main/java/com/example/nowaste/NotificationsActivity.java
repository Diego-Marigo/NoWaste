package com.example.nowaste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * NotificationsActivity
 * Activity in cui si vedono tutte le notifiche delle scadenze
 *
 * @author martinaragusa
 * @since 1.0
 */
public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
    }
}