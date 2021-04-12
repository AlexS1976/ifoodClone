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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ifood.R;
import com.example.ifood.helper.ConfiguracaoFirebase;
import com.example.ifood.helper.UsuarioFirebase;
import com.example.ifood.model.Empresa;
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

public class ConfiguracoesEmpresaActivity extends AppCompatActivity {

    private ImageView imagemPerfil;
    private EditText nomeEmpresa;
    private EditText tipoCozinha;
    private EditText tempoEntrega;
    private EditText taxaEntrega;
    private Button botaoSalvar;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private String idUsuarioLogado;
    private String urlImagemSelecionada="";
    private DatabaseReference firebaseref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_empresa);

        inicializarComponentes();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
        firebaseref = ConfiguracaoFirebase.getFirebase();


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        //cria o botao voltar na toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (i.resolveActivity(getPackageManager()) !=null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });


recuperarDadosEmpresa();

    }

    public void recuperarDadosEmpresa(){

        DatabaseReference empresaRef = firebaseref
                .child("empresas")
                .child(idUsuarioLogado);
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    Empresa empresa = snapshot.getValue(Empresa.class);
                    nomeEmpresa.setText(empresa.getNomeEmpresa());
                    tipoCozinha.setText(empresa.getTipoComida());
                    tempoEntrega.setText(empresa.getTempo());
                    taxaEntrega.setText(empresa.getPrecoEntrega().toString());

                    urlImagemSelecionada = empresa.getUrlImagem();
                   // Log.i("teste", String.valueOf(nomeEmpresa));

//
                    if (urlImagemSelecionada != null){
                    Picasso.get().load(urlImagemSelecionada).into(imagemPerfil);
                   }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    public void validarDadosEmpresa(View view){

        String nome = nomeEmpresa.getText().toString();
        String tipo = tipoCozinha.getText().toString();
        String tempo = tempoEntrega.getText().toString();
        String taxa= taxaEntrega.getText().toString();

        if(!nome.isEmpty()){
            if(!tipo.isEmpty()){
                if(!tempo.isEmpty()){
                    if(!taxa.isEmpty()){

                        Empresa empresa = new Empresa();
                        empresa.setIdUsuario(idUsuarioLogado);
                        empresa.setNomeEmpresa(nome);
                        empresa.setPrecoEntrega(Double.parseDouble(taxa));
                        empresa.setTempo(tempo);
                        empresa.setTipoComida(tipo);
                        empresa.setUrlImagem(urlImagemSelecionada);
                        empresa.salvar();
                        finish();

                    }else{
                        exibirmensagem("Preencha taxa de entrega");

                    }

                }else{
                    exibirmensagem("Preencha tempo de entrega");

                }

            }else{
                exibirmensagem("Preencha o tipo de cozinha");

            }

        }else{
            exibirmensagem("Preencha nome da empresa");

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_GALERIA:

                        Uri localImagem = data.getData();
                        try {
                            imagem = MediaStore.Images
                                    .Media
                                    .getBitmap(getContentResolver(), localImagem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                }

                if (imagem != null) {
                    imagemPerfil.setImageBitmap(imagem);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("empresas")
                            .child(idUsuarioLogado + "jpeg");
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return imagemRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                urlImagemSelecionada = downloadUrl.toString();
                                Toast.makeText(ConfiguracoesEmpresaActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ConfiguracoesEmpresaActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    /*uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesEmpresaActivity.this,
                                    "falha ao carregar imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                   urlImagemSelecionada = taskSnapshot.getStorage().toString();
                                    Toast.makeText(ConfiguracoesEmpresaActivity.this,
                                            "Sucesso ao carregar imagem",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });*/

                }


            }catch (Exception e){
                e.printStackTrace();
        }


            }
        }


    public void inicializarComponentes(){

        imagemPerfil = findViewById(R.id.imagePerfilEmpresa);
      nomeEmpresa = findViewById(R.id.editTextNomeProduto);
      tipoCozinha = findViewById(R.id.editTextDescricaoProduto);
      tempoEntrega = findViewById(R.id.editTextValorProduto);
      taxaEntrega = findViewById(R.id.editTextEntregaEmpresa);
      botaoSalvar = findViewById(R.id.buttonEmpresaSalvar);


    }

    public void exibirmensagem(String texto){
        Toast.makeText(this, texto,Toast.LENGTH_LONG).show();

    }
}