package br.com.valmirosjunior.caronafap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import br.com.valmirosjunior.caronafap.model.ObserverRide;
import br.com.valmirosjunior.caronafap.model.dao.RideDAO;
import br.com.valmirosjunior.caronafap.util.CustomAdapterRide;
import br.com.valmirosjunior.caronafap.util.MessageUtil;

public class ShowRider extends AppCompatActivity implements ObserverRide {
    private  RideDAO rideDAO;
    private CustomAdapterRide customAdapterRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        rideDAO =RideDAO.getInstance();
        customAdapterRide = new CustomAdapterRide(this);

        ListView listView = (ListView) findViewById(R.id.listViewShowRides);
        listView.setAdapter(customAdapterRide);

        updateUi();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void updateUi() {
        rideDAO.addObserver(this);
        customAdapterRide.notifyDataSetInvalidated();
        customAdapterRide.setRidesFromMap(rideDAO.getMapRide());
        customAdapterRide.notifyDataSetChanged();
    }


    @Override
    protected void onStop() {
        super.onStop();
        rideDAO.removeObserver(this);
        MessageUtil.hideProgressDialog();
    }

}
