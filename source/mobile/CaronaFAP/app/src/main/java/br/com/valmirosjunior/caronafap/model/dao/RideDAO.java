package br.com.valmirosjunior.caronafap.model.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.valmirosjunior.caronafap.model.ObserverRide;
import br.com.valmirosjunior.caronafap.model.Ride;

/**
 * Created by junior on 10/04/17.
 */

public class RideDAO {
    private static RideDAO rideDao;
    private FirebaseDatabase database;
    private DatabaseReference refToRides;
    private HashMap<String,Ride> rides;
    private ArrayList<ObserverRide> observerRides;

    private  RideDAO() {
        database = FirebaseDatabase.getInstance();
        refToRides = database.getReference("Rides");
        rides= new HashMap<String, Ride>();
        observerRides= new ArrayList<>();
        addValueEventListener();
    }

    public static RideDAO getInstance (){
        if (rideDao == null){
            rideDao = new RideDAO();
        }
        return rideDao;
    }

    public void saveRide(Ride ride){
        refToRides.push().setValue(ride);
    }

    private void addValueEventListener(){
        ValueEventListener rideListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rides= new HashMap<String, Ride>();
                try {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Ride ride = snapshot.getValue(Ride.class);
                            rides.put(snapshot.getKey(),ride);
                        }
                    }
                    for (ObserverRide observerRide: observerRides) {
                        observerRide.updateUi();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Cancelled Read Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        refToRides.addValueEventListener(rideListener);
    }

    public HashMap<String,Ride> getMapRide(){
        return rides;
    }

    public void addObserver(ObserverRide observerRide){
        this.observerRides.add(observerRide);
    }

    public void removeObserver (ObserverRide observerRide){
        if(observerRides.contains(observerRide)){
            observerRides.remove(observerRide);
        }
    }

}
