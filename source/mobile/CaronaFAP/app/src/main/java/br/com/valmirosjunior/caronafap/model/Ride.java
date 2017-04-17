package br.com.valmirosjunior.caronafap.model;

import java.util.Calendar;

/**
 * Created by junior on 28/03/17.
 */

public class Ride {

    private MyLocation origin, destination;
    private Calendar dateEvent;
    private Schedule scheduleRide;
    private long distance;
    private User user;
    private TypeRide typeRide;

    public Ride() {
    }

    public MyLocation getOrigin() {
        return origin;
    }

    public void setOrigin(MyLocation origin) {
        this.origin = origin;
    }

    public MyLocation getDestination() {
        return destination;
    }

    public void setDestination(MyLocation destination) {
        this.destination = destination;
    }

    public Calendar getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Calendar dateEvent) {
        this.dateEvent = dateEvent;
    }

    public Schedule getScheduleRide() {
        return scheduleRide;
    }

    public void setScheduleRide(Schedule scheduleRide) {
        this.scheduleRide = scheduleRide;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TypeRide getTypeRide() {
        return typeRide;
    }

    public void setTypeRide(TypeRide typeRide) {
        this.typeRide = typeRide;
    }
}
