package whatsapp.com.cursoandroid.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by jonnatas on 03/02/17.
 */

public final class ConfiguracaoFirebase {
    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth auth;

    public  static FirebaseAuth getFirebaseAuth(){

        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static DatabaseReference getFirebase(){

        if (referenciaFirebase==null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

}
