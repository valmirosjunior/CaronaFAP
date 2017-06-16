package br.com.valmirosjunior.caronafap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import br.com.valmirosjunior.caronafap.controller.functions.FunctionsRide;
import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.model.dao.RideDAO;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.MessageUtil;

public class ShowOneRideActivity extends AppCompatActivity {
    private RideDAO rideDAO;
    private Ride ride;
    private FunctionsRide functionsRide;
    private TextView  tvdescription;
    private ProfilePictureView ppvUser;
    private LinearLayout llUser,llOtherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_one_ride);

        tvdescription = (TextView) findViewById(R.id.tvDescriptoinRide);
        ppvUser = (ProfilePictureView) findViewById(R.id.pvUser);
        llUser = (LinearLayout) findViewById(R.id.llUser);
        llOtherUser = (LinearLayout) findViewById(R.id.llOtherUser);

        rideDAO = RideDAO.getInstance();

        Intent intent = getIntent();
        String idRide = intent.getStringExtra(Constants.ID_RIDE);
        ride = rideDAO.getRide(idRide);
        functionsRide = new FunctionsRide(this,rideDAO);

        updateFields(ride);
        changeButton();
    }

    private void updateFields(Ride ride){

        ppvUser.setProfileId(ride.getUser().getId());
        tvdescription.setText(Html.fromHtml(ride.showDescription()));
    }

    private void changeButton(){
        if(ride.isMine()){
            llOtherUser.setVisibility(View.GONE);
        }else{
            llUser.setVisibility(View.GONE);
        }
    }

    public void openMap(View view){
        functionsRide.seeRouteOnMap(ride);
    }

    public void seeProfile(View view){
        functionsRide.seeProfile(ride.getUser().getId());
    }

    public void editRide(View view){
        functionsRide.editRide(ride);
    }

    public void deleteRide(View view){
       functionsRide.deleteRide(ride);
    }

    public void findPatner(View view){
        functionsRide.findPatner(ride);
    }

    public void requestRide(View view){
        MessageUtil.showConfirm(this, getString(R.string.confirm_delete_ride),functionsRide.requestRide(ride));
    }
}