package whatsapp.com.cursoandroid.whatsapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import whatsapp.com.cursoandroid.whatsapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<String> contatos;

    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        contatos = new ArrayList<>();
        contatos.add("Teste 1");
        contatos.add("Teste 2");

        listView = (ListView) view.findViewById(R.id.lv_contatos);

        adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                contatos
        );
        listView.setAdapter(adapter);

        return view;

    }

}
