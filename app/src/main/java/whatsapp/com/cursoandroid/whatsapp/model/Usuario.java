package whatsapp.com.cursoandroid.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import whatsapp.com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.com.cursoandroid.whatsapp.helper.Base64Custom;

/**
 * Created by jonnatas on 23/01/17.
 */

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private DatabaseReference databaseReference;

    public Usuario(){

    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void salvar() {
        databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("usuario").child(getId()).setValue(this);
    }
}
