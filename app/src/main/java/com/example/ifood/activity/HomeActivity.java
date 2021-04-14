package com.example.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.ifood.R;
import com.example.ifood.adapter.AdapterEmpresa;
import com.example.ifood.adapter.AdapterProduto;
import com.example.ifood.helper.ConfiguracaoFirebase;
import com.example.ifood.listener.RecyclerItemClickListener;
import com.example.ifood.model.Empresa;
import com.example.ifood.model.Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;
    private RecyclerView recyclerEmpresa;
    private List<Empresa> empresas = new ArrayList<>();
    private DatabaseReference firebaseref;
    private AdapterEmpresa adapterEmpresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inicializarComponentes();
        firebaseref = ConfiguracaoFirebase.getFirebase();


        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood");
        setSupportActionBar(toolbar);

        //configura recyclerView
        recyclerEmpresa.setLayoutManager(new LinearLayoutManager(this));
        recyclerEmpresa.setHasFixedSize(true);
        adapterEmpresa = new AdapterEmpresa(empresas);
        recyclerEmpresa.setAdapter(adapterEmpresa);

        recuperarEmpresa();
        //config serchView
        searchView.setHint("Pesquisar restaurante");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarEmpresas(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        MenuItem iten = menu.findItem(R.id.menuPesquisar);
        searchView.setMenuItem(iten);

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
            case R.id.menuPesquisar:
                //abrirNovoProduto();
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
        Intent intent = new Intent(HomeActivity.this, ConfiguracoesUsuarioActivity.class);
        startActivity(intent);
    }

    public void inicializarComponentes(){

        searchView = findViewById(R.id.materialSearchView);
        recyclerEmpresa = findViewById(R.id.recyclerEmpresas);

    }

    public void pesquisarEmpresas(String pesquisa){

        DatabaseReference empresasRef = firebaseref
                .child("empresas");
        Query query = empresasRef.orderByChild("nomeEmpresa")
                .startAt(pesquisa)
                .endAt(pesquisa + "/ufoff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresas.clear();

                for (DataSnapshot ds: snapshot.getChildren()){
                    empresas.add( ds.getValue(Empresa.class));
                }
                adapterEmpresa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        clickarEmpresa();


    }





    public void recuperarEmpresa(){
        DatabaseReference produtosRef = firebaseref
                .child("empresas");


        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresas.clear();

                for (DataSnapshot ds: snapshot.getChildren()){
                    empresas.add( ds.getValue(Empresa.class));
                }
                adapterEmpresa.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        clickarEmpresa();

    }

    public void clickarEmpresa(){

        //configurar click
        recyclerEmpresa.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerEmpresa, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Empresa empresaSelecionada = empresas.get(position);
                Intent i = new Intent(HomeActivity.this, CardapioActivity.class);
                i.putExtra("empresa", empresaSelecionada);
                startActivity(i);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));


    }
}