package br.com.valmirosjunior.caronafap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import br.com.valmirosjunior.caronafap.adapter.CustomAdapterRide;
import br.com.valmirosjunior.caronafap.model.ObserverRide;
import br.com.valmirosjunior.caronafap.model.dao.RideDAO;
import br.com.valmirosjunior.caronafap.util.MessageUtil;

public class ShowRider extends AppCompatActivity implements ObserverRide {
    private  RideDAO rideDAO;
    private CustomAdapterRide customAdapterRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rider);


        rideDAO =RideDAO.getInstance();
        customAdapterRide = new CustomAdapterRide(this);

        ListView listView = (ListView) findViewById(R.id.listViewShowRides);
        listView.setAdapter(customAdapterRide);

        rideDAO.addObserver(this);
        updateUi(rideDAO.isMapReady());


    }

    @Override
    public void updateUi(boolean mapReady) {
        if (!mapReady){
            MessageUtil.showProgressDialog(this);
        }else {
            customAdapterRide.notifyDataSetInvalidated();
            customAdapterRide.setRidesFromMap(rideDAO.getMapRide());
            customAdapterRide.notifyDataSetChanged();
            MessageUtil.hideProgressDialog();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        rideDAO.removeObserver(this);
        MessageUtil.hideProgressDialog();
    }

}
