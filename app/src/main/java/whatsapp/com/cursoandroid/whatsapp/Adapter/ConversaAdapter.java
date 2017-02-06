package whatsapp.com.cursoandroid.whatsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import whatsapp.com.cursoandroid.whatsapp.R;
import whatsapp.com.cursoandroid.whatsapp.model.Conversa;

/**
 * Created by jonnatas on 06/02/17.
 */

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private ArrayList<Conversa> conversas;
    private Context context;
    private Conversa conversa;

    public ConversaAdapter(Context context, ArrayList<Conversa> objects) {
        super(context, 0, objects);
        this.context = context;
        this.conversas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (conversas!=null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_conversas, parent, false);

            TextView nome = (TextView) view.findViewById(R.id.text_nome);
            TextView ultimaMensagem = (TextView) view.findViewById(R.id.text_mensagem);

            conversa = conversas.get(position);
            nome.setText(conversa.getNome());
            ultimaMensagem.setText(conversa.getMensagem());

        }

        return view;
    }
}
