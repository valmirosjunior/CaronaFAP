package br.com.valmirosjunior.caronafap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import br.com.valmirosjunior.caronafap.model.Carona;
import br.com.valmirosjunior.caronafap.util.DialogsdMessages;
import br.com.valmirosjunior.caronafap.util.FaceBookUtil;

public class PedirCarona extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    FaceBookUtil faceBookUtil=new FaceBookUtil(this);
    EditText editText, edOrigem,edDestino;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_carona);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edOrigem = (EditText) findViewById(R.id.edOrigemCarona);
        edDestino = (EditText) findViewById(R.id.edDestino);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Carona carona=makeCarona();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Caronas").child("pedidas").child(carona.getCodigo());
                myRef.child("origem").setValue(carona.getOrigem());
                myRef.child("destino").setValue(carona.getDedstino());
                DialogsdMessages.showCustomToast(PedirCarona.this,"Espere um pouco essa parte ainda falta");
                //faceBookUtil.disconnectFromFacebook();

            }
        });
    }

    public void openMap(View view) {
        editText=(EditText) view;
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
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                editText.setText(place.getAddress());
            }

        }
    }

    private Carona makeCarona(){
        String codigo,origem,destino;
        codigo =" cd "+ new Random().nextInt(10000000);
        origem= edOrigem.getText().toString();
        destino= edDestino.getText().toString();
        return new Carona(codigo,origem,destino);
    }

}
