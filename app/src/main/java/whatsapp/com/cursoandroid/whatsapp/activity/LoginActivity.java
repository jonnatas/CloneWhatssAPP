package whatsapp.com.cursoandroid.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

import whatsapp.com.cursoandroid.whatsapp.Manifest;
import whatsapp.com.cursoandroid.whatsapp.R;
import whatsapp.com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.com.cursoandroid.whatsapp.helper.Base64Custom;
import whatsapp.com.cursoandroid.whatsapp.helper.Permissao;
import whatsapp.com.cursoandroid.whatsapp.helper.Preferencias;
import whatsapp.com.cursoandroid.whatsapp.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button logar;
    private Usuario usuario;
    private String idUsuarioLogado;
    private DatabaseReference firebase;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        email = (EditText) findViewById(R.id.editLoginEmail);
        senha = (EditText) findViewById(R.id.editLoginSenha);
        logar = (Button) findViewById(R.id.buttonLogar);

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    usuario = new Usuario();
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());

                    validarLogin();

                } catch (IllegalArgumentException e){
                    Toast.makeText(getApplicationContext(), "Invalido, preencha os campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void validarLogin() {
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            idUsuarioLogado = Base64Custom.converterBase64(usuario.getEmail());
                            firebase = ConfiguracaoFirebase.getFirebase()
                                    .child("usuario")
                                    .child(idUsuarioLogado);

                            firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Usuario usuario = dataSnapshot.getValue(Usuario.class);



                                    String identificadorUsuarioLogado = Base64Custom.converterBase64(usuario.getEmail());
                                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                                    preferencias.salvarDados(identificadorUsuarioLogado, usuario.getNome());

                                    abrirTelaPrincipal();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Error ao realizar o cadastro!!!!" + task.getException(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                });
    }
    private void verificarUsuarioLogado(){
        if (firebaseAuth.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }

    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }
}
