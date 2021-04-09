package com.example.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.ifood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - Empresa");
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.sair :
                deslogarUsuario();
                finish();
                break;
            case R.id.config:
                abrirConfiguracoes();
                break;
            case R.id.ItemAdicionar:
                abrirNovoProduto();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario() {

        try {
            autenticacao.signOut();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirConfiguracoes(){
        Intent intent = new Intent(EmpresaActivity.this, ConfiguracoesEmpresaActivity.class);
        startActivity(intent);
    }

    public void abrirNovoProduto() {
        Intent intent = new Intent(EmpresaActivity.this, NovoProdutoEmpresaActivity.class);
        startActivity(intent);
    }
}