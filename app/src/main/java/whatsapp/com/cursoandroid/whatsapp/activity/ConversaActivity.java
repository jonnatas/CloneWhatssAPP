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

import whatsapp.com.cursoandroid.whatsapp.R;
import whatsapp.com.cursoandroid.whatsapp.helper.Base64Custom;
import whatsapp.com.cursoandroid.whatsapp.helper.Preferencias;
import whatsapp.com.cursoandroid.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton btMensagem;
    private EditText editMensagem;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> mensagens;
    private ValueEventListener valueEventListenerMensagens;

    private Bundle extra;
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;
    private String idUsuarioLogado;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mensagemDatabaseReference = databaseReference.child("mensagem");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioLogado = preferencias.getIdentificador();

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

        arrayAdapter = new ArrayAdapter<String>(
                ConversaActivity.this,
                android.R.layout.simple_list_item_1,
                mensagens
        );

        listView.setAdapter(arrayAdapter);

        mensagemDatabaseReference = mensagemDatabaseReference
                .child(idUsuarioLogado)
                .child(idUsuarioDestinatario);

        valueEventListenerMensagens = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensagens.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Mensagem mensagem = data.getValue(Mensagem.class);

                    mensagens.add(mensagem.getMensagem());
                }

                arrayAdapter.notifyDataSetChanged();
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

                Toast.makeText(ConversaActivity.this, idUsuarioLogado+"   "+idUsuarioDestinatario+"   "+textoMensagem, Toast.LENGTH_SHORT).show();

                salvarMensagemFirebase(idUsuarioLogado, idUsuarioDestinatario,mensagem);
            }
        }
    });
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
}
