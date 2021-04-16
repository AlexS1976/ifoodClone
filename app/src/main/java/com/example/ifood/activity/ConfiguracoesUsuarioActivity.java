package com.example.ifood.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ifood.R;
import com.example.ifood.helper.ConfiguracaoFirebase;
import com.example.ifood.helper.UsuarioFirebase;
import com.example.ifood.model.Empresa;
import com.example.ifood.model.Usuario;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private String idUsuarioLogado;
    private EditText nomeUsuario;
    private EditText enderecoUsuario;
    private EditText numeroEntrega;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configuraçoes Usuario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();
        recuperarDadosUsuario();

        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
        firebaseRef = ConfiguracaoFirebase.getFirebase();


    }

    public void validarDadosUsuario(View view){

        String nome = nomeUsuario.getText().toString();
        String endereco = enderecoUsuario.getText().toString();
        String numero = numeroEntrega.getText().toString();


        if(!nome.isEmpty()){
            if(!endereco.isEmpty()){
                if(!numero.isEmpty()){

                        Usuario usuario = new Usuario();
                        usuario.setIdUsuario(idUsuarioLogado);
                        usuario.setNome(nome);
                        usuario.setEndereco(endereco);
                        usuario.setNumero(numero);
                        usuario.salvar();
                    Toast.makeText(ConfiguracoesUsuarioActivity.this,
                            "Sucesso ao salvar configurações",
                            Toast.LENGTH_SHORT).show();
                        finish();

                }else{
                    exibirmensagem("Preencha numero");

                }

            }else{
                exibirmensagem("Preencha endereço");

            }

        }else{
            exibirmensagem("Preencha nome");

        }

    }

    public void inicializarComponentes(){

        nomeUsuario = findViewById(R.id.editTextNomeUsuario);
        enderecoUsuario = findViewById(R.id.editTextEnderecoUsuario);
        numeroEntrega = findViewById(R.id.editTextNumeroEntrega);

    }

    public void recuperarDadosUsuario(){

        DatabaseReference usuarioRef = firebaseRef
                .child("usuario")
                .child(idUsuarioLogado);
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    nomeUsuario.setText(usuario.getNome());
                    enderecoUsuario.setText(usuario.getEndereco());
                    numeroEntrega.setText(usuario.getNumero());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void exibirmensagem(String texto){
        Toast.makeText(this, texto,Toast.LENGTH_LONG).show();

    }
}