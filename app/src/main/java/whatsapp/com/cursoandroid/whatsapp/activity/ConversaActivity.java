package whatsapp.com.cursoandroid.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsapp.com.cursoandroid.whatsapp.Adapter.MensagemAdapter;
import whatsapp.com.cursoandroid.whatsapp.R;
import whatsapp.com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.com.cursoandroid.whatsapp.helper.Base64Custom;
import whatsapp.com.cursoandroid.whatsapp.helper.Preferencias;
import whatsapp.com.cursoandroid.whatsapp.model.Conversa;
import whatsapp.com.cursoandroid.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton btMensagem;
    private EditText editMensagem;
    private ListView listView;

    private ArrayAdapter<Mensagem> mensagemAdapter;
    private ArrayList<Mensagem> mensagens;
    private ValueEventListener valueEventListenerMensagens;

    private Bundle extra;
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;
    private String idUsuarioLogado;

    private Conversa conversa;
    private String nomeUsuarioLogado;

    private DatabaseReference databaseReference;
    private DatabaseReference mensagemDatabaseReference;
    private DatabaseReference conversaDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        databaseReference = ConfiguracaoFirebase.getFirebase();
        mensagemDatabaseReference = databaseReference.child("mensagem");

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioLogado = preferencias.getIdentificador();
        nomeUsuarioLogado = preferencias.getNome();

        btMensagem = (ImageButton) findViewById(R.id.bt_enviar);
        editMensagem = (EditText) findViewById(R.id.edit_mensagem);

        listView = (ListView) findViewById(R.id.lv_mensagens);


        extra = getIntent().getExtras();

        if (!extra.isEmpty()){
            nomeUsuarioDestinatario = extra.getString("nome");
            idUsuarioDestinatario = Base64Custom.converterBase64(extra.getString("email"));
        }

        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        mensagens = new ArrayList<>();

        mensagemAdapter = new MensagemAdapter(ConversaActivity.this, mensagens);

        listView.setAdapter(mensagemAdapter);

        mensagemDatabaseReference = mensagemDatabaseReference
                .child(idUsuarioLogado)
                .child(idUsuarioDestinatario);

        valueEventListenerMensagens = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensagens.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Mensagem mensagem = data.getValue(Mensagem.class);

                    mensagens.add(mensagem);
                }

                mensagemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mensagemDatabaseReference.addValueEventListener(valueEventListenerMensagens);


        btMensagem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String textoMensagem = editMensagem.getText().toString();

            if (textoMensagem.isEmpty()){
                Toast.makeText(ConversaActivity.this, "Digite a mensagem para enviar", Toast.LENGTH_LONG).show();
            } else {
                Mensagem mensagem = new Mensagem();
                mensagem.setIdUsuario(idUsuarioLogado);
                mensagem.setMensagem(textoMensagem);

                salvarMensagem(idUsuarioLogado, idUsuarioDestinatario, mensagem);
                editMensagem.setText("");
            }
        }
    });
    }

    private void salvarMensagem(String idUsuarioLogado, String idUsuarioDestinatario, Mensagem mensagem) {

        boolean retornoRemetente = salvarMensagemFirebase(idUsuarioLogado, idUsuarioDestinatario, mensagem);
        boolean retornoDestinatario = salvarMensagemFirebase(idUsuarioDestinatario, idUsuarioLogado, mensagem);

        if (!retornoRemetente) {
            Toast.makeText(ConversaActivity.this, "Problema ao enviar a mensagem, tente novamente!!!", Toast.LENGTH_SHORT).show();
        }
        if (!retornoDestinatario) {
            Toast.makeText(ConversaActivity.this, "Problema ao enviar a mensagem, tente novamente!!!", Toast.LENGTH_SHORT).show();
        }

        salvarConversaFirebase(idUsuarioLogado, idUsuarioDestinatario, mensagem);
        salvarConversaFirebase(idUsuarioDestinatario, idUsuarioLogado, mensagem);

    }

    private Boolean salvarConversaFirebase(String idRemetente, String idDestinatario, Mensagem mensagem) {

        conversa = new Conversa();
        conversa.setIdUsuario(idRemetente);
        conversa.setNome(idDestinatario);
        conversa.setMensagem(mensagem.getMensagem());


        try {

            conversaDatabaseReference = ConfiguracaoFirebase.getFirebase().child("conversas");
            conversaDatabaseReference.
                    child(idRemetente).
                    child(idDestinatario).
                    setValue(conversa);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private Boolean salvarMensagemFirebase(String idRemetente, String idDestinatario, Mensagem mensagem) {
        try {
            databaseReference.child("mensagem").child(idRemetente).child(idDestinatario).push().setValue(mensagem);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        mensagemDatabaseReference.addValueEventListener(valueEventListenerMensagens);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagemDatabaseReference.removeEventListener(valueEventListenerMensagens);
    }
}
