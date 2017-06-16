package br.com.valmirosjunior.caronafap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.dao.UserDAO;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;
import br.com.valmirosjunior.caronafap.util.Constants;

public class Conversation extends AppCompatActivity implements Observer{
    private String id,ID_USER = Constants.ID_USER;
    private User user;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        userDAO = UserDAO.getInstance();
        Intent intent = getIntent();
        id = intent.getStringExtra(ID_USER);
        user = userDAO.getUser(id);
    }

    @Override
    public void update(Object object) {

    }

    @Override
    public void update(Observable observable, Object object) {

    }

    @Override
    public Type getType() {
        return null;
    }
}
