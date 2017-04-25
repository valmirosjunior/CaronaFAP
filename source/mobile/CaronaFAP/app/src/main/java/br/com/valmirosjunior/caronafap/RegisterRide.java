package br.com.valmirosjunior.caronafap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;

import br.com.valmirosjunior.caronafap.fragments.TimePickerFragment;
import br.com.valmirosjunior.caronafap.model.MyLocation;
import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.model.TypeRide;
import br.com.valmirosjunior.caronafap.model.dao.RideDAO;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.FaceBookUtil;
import br.com.valmirosjunior.caronafap.util.MessageUtil;
import br.com.valmirosjunior.caronafap.util.Util;

public class RegisterRide extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    FaceBookUtil faceBookUtil=new FaceBookUtil(this);
    private EditText editDistance, editAdress,editOrigin, editDestination,editTime;
    private RideDAO rideDAO;
    private MyLocation locationOrigin, locationDestination,locationAdress;
    private int ordinalTypeRide;

    public RegisterRide() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        ordinalTypeRide =intent.getIntExtra(Constants.TYPE_RIDE_EXTRA,0);
        setTitle( (ordinalTypeRide==TypeRide.OFFERED.ordinal())? R.string.offer_ride : R.string.ask_ride);


        editOrigin = (EditText) findViewById(R.id.editOrigimRide);
        editDestination = (EditText) findViewById(R.id.editDestinationRide);
        editTime = (EditText) findViewById(R.id.editTimeRide);
        editDistance = (EditText) findViewById(R.id.editDistance);
        locationOrigin =new MyLocation();
        locationDestination = new MyLocation();

        rideDAO = RideDAO.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageUtil.showProgressDialog(RegisterRide.this);
                rideDAO.saveRide(makeRide());
                MessageUtil.hideProgressDialog();
                showConfirmDialog();
            }
        });
    }

    public void openMap(View view) {
        editAdress =(EditText) view;
        locationAdress = (editAdress.getId() == R.id.editOrigimRide) ? locationOrigin : locationDestination;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    public void saveRide(View view){

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this,data);
                locationAdress.setIdPlace(place.getId());
                locationAdress.setLatitude(place.getLatLng().latitude);
                locationAdress.setLongitude(place.getLatLng().longitude);
                locationAdress.setName(place.getName().toString());
                locationAdress.setAdress(place.getAddress().toString());
                editAdress.setText(place.getAddress());
            }

        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    private Ride makeRide(){
        Ride ride = new Ride();
        ride.setUser(FaceBookUtil.getCurrentUser());
        ride.setTypeRide(TypeRide.values()[ordinalTypeRide]);
        ride.setOrigin(locationOrigin);
        ride.setDestination(locationDestination);
        ride.setDistance(Integer.parseInt(editDistance.getText().toString()));
        ride.setScheduleRide(Util.convertStringTimeToSchedule(editTime.getText().toString()));
        ride.setDateEvent(Calendar.getInstance().getTime());
        return ride;
    }

    private void showConfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sua Solicitação de Carona Foi registrada").setTitle("Solicitação Realizada com Sucesso!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backToHomeScreen();
            }
        });

        builder.setNegativeButton("Nova!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                continueHere();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void backToHomeScreen(){
        this.finish();
    }

    private void continueHere(){
        editOrigin.setText("");
        editDestination.setText("");
        editTime.setText("");
        editDistance.setText("");
    }

}
