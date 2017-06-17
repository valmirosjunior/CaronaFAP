package br.com.valmirosjunior.caronafap.controller.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.controller.adapter.ComentAdapter;
import br.com.valmirosjunior.caronafap.model.Coment;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.dao.UserDAO;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.FaceBookManager;
import br.com.valmirosjunior.caronafap.util.MessageUtil;
import br.com.valmirosjunior.caronafap.util.Util;

public class ShowProfileActivity extends AppCompatActivity implements Observer{
    private ComentAdapter comentAdapter;
    private ListView lvComents;
    private AlertDialog alertDialog;
    private EditText etComent;
    private RatingBar rbDialog, rbUser;
    private ProfilePictureView ppvUser;
    private String idUser;
    private User user;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        rbUser = (RatingBar) findViewById(R.id.rbUser);
        ppvUser =(ProfilePictureView) findViewById(R.id.ppvUser);

        Intent intent = getIntent();
        idUser=  intent.getStringExtra(Constants.ID_USER);
        userDAO = UserDAO.getInstance();
        user = userDAO.getUser(idUser);
        setTitle(user.getName());

        comentAdapter = new ComentAdapter(this,new ArrayList<Coment>());
        lvComents = (ListView) findViewById(R.id.lvComents);
        lvComents.setAdapter(comentAdapter);
        ppvUser.setProfileId(idUser);
        userDAO.addObserver(this);
        update(userDAO);

    }

    public void openProfile (View view){
        ProfilePictureView profile = (ProfilePictureView) findViewById(R.id.profilePictureUser);
        Util.seeProfile(this,profile.getProfileId());
    }

    private void sendComent(){
        MessageUtil.showConfirm(this, getString(R.string.confirm_send_coment),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Coment coment = makeComent();
                        userDAO.sendComment(user,coment);
                        MessageUtil.showAlertDialogBuilder(ShowProfileActivity.this,
                                getString(R.string.success),getString(R.string.send_coment_succes));
                    }
                });
    }

    private Coment makeComent() {
        Coment coment = new Coment();
        coment.setAuthor(FaceBookManager.getCurrentUser());
        coment.setIdAuthor(coment.getAuthor().getId());
        coment.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        coment.setComent(etComent.getText().toString());
        coment.setNote(rbDialog.getRating());
        clearFieldDialog();
        return coment;
    }

    public void showDialog(View view) {
        if(alertDialog == null) {

            LayoutInflater inflater = getLayoutInflater();
            View scd = inflater.inflate(R.layout.send_coment_dialog,null);

            etComent = (EditText) scd.findViewById(R.id.etComent);
            rbDialog = (RatingBar) scd.findViewById(R.id.rbComent);

            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setView(scd);
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    clearFieldDialog();
                }
            });
            builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sendComent();
                }
            });
            alertDialog = builder.create();
        }

        alertDialog.show();
    }

    private void clearFieldDialog(){
        rbDialog.setRating(0);
        etComent.setText("");
    }

    @Override
    public void update(Object object) {
        user = userDAO.getUser(idUser);
        comentAdapter.notifyDataSetInvalidated();
        comentAdapter.setComents(user.getComents());
        comentAdapter.notifyDataSetChanged();
        rbUser.setRating(comentAdapter.getNote());
    }

    @Override
    public void update(Observable observable, Object object) {

    }

    @Override
    public Type getType() {
        return null;
    }
}
