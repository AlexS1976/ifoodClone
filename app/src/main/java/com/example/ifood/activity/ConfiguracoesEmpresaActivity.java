package com.example.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ifood.R;

public class ConfiguracoesEmpresaActivity extends AppCompatActivity {

    private EditText nomeEmpresa;
    private EditText tipoCozinha;
    private EditText tempoEntrega;
    private EditText taxaEntrega;
    private Button botaoSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_empresa);

        inicializarComponentes();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        //cria o botao voltar na toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void inicializarComponentes(){

      nomeEmpresa = findViewById(R.id.editTextNomeEmpresa);
      tipoCozinha = findViewById(R.id.editTextTipoEmpresa);
      tempoEntrega = findViewById(R.id.editTextTempoEmpresa);
      taxaEntrega = findViewById(R.id.editTextEntregaEmpresa);
      botaoSalvar = findViewById(R.id.buttonEmpresaSalvar);


    }
}