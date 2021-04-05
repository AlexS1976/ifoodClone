package com.example.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ifood.R;

public class SpalshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        //metodo para criar o splash
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirAutenticacao();

            }
        }, 3000);
    }

    private void abrirAutenticacao(){
        Intent intent = new Intent(SpalshActivity.this, AutenticacaoActivity.class);
        startActivity(intent);
        finish();
    }
}