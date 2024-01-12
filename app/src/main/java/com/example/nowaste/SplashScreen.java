package com.example.nowaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * SplashScreen:
 * Activity per la schermata di caricamento iniziale in cui viene visualizzato il logo dell'app.
 *
 * @author martinaragusa
 * @since 1.0
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        /*
        handler per fare in modo che al lancio dell'applicazione
        si visualizzi il logo e, dopo due secondi, venga visualizzata la schermata di login
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}