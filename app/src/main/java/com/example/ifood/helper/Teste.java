package com.example.ifood.helper;

import com.google.firebase.database.DatabaseReference;

public class Teste {

    public void teste(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        DatabaseReference ref = databaseReference.child("teste");
    }

}
