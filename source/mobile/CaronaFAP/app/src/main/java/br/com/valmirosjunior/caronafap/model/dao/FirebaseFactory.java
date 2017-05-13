package br.com.valmirosjunior.caronafap.model.dao;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by junior on 11/05/17.
 */

public abstract class FirebaseFactory {

    private static FirebaseDatabase database;

    public static FirebaseDatabase getInstance(){
        if(database== null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            database =FirebaseDatabase.getInstance();
        }
        return database;
    }
}
