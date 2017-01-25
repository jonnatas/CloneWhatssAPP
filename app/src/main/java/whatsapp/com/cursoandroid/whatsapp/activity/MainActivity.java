package whatsapp.com.cursoandroid.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import whatsapp.com.cursoandroid.whatsapp.Adapter.TabAdapter;
import whatsapp.com.cursoandroid.whatsapp.R;
import whatsapp.com.cursoandroid.whatsapp.helper.Base64Custom;
import whatsapp.com.cursoandroid.whatsapp.helper.Preferencias;
import whatsapp.com.cursoandroid.whatsapp.helper.SlidingTabLayout;
import whatsapp.com.cursoandroid.whatsapp.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usuarioReference = databaseReference.child("usuario");

    private Toolbar toolbar_princiapl;
    private String identificadorContato;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        firebaseAuth = FirebaseAuth.getInstance();
        toolbar_princiapl = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar_princiapl.setTitle("WhatssApp");

        setSupportActionBar(toolbar_princiapl);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));


        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_sair:
                delogarUsuario();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.item_add_Person:
                abrirCadastroContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abrirCadastroContato() {
        AlertDialog.Builder alertDiBuilder = new AlertDialog.Builder(this);
        alertDiBuilder.setTitle("Novo Contato");
        alertDiBuilder.setMessage("E-mail do usuário");
        alertDiBuilder.setCancelable(false);

        final EditText editText = new EditText(this);
        alertDiBuilder.setView(editText);

        alertDiBuilder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emailContato = editText.getText().toString();

                if (emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this,"Preencha o e-mail", Toast.LENGTH_SHORT).show();
                } else {
                    identificadorContato = Base64Custom.converterBase64(emailContato);

                    usuarioReference.child(identificadorContato);
                    Log.i("USERR", "Usuario:::"+usuarioReference.child(identificadorContato).toString());

                    usuarioReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null ){

                                Usuario usuario = new Usuario();
                                usuario = dataSnapshot.getValue(Usuario.class);

                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                Log.i("IDENTIFICADOR", "IDENTIFICADOR Logado:: "+identificadorUsuarioLogado);
                            } else {
                                Toast.makeText(MainActivity.this, "Usuario não cadastrado" , Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        alertDiBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDiBuilder.create();
        alertDiBuilder.show();

    }

    private void delogarUsuario() {

        firebaseAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
