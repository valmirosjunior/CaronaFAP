package br.com.valmirosjunior.caronafap.model.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.valmirosjunior.caronafap.model.User;

/**
 * Created by junior on 08/05/17.
 */

public class UserDAO {
    private static UserDAO userDAO;
    private FirebaseDatabase database;
    private DatabaseReference refToUsers,ref;


    private UserDAO() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        database = FirebaseDatabase.getInstance();
        refToUsers = database.getReference("USers");
        refToUsers.keepSynced(true);
    }

    public static UserDAO getInstance (){
        if (userDAO == null){
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    public void saveUser(User user){
        refToUsers.child(user.getId()).setValue(user);
    }

}
