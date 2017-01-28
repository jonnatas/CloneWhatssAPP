package whatsapp.com.cursoandroid.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsapp.com.cursoandroid.whatsapp.Adapter.ContatoAdapter;
import whatsapp.com.cursoandroid.whatsapp.R;
import whatsapp.com.cursoandroid.whatsapp.activity.ConversaActivity;
import whatsapp.com.cursoandroid.whatsapp.helper.Preferencias;
import whatsapp.com.cursoandroid.whatsapp.model.Contato;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userReference = databaseReference.child("contatos");
    private ValueEventListener valueEventListenerContato;

    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        contatos = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_contatos);

        /*adapter = new ArrayAdapter(
                getActivity(),
                R.layout.lista_contatos,
                contatos
        );*/
        adapter = new ContatoAdapter(getActivity(),0, contatos);

        listView.setAdapter(adapter);


        Preferencias preferencias = new Preferencias(getActivity());
        String identificarUsuarioLogado = preferencias.getIdentificador();

        userReference = userReference.child(identificarUsuarioLogado);
        valueEventListenerContato = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                contatos.clear();
                for (DataSnapshot dado : dataSnapshot.getChildren()){
                    Contato contato = dado.getValue(Contato.class);
                    contatos.add(contato);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                Contato contato = contatos.get(position);

                intent.putExtra("nome",contato.getNome());
                intent.putExtra("email",contato.getEmail());

                startActivity(intent);
            }
        });

        return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        userReference.addValueEventListener(valueEventListenerContato);
        Log.i("log", "EVENTO::onStart>>");
    }

    @Override
    public void onStop() {
        super.onStop();
        userReference.removeEventListener(valueEventListenerContato);
        Log.i("log", "EVENTO::onStop<<");
    }

}
