package br.com.valmirosjunior.caronafap.model.dao;

import android.location.Location;
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
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.network.FaceBookManager;

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
        addChildAddEventListenerToRides();
    }

    public static RideDAO getInstance() {
        if (rideDao == null) {
            rideDao = new RideDAO();
        }
        return rideDao;
    }

    public void saveRide(Ride ride) {
        if (ride.getIdRide() == null) {
            ride.setIdRide(refToRides.push().getKey());
        }
        refToRides.child(ride.getIdRide()).setValue(ride);
    }

    public void removeRide(String idRide) {
        refToRides.child(idRide).removeValue();

    }

    private ValueEventListener valueEventListiner(){
        ValueEventListener rideListener = new ValueEventListener() {
            HashMap<String, Ride> rideMapTemp;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        rideMapTemp = new HashMap<>();
                        for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                            rideMapTemp.put(rideSnapshot.getKey(), rideSnapshot.getValue(Ride.class));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    rideMap = rideMapTemp;
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

    public List<Ride> getMyRides(){
        User user = FaceBookManager.getCurrentUser();
        Ride ride;
        rides= new ArrayList<>();
        for (Map.Entry<String, Ride> rideEntry : rideMap.entrySet()){
            ride= rideEntry.getValue();
            if(ride.getUser().equals(user)){
                rides.add(rideEntry.getValue());
            }
        }
        return rides;

    }

    public List<Ride> getEspecifRides(Ride myRide){
        Location la,lb;
        la = new Location("A");
        lb = new Location("B");

        User user = FaceBookManager.getCurrentUser();
        Ride ride;
        int timeRide,timeMyRide;
        rides= new ArrayList<>();
        for (Map.Entry<String, Ride> rideEntry : rideMap.entrySet()){
            ride= rideEntry.getValue();
            if(!ride.getUser().equals(user)){
                if(ride.getType()== myRide.getType()){
                    timeRide=ride.getHourInMinutes();
                    timeMyRide=myRide.getHourInMinutes();
                    if(timeRide>=(timeMyRide-30) && timeRide <= (timeMyRide+30)){
                        la.setLatitude(ride.getOrigin().getLatitude());
                        la.setLongitude(ride.getDestination().getLongitude());
                        lb.setLatitude(myRide.getOrigin().getLatitude());
                        lb.setLongitude(myRide.getDestination().getLongitude());
                        if(la.distanceTo(lb)< 1000){
                            rides.add(ride);
                        }
                    }
                }
                rides.add(rideEntry.getValue());
            }
        }
        return rides;

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
}