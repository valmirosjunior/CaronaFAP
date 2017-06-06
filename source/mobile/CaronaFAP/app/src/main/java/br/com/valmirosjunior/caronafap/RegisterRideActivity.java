package br.com.valmirosjunior.caronafap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import br.com.valmirosjunior.caronafap.fragments.TimePickerFragment;
import br.com.valmirosjunior.caronafap.model.MyLocation;
import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.model.dao.RideDAO;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.network.FaceBookManager;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.MessageUtil;
import br.com.valmirosjunior.caronafap.util.Util;

public class RegisterRideActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    private EditText  editAdress,editOrigin, editDestination,editTime;
    private RadioGroup radioGroup;
    private RideDAO rideDAO;
    private MyLocation locationOrigin, locationDestination,locationAdress;
    private String idRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ride);

        editOrigin = (EditText) findViewById(R.id.editOrigimRide);
        editDestination = (EditText) findViewById(R.id.editDestinationRide);
        editTime = (EditText) findViewById(R.id.editTimeRide);
        radioGroup = (RadioGroup) findViewById(R.id.rgRide);

        locationOrigin =new MyLocation();
        locationDestination = new MyLocation();

        rideDAO = RideDAO.getInstance();


        checkIsNewRideOrUpdate();

        Button butSaveRide = (Button) findViewById(R.id.butSaveRide);

        butSaveRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Ride ride = fillRide();
                    if (ride!=null){
                        ride.setId(idRide);
                        rideDAO.saveRide(ride);
                        showConfirmDialog();
                    }
                }catch (Exception e){
                    showAlertDialog("Erro!", "Um possivel erro ocorreu tente novamente mais tarde");
                }
            }
        });
    }

    public void openPlacePicker(View view) {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    locationAdress.setIdPlace(place.getId());
                    locationAdress.setLatitude(place.getLatLng().latitude);
                    locationAdress.setLongitude(place.getLatLng().longitude);
                    locationAdress.setName(place.getName().toString());
                    locationAdress.setAdress(place.getAddress().toString());
                    editAdress.setText(place.getAddress());
                }

            }
        }catch (Exception e ){
            showAlertDialog(getString(R.string.error),getString(R.string.internal_error));
            e.printStackTrace();
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    private Ride fillRide(){
        if(!valideFields()){
            return null;
        }
        Ride ride = new Ride();
        ride.setUser(FaceBookManager.getCurrentUser());
        Type type = (radioGroup.getCheckedRadioButtonId() == R.id.rbAskRide)?
                Type.ORDERED: Type.OFFERED;
        ride.setType(type);
        ride.setOrigin(locationOrigin);
        ride.setDestination(locationDestination);
        ride.setHourInMinutes(Util.convertStringTimeToSchedule(editTime.getText().toString()));
        return ride;
    }

    private void checkIsNewRideOrUpdate(){
        Intent intent = getIntent();
        idRide =intent.getStringExtra(Constants.ID_RIDE);
        if(idRide != null){
            Ride ride;
            ride = rideDAO.getRide(idRide);
            editOrigin.setText(ride.getOrigin().getAdress());
            editDestination.setText(ride.getDestination().getAdress());
            editTime.setText(ride.formaterTime());
            radioGroup.check((ride.getType()== Type.ORDERED?
                    R.id.rbAskRide : R.id.rbOferrRide ));
            locationOrigin =ride.getOrigin();
            locationDestination= ride.getDestination();
        }
    }

    private boolean valideFields() {
        if (radioGroup.getCheckedRadioButtonId() == -1){
            showAlertDialog("Erro!","É necessário escolher o Que você deseja fazer!!");
            return false;
        }
        if(editOrigin.getText().toString().equals("")){
            showAlertDialog("Erro!","É necessário escolher um Local de origem!");
            return false;
        }
        if(editDestination.getText().toString().equals("")){
            showAlertDialog("Erro!","É necessário escolher um Local de Destino!");
            return false;
        }
        if(editTime.getText().toString().equals("")){
            showAlertDialog("Erro!","É necessário escolher um Horário Desejado!");
            return false;
        }
        return true;
    }

    private void showConfirmDialog(){
        AlertDialog.Builder builder = MessageUtil.createAlertDialogBuilder(this);
        builder.setMessage("Sua Solicitação de Carona Foi registrada");
        builder.setTitle("Solicitação Realizada com Sucesso!");

        builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                backToHomeScreen();
            }
        });
        builder.setPositiveButton(R.string.new_ride, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                continueHere();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertDialog(String title,String message){
        AlertDialog.Builder builder = MessageUtil.createAlertDialogBuilder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("OK", null);
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
    }

}
