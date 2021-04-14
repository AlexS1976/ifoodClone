package com.example.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ifood.R;
import com.example.ifood.adapter.AdapterProduto;
import com.example.ifood.helper.ConfiguracaoFirebase;
import com.example.ifood.model.Empresa;
import com.example.ifood.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardapioActivity extends AppCompatActivity {
    private RecyclerView recyclerProdutosCardapio;
    private ImageView imagemEmpresaCardapio;
    private TextView textNomeEmpresaCardapio;
    private Empresa empresaSelecionada;

    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        firebaseRef = ConfiguracaoFirebase.getFirebase();
        inicializarComponentes();

        //recuperar empresa selecionada
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            empresaSelecionada = (Empresa) bundle.getSerializable("empresa");
            textNomeEmpresaCardapio.setText(empresaSelecionada.getNomeEmpresa());
            //######arrumar idUsuario para idempresa no cadastro empresa#####
            idEmpresa = empresaSelecionada.getIdUsuario();
            String url = empresaSelecionada.getUrlImagem();
            Picasso.get().load(url).into(imagemEmpresaCardapio);

        }

        //configuracao toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cardápio");
        setSupportActionBar(toolbar);
        //cria o botao voltar na toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //configura recyclerView
        recyclerProdutosCardapio.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutosCardapio.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutosCardapio.setAdapter(adapterProduto);

        recuperarProdutos();




    }

    public void inicializarComponentes(){

        recyclerProdutosCardapio = findViewById(R.id.recyclerEmpresaCardapio);
        imagemEmpresaCardapio = findViewById(R.id.imageEmpresaCardapio);
        textNomeEmpresaCardapio = findViewById(R.id.textNomeEmpresaCardapio);
    }

    private void recuperarProdutos(){

        DatabaseReference produtosRef = firebaseRef
                .child("produtos")
                .child(idEmpresa);

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.clear();

                for (DataSnapshot ds: snapshot.getChildren()){
                    produtos.add( ds.getValue(Produto.class));
                }
                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}