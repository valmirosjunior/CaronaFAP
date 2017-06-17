package br.com.valmirosjunior.caronafap.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.controller.adapter.RideAdapter;
import br.com.valmirosjunior.caronafap.controller.helpers.RideHelper;
import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.model.dao.RideDAO;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.MessageUtil;

public class ShowRidesActivity extends AppCompatActivity implements Observer {
    private RideDAO rideDAO;
    private Ride ride;
    private RideHelper rideHelper;
    private RideAdapter rideAdapter;
    private TextView textViewMessage;
    private Type typeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rides);
        init();
        updateMessage();
        rideDAO =RideDAO.getInstance();
        rideDAO.addObserver(this);
        rideDAO.notifyObservers();
        rideHelper = new RideHelper(this,rideDAO);
    }

    private void init(){
        rideAdapter = new RideAdapter(this,new ArrayList<Ride>());
        ListView listView = (ListView) findViewById(R.id.listViewShowRides);
        listView.setAdapter(rideAdapter);
        listView.setOnItemClickListener(rideAdapter);

        textViewMessage = (TextView) findViewById(R.id.textViewMessage);

        registerForContextMenu(listView);
        Intent intent = getIntent();

        if (intent.hasExtra(Constants.TYPE_OBSERVER)){
            String nameType = intent.getStringExtra(Constants.TYPE_OBSERVER);
            typeObserver = Type.valueOf(nameType);
        }else {
            typeObserver = Type.MINE;
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowRidesActivity.this, RegisterRideActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        rideDAO.addObserver(this);
        rideDAO.notifyObservers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rideDAO.addObserver(this);
        rideDAO.notifyObservers();
    }

    @Override
    protected void onStop() {
        super.onStop();
        rideDAO.deleteObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rideDAO.deleteObserver(this);
    }

    private void changeTypeObserver() {
        updateMessage();
        MessageUtil.showProgressDialog(this);
        rideDAO.setRide(ride);
        typeObserver = typeObserver== Type.MINE ? Type.OTHER_RIDES: Type.MINE;
        rideDAO.notifyObservers();
        updateMessage();
        MessageUtil.hideProgressDialog();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater =getMenuInflater();
        if(typeObserver == Type.OTHER_RIDES){
            inflater.inflate(R.menu.menu_other_rides,menu);
        }else{
            inflater.inflate(R.menu.menu_my_liste_ride, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent;
        Ride ride = rideAdapter.getRides().get(info.position);
        switch (item.getItemId()) {
            case R.id.edit_ride:
                rideHelper.editRide(ride);
                return true;

            case R.id.delete:
                rideHelper.deleteRide(ride);
                return true;

            case R.id.viewOnmap :
                rideHelper.seeRouteOnMap(ride);
                return true;

            case R.id.viewProfile :
                String idUSer= ride.getUser().getId();
                rideHelper.seeProfile(idUSer);
                return true;

            case R.id.findRide:
                rideHelper.findPatner(ride);
                return true;

            case R.id.requestRide:
                rideHelper.requestRide(this,ride);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
    private void updateMessage (){
        if(typeObserver ==  Type.MINE){
            setTitle(R.string.your_rides);
        }else{
            setTitle(R.string.result_search);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        List<Ride> rides = (List<Ride>) o;
        if (rides.size()==0){
            textViewMessage.setVisibility(View.VISIBLE);
            if(typeObserver == Type.MINE){
                textViewMessage.setText(R.string.no_register_rides);
            }else{
                textViewMessage.setText(R.string.no_found_rides);
            }
        }else {
            textViewMessage.setVisibility(View.INVISIBLE);
            textViewMessage.setText("");
        }
        rideAdapter.notifyDataSetInvalidated();
        rideAdapter.setRides(rides);
        rideAdapter.notifyDataSetChanged();

    }

    @Override
    public Type getType() {
        return typeObserver;
    }

    @Override
    public void update(Object object) {
        update(null, object);
    }
}