package com.example.lenovo_.hedeftantm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.lenovo_.hedeftantm.Helper.DBHelper;


public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                DBHelper dbHelper=new DBHelper(getApplicationContext());
                if (dbHelper.getUser().getKullaniciAdi().length()>0) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else startActivity(new Intent(getApplicationContext(), GirisActivity.class));
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
