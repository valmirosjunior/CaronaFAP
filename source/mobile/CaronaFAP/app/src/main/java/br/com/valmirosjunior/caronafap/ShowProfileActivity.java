package br.com.valmirosjunior.caronafap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

import com.facebook.login.widget.ProfilePictureView;

import java.util.Calendar;

import br.com.valmirosjunior.caronafap.model.Coment;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.dao.ComentDAO;
import br.com.valmirosjunior.caronafap.model.dao.UserDAO;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.util.FaceBookManager;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.Util;

public class ShowProfileActivity extends AppCompatActivity implements Observer{
    private LayoutInflater inflater;
    private EditText editText;
    private RatingBar ratingBar;
    private View sendComentDialog;
    private String idUser;
    private User user;
    private UserDAO userDAO;
    private ComentDAO comentDAO;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inflater = getLayoutInflater();
        sendComentDialog = inflater.inflate(R.layout.send_coment_dialog,null);
        editText = (EditText) sendComentDialog.findViewById(R.id.editTextComent);
        ratingBar = (RatingBar) sendComentDialog.findViewById(R.id.ratingBarComent);
        Intent intent = getIntent();
        idUser=  intent.getStringExtra(Constants.ID_USER);
        userDAO = UserDAO.getInstance();
        user = userDAO.getUser(idUser);
        comentDAO = ComentDAO.getInstance();
    }

    public void openProfile (View view){
        ProfilePictureView profile = (ProfilePictureView) findViewById(R.id.profilePictureUser);
        Util.seeProfile(this,profile.getProfileId());
    }

    private void sendComent(){
        Coment coment = new Coment();
        coment.setAuthor(FaceBookManager.getCurrentUser());
        coment.setUser(user);
        coment.setDate(Calendar.getInstance());
        coment.setComent(editText.getText().toString());
        coment.setNote(ratingBar.getRating());
        comentDAO.sendComent(coment);
        clearFieldDialog();
    }

    public void showDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(sendComentDialog);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendComent();
            }
        });
    }

    private void clearFieldDialog(){
        ratingBar.setRating(0);
        editText.setText("");
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
