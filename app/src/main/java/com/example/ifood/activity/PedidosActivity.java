package com.example.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;

import com.example.ifood.R;
import com.example.ifood.adapter.AdapterPedido;
import com.example.ifood.adapter.AdapterProduto;
import com.example.ifood.helper.ConfiguracaoFirebase;
import com.example.ifood.helper.UsuarioFirebase;
import com.example.ifood.model.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private AdapterPedido adapterPedido;
    private ArrayList<Pedido> pedidos = new ArrayList<>();
    private AlertDialog dialog;
    private DatabaseReference firebaseRef;
    private String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        //config iniciais
        inicialiazarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idEmpresa = UsuarioFirebase.getIdUsuario();

        // configurar toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);

        //cria o botao voltar na toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //configurar RecyclerView
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setHasFixedSize(true);
        adapterPedido = new AdapterPedido(pedidos);
        recyclerPedidos.setAdapter(adapterPedido);

        recuperarPedidos();

    }

    private void recuperarPedidos(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.themeIfood)
                .setMessage("Carregando dados")
                .setCancelable(false)
                .build();
        dialog.show();

        DatabaseReference pedidoRef = firebaseRef
                .child("pedido_confirmado")
                .child(idEmpresa);
        Query pedidoPesquisa = pedidoRef.orderByChild("status")
                .equalTo("confirmado");
        pedidoPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                pedidos.clear();
                if(snapshot.getValue() !=null){
                    for (DataSnapshot ds: snapshot.getChildren()){

                        Pedido pedido = ds.getValue(Pedido.class);
                        pedidos.add(pedido);

                    }
                    adapterPedido.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void inicialiazarComponentes(){

        recyclerPedidos = findViewById(R.id.recyclerPedidos);

    }

}