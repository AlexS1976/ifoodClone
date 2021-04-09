package com.example.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ifood.R;
import com.example.ifood.helper.ConfiguracaoFirebase;
import com.example.ifood.helper.UsuarioFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class AutenticacaoActivity extends AppCompatActivity {
    private EditText camporEmail;
    private EditText campoSenha;
    private Switch tipoAcesso, tipoUsuario;
    private Button botaoAcessar;
    private LinearLayout linearTipoUsuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);


        camporEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        tipoAcesso = findViewById(R.id.switchAcesso);
        botaoAcessar = findViewById(R.id.buttonAcessar);
        tipoUsuario = findViewById(R.id.switchEmpresa);
        linearTipoUsuario = findViewById(R.id.linearTipoUsuario);


        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut();

        verificarUsuarioLogado();

        tipoAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {//empresa
                    linearTipoUsuario.setVisibility(View.VISIBLE);


                } else { //usuario
                    linearTipoUsuario.setVisibility(View.GONE);

                }
            }
        });

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = camporEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()) {

                    if (!senha.isEmpty()) {


                        if (tipoAcesso.isChecked()) {//cadastro

                            autenticacao.createUserWithEmailAndPassword(email, senha)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(AutenticacaoActivity.this, "cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                                                String tipoUsuario = getTipoUsuario();
                                                UsuarioFirebase.AtualizarTipoUsuario(tipoUsuario);
                                                abrirTelaprincipal(tipoUsuario);
                                            } else {
                                                String erroExcecao = "";

                                                try {
                                                    throw task.getException();
                                                } catch (FirebaseAuthWeakPasswordException e) {
                                                    erroExcecao = "Digite uma senha mais forte!";
                                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                                    erroExcecao = "Por favor, digite um e-mail válido";
                                                } catch (FirebaseAuthUserCollisionException e) {
                                                    erroExcecao = "Esta conta já foi cadastrada";
                                                } catch (Exception e) {
                                                    erroExcecao = "ao cadastrar usuário: " + e.getMessage();
                                                    e.printStackTrace();
                                                }

                                                Toast.makeText(AutenticacaoActivity.this,
                                                        "Erro: " + erroExcecao,
                                                        Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });


                        } else {//Login

                            autenticacao.signInWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Logado com sucesso",
                                                Toast.LENGTH_SHORT).show();
                                        String tipoUsuario = task.getResult().getUser().getDisplayName();
                                        abrirTelaprincipal(tipoUsuario);

                                    } else {
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Erro ao fazer login : " + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }


                    } else {
                        Toast.makeText(AutenticacaoActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(AutenticacaoActivity.this, "Preencha o E-mail", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void verificarUsuarioLogado() {
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null) {
            Toast.makeText(this, "Usuário Logado", Toast.LENGTH_SHORT).show();
            String tipoUsuario = usuarioAtual.getDisplayName();
            abrirTelaprincipal(tipoUsuario);
        }

    }

    private String getTipoUsuario() {
        return tipoUsuario.isChecked() ? "E" : "U";
    }

    private void abrirTelaprincipal(String tipoUsuario) {
        if (tipoUsuario.equals("E")) {//empresa

            startActivity(new Intent(getApplicationContext(), EmpresaActivity.class));

        } else {//Usuario
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

        }

    }


}