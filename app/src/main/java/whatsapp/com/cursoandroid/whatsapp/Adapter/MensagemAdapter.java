package whatsapp.com.cursoandroid.whatsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import whatsapp.com.cursoandroid.whatsapp.R;
import whatsapp.com.cursoandroid.whatsapp.activity.ConversaActivity;
import whatsapp.com.cursoandroid.whatsapp.helper.Preferencias;
import whatsapp.com.cursoandroid.whatsapp.model.Mensagem;

/**
 * Created by jonnatas on 29/01/17.
 */

public class MensagemAdapter extends ArrayAdapter<Mensagem> {
    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(Context context, ArrayList objects) {
        super(context, 0, objects);
        this.context = context;
        this.mensagens = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  null;
        if (!mensagens.isEmpty()){
            Mensagem mensagem = mensagens.get(position);

            Preferencias preferencias = new Preferencias(context);
            String idUsuarioLogado = preferencias.getIdentificador();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            if (idUsuarioLogado.equals(mensagem.getIdUsuario())){
                view = layoutInflater.inflate(R.layout.item_mensagem_direita, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }

            TextView textView = (TextView) view.findViewById(R.id.tv_mensagem);
            textView.setText(mensagem.getMensagem());
        }
        return view;

    }
}
