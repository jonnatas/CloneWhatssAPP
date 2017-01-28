package whatsapp.com.cursoandroid.whatsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import whatsapp.com.cursoandroid.whatsapp.R;
import whatsapp.com.cursoandroid.whatsapp.model.Contato;

/**
 * Created by jonnatas on 26/01/17.
 */

public class ContatoAdapter extends ArrayAdapter<Contato>{

    private Context context;
    private ArrayList<Contato> contatos;


    public ContatoAdapter(Context context, int resource, ArrayList<Contato> objects) {
        super(context, 0, objects);
        this.context = context;
        this.contatos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        if (!contatos.isEmpty()){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            TextView textView = (TextView) view.findViewById(R.id.tv_nome);
            Contato contato = contatos.get(position);

            textView.setText(contato.getNome());

        }
        return view;
    }
}

