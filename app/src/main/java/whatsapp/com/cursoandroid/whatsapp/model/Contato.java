package whatsapp.com.cursoandroid.whatsapp.model;

/**
 * Created by jonnatas on 25/01/17.
 */

public class Contato {
    public String indentificadorUsuario;
    public String nome;
    public String email;

    public Contato() {
    }

    public String getIndentificadorUsuario() {
        return indentificadorUsuario;
    }

    public void setIndentificadorUsuario(String indentificadorUsuario) {
        this.indentificadorUsuario = indentificadorUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
