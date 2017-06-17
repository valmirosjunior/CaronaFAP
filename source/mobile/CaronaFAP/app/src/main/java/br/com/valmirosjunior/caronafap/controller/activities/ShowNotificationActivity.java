package br.com.valmirosjunior.caronafap.controller.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.model.Solicitation;
import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.dao.SolicitationDAO;
import br.com.valmirosjunior.caronafap.model.dao.RideDAO;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.MessageUtil;
import br.com.valmirosjunior.caronafap.util.Util;

public class ShowNotificationActivity extends AppCompatActivity implements Observer {

    private RideDAO rideDAO;
    private Ride ride;
    private SolicitationDAO solicitationDAO;
    private Solicitation solicitation;
    private ProfilePictureView profilePicture;
    private TextView tvDescriprition;
    private LinearLayout layout;
    private Button butSeeProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_solicitation);
        rideDAO= RideDAO.getInstance();
        layout = (LinearLayout) findViewById(R.id.layoutButtons);
        butSeeProfile = (Button) findViewById(R.id.butSeeProfile);
        solicitationDAO = SolicitationDAO.getInstance();
        tvDescriprition = (TextView) findViewById(R.id.tvDescriptionNotification);
        profilePicture = (ProfilePictureView) findViewById(R.id.profilePictureUser);
        Intent intent = getIntent();
        String idNotification = intent.getStringExtra(Constants.ID_NOTIFICATION);

        solicitation = solicitationDAO.getNotification(idNotification);
        if(solicitation != null){
            updateFields(solicitation);
        }else{
            solicitationDAO.getNotification(idNotification,this);
        }

    }

    public void seeProfile(View view){
        String idUser= solicitation.getSender().getId();
        Util.seeProfile(this,idUser);
    }

    private void updateFields(Solicitation solicitation){
        if(solicitation.getRide() != null){
            layout.setVisibility(View.INVISIBLE);
            butSeeProfile.setVisibility(View.VISIBLE);
        }else if (layout.getVisibility() == View.INVISIBLE){
            layout.setVisibility(View.VISIBLE);
            butSeeProfile.setVisibility(View.INVISIBLE);
        }
        rideDAO.getRide(solicitation.getIdRide(),this);

    }

    @Override
    public void update(Object object) {
        User sender= solicitation.getSender();
        ride = (Ride) object;
        profilePicture.setProfileId(sender.getId());
        String action = ((solicitation.getSend()== Type.REQUEST)? "Solicitou" : "Aceitou");
        tvDescriprition.setText(sender.getName()+ "\n"+action+ "\n"+ride.showDescription());
    }

    @Override
    public void update(Observable observable, Object object) {
        Solicitation solicitation = (Solicitation) object;
        updateFields(solicitation);
    }

    @Override
    public Type getType() {
        return null;
    }

    public void respondNotification(View view){
        if(view.getId() == R.id.btAccept){
            showConfirmRequestRide(Type.CONFIRM);
        }else if(view.getId() == R.id.btReject){
            showConfirmRequestRide(Type.REJECT);
        }
    }

   /* private void sendNotification(Type type){
        User user= solicitation.getReceiver();
        Solicitation respondNotification= new Solicitation();
        respondNotification.setSender(solicitation.getReceiver());
        respondNotification.setReceiver(solicitation.getSender());
        respondNotification.setIdRide(solicitation.getIdRide());
        respondNotification.setType(type);
        solicitationDAO.sendNotification(respondNotification);
    }*/

    private void sendNotification(Type type){
        solicitation.setReceive(type);
        solicitationDAO.sendNotification(solicitation);
    }

    private void showConfirmRequestRide(final Type type){
        AlertDialog.Builder builder = MessageUtil.createAlertDialogBuilder(this);
        builder.setTitle(R.string.alert);
        builder.setMessage(type == Type.CONFIRM ? R.string.confirm_accept_request: R.string.reject_accept_request );

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendNotification(type);
            }
        });
        builder.setNegativeButton(R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
