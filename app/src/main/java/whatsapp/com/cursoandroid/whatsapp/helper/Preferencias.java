package whatsapp.com.cursoandroid.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by jonnatas on 22/01/17.
 */

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whatsapp.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuario";
    private final String CHAVE_NOME = "nome";

    public Preferencias(Context contexto){
        this.contexto = contexto;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(String identificadorUsuario, String nome){
        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nome);
        editor.commit();
    }

    public String getIdentificador(){

        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){

        return preferences.getString(CHAVE_NOME, null);
    }


}
