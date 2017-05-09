package br.com.valmirosjunior.caronafap.model.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import br.com.valmirosjunior.caronafap.model.Ride;

/**
 * Created by junior on 10/04/17.
 */

public class RideDAO extends Observable{
    private static RideDAO rideDao;
    private List<Observer> observers;
    private DatabaseReference refToRides, ref;
    private HashMap<String,Ride> rideMap;
    private List<Ride> rides;


    private RideDAO() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        refToRides = FirebaseDatabase.getInstance().getReference("Rides");
        refToRides.keepSynced(true);
        rideMap = new HashMap<>();
        observers = new ArrayList<>();
        refToRides.addValueEventListener(addValueEventListiner());
    }

    public static RideDAO getInstance() {
        if (rideDao == null) {
            rideDao = new RideDAO();
        }
        return rideDao;
    }

    public void saveRide(Ride ride) {
        ref = refToRides.push();
        if (ride.getIdRide() == null) {
            ride.setIdRide(ref.getKey());
        }
        ref.setValue(ride);
    }

    private ValueEventListener addValueEventListiner(){
        ValueEventListener rideListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        rideMap = new HashMap<>();
                        for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                            rideMap.put(rideSnapshot.getKey(), rideSnapshot.getValue(Ride.class));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    notifyObservers();
                    addChildAddEventListenerToRides();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Cancelled Read Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        return rideListener;

    }

    private void addChildAddEventListenerToRides() {
        refToRides.removeEventListener(addValueEventListiner());
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                rideMap.put(dataSnapshot.getKey(),dataSnapshot.getValue(Ride.class));
                notifyObservers();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                rideMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(Ride.class));
                notifyObservers();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                rideMap.remove(dataSnapshot.getKey());
                notifyObservers();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("Moved nodeFirebase", "move Child " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Cancelled Read Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        refToRides.addChildEventListener(childEventListener);
    }




    @Override
    public synchronized void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o: observers) {
            o.update(this, getRides());
        }
    }

    @Override
    public synchronized void deleteObservers() {
        observers = new ArrayList<>();
    }

    public Ride getRide(String idRide) {
        return rideMap.get(idRide);
    }

    public List<Ride> getRides() {

        rides= new ArrayList<>();
        for (Map.Entry<String, Ride> rideEntry : rideMap.entrySet()){
            rides.add(rideEntry.getValue());
        }
        return rides;
    }

}