package br.com.valmirosjunior.caronafap.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.model.Solicitation;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.dao.SolicitationDAO;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.FaceBookManager;
import br.com.valmirosjunior.caronafap.util.MessageUtil;
import br.com.valmirosjunior.caronafap.util.Util;

public class ShowOneSolicitationActivity extends AppCompatActivity implements Observer {

    private SolicitationDAO solicitationDAO;
    private Solicitation solicitation;
    private ProfilePictureView ppvUser;
    private TextView tvDescriptionRide,tvDescriptionSolicitation;
    private LinearLayout layout;
    private Button butSeeProfile;
    private String idSolicitation;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_one_solicitation);

        layout = (LinearLayout) findViewById(R.id.layoutButtons);
        butSeeProfile = (Button) findViewById(R.id.butSeeProfile);
        solicitationDAO = SolicitationDAO.getInstance();

        ppvUser = (ProfilePictureView) findViewById(R.id.profilePictureUser);
        tvDescriptionSolicitation = (TextView) findViewById(R.id.tvDescriptoinSolicitation);
        tvDescriptionRide = (TextView) findViewById(R.id.tvDescriptoinRide);

        Intent intent = getIntent();

        idSolicitation = intent.getStringExtra(Constants.ID_SOLICITATION);

        solicitation = solicitationDAO.getSolicitation(idSolicitation);
        if(solicitation != null){
            updateFields(solicitation);
        }else{
            solicitationDAO.addObserver(this);
        }

    }

    public void seeProfile(View view){
        Util.seeProfile(this, user.getId());
    }

    public void seeRoute(View view) {
        Util.seeOnMap(this,solicitation.getRide());
    }

    private void updateFields(Solicitation solicitation){
        if(FaceBookManager.getCurrentUser().equals(solicitation.getSender())){
            user =solicitation.getRide().getUser();
            disableComands();
        }else{
            user =solicitation.getSender();
        }
        ppvUser.setProfileId(user.getId());
        tvDescriptionSolicitation.setText(Html.fromHtml(solicitation.showDescription()));
        tvDescriptionRide.setText(Html.fromHtml(solicitation.getRide().showDescrpitionSolicitation()));

    }
    private void disableComands() {
        layout.setVisibility(View.INVISIBLE);
    }


    public void seeComents(View view) {
        Intent intent = new Intent(this,ShowProfileActivity.class);
        intent.putExtra(Constants.ID_USER, user.getId());
        startActivity(intent);
    }
    @Override
    public void update(Object object) {
        solicitation = solicitationDAO.getSolicitation(idSolicitation);
        updateFields(solicitation);
    }

    @Override
    public void update(Observable observable, Object object) {
        update(object);
    }

    @Override
    public Type getType() {
        return null;
    }

    public void respondSolicitation(View view){
        if(view.getId() == R.id.btAccept){
            showConfirmRequestRide(Type.CONFIRM);
        }else if(view.getId() == R.id.btReject){
            showConfirmRequestRide(Type.REJECT);
        }
    }

    private void respondSolicitation(Type type){
        solicitation.setMessageResponse(type);
        solicitationDAO.sendSolicitation(solicitation);
    }

    private void showConfirmRequestRide(final Type type){
        MessageUtil.showConfirm(this,getString(R.string.confirm_request_ride),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                respondSolicitation(type);
                finish();
            }
        });


    }


}
