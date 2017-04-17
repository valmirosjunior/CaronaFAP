package br.com.valmirosjunior.caronafap.model.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.util.Util;

/**
 * Created by junior on 10/04/17.
 */

public class RideDAO {
    private FirebaseDatabase database;
    private DatabaseReference refToRides, refToRegister;

    public RideDAO() {
        database = FirebaseDatabase.getInstance();
        refToRides = database.getReference("Rides");
    }

    public void saveRide(Ride ride){
        refToRegister =refToRides.child(ride.getTypeRide().getValue()).child(ride.getUser().getFacebookId());
        refToRegister.child("Origin").setValue(ride.getOrigin());
        refToRegister.child("Destination").setValue(ride.getDestination());
        refToRegister.child("Distance").setValue(ride.getDistance());
        refToRegister.child("Schedule").setValue(ride.getScheduleRide());
        refToRegister.child("DateEvent").setValue(Util.getDateFormaterFromCalendar(ride.getDateEvent()));
    }

//    public void saveRide(Ride ride){
//        refToRegister= refToRides.child(ride.getTypeRide().getValue()).child(ride.getUser().getFacebookId());
//        refToRegister.child("Origin").setValue(ride.getOrigin());
//        refToRegister.child("Destination").setValue(ride.getDestination());
//        refToRegister.child("DateOrder").setValue(ride.getDateOrder().toString());
//        refToRegister.child("TimeSchedule").setValue(ride.getTimeSchedule().toString());
//    }
}
