package whatsapp.com.cursoandroid.whatsapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsapp.com.cursoandroid.whatsapp.Adapter.ConversaAdapter;
import whatsapp.com.cursoandroid.whatsapp.R;
import whatsapp.com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.com.cursoandroid.whatsapp.helper.Preferencias;
import whatsapp.com.cursoandroid.whatsapp.model.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private ListView listView;
    private ArrayList<Conversa> conversas;
    private ArrayAdapter<Conversa> arrayAdapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerConversas;

    public ConversasFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        listView = (ListView) view.findViewById(R.id.lv_comversas);
        conversas = new ArrayList<>();
        arrayAdapter = new ConversaAdapter(getActivity(), conversas);
        listView.setAdapter(arrayAdapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("conversas")
                .child(idUsuarioLogado);

        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conversas.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListenerConversas);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerConversas);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerConversas);

    }
}
