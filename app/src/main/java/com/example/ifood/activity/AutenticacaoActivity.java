package com.example.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifood.R;
import com.example.ifood.helper.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class AutenticacaoActivity extends AppCompatActivity {
    private TextView email;
    private TextView senha;
    private Switch trocar;
    private Button acessar;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        getSupportActionBar().hide();

        email = findViewById(R.id.editCadastroEmail);
        senha = findViewById(R.id.editCadastroEmail);
        trocar = findViewById(R.id.switchAcesso);
        acessar = findViewById(R.id.buttonAcessar);

        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
       autenticacao.signOut();

        verificarUsuarioLogado();

        acessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoEmail = email.getText().toString();
                String textoSenha = senha.getText().toString();

                if (!textoEmail.isEmpty()){

                    if (!textoEmail.isEmpty()){

                        // verificar o estado do switch

                        if (trocar.isChecked()){

                            autenticacao.createUserWithEmailAndPassword(textoEmail, textoSenha)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AutenticacaoActivity.this, "cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                    }else{
                                        String erroExcecao = "";

                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException e){
                                            erroExcecao = "Digite uma senha mais forte";
                                        }catch (FirebaseAuthInvalidCredentialsException e){
                                            erroExcecao = "Digite  um email válido";
                                        }catch (FirebaseAuthUserCollisionException e){
                                            erroExcecao = "Email já cadastrado";
                                        }catch (Exception e) {
                                            erroExcecao = "Ao cadastrar o usuário: " +e.getMessage();
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(AutenticacaoActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT ).show();

                                    }

                                }
                            });


                        }else{
                            autenticacao.signInWithEmailAndPassword(textoEmail, textoSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                    } else {
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Erro ao fazer login : " + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            }





                    }else{
                        Toast.makeText(AutenticacaoActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(AutenticacaoActivity.this, "Preencha o E-mail", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void verificarUsuarioLogado(){
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual !=null);
    }
}