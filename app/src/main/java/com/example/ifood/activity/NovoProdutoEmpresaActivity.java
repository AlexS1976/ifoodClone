package com.example.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ifood.R;
import com.example.ifood.helper.UsuarioFirebase;
import com.example.ifood.model.Empresa;
import com.example.ifood.model.Produto;
import com.google.firebase.auth.FirebaseAuth;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private EditText nomeProduto;
    private EditText descriscaoProduto;
    private EditText valorProduto;
    private String idUsuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo produto");
        setSupportActionBar(toolbar);
        //cria o botao voltar na toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();


    }

    public void validarDadosProduto(View view){

        String nome = nomeProduto.getText().toString();
        String descricao = descriscaoProduto.getText().toString();
        String valor = valorProduto.getText().toString();


        if(!nome.isEmpty()){
            if(!descricao.isEmpty()){
                if(!valor.isEmpty()){

                        Produto produto = new Produto();
                        produto.setIdUsuario(idUsuarioLogado);
                        produto.setNome(nome);
                        produto.setDescicao(descricao);
                        produto.setValor(Double.parseDouble(valor));

                        produto.salvar();
                        finish();
                        exibirmensagem("produto salvo com sucesso");

                }else{
                    exibirmensagem("Preencha valor do produto");

                }

            }else{
                exibirmensagem("Preencha descrição do produto");

            }

        }else{
            exibirmensagem("Preencha nome do produto");

        }

    }

    public void exibirmensagem(String texto){
        Toast.makeText(this, texto,Toast.LENGTH_LONG).show();

    }
    public void inicializarComponentes(){

        nomeProduto = findViewById(R.id.editTextNomeProduto);
        descriscaoProduto = findViewById(R.id.editTextDescricaoProduto);
        valorProduto = findViewById(R.id.editTextValorProduto);

    }
}