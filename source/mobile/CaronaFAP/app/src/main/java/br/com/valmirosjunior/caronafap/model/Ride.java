package br.com.valmirosjunior.caronafap.model;

import java.util.Date;

/**
 * Created by junior on 28/03/17.
 */

public class Ride {

    private MyLocation origin, destination;
    private Date dateEvent;
    private Schedule scheduleRide;
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

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public Schedule getScheduleRide() {
        return scheduleRide;
    }

    public void setScheduleRide(Schedule scheduleRide) {
        this.scheduleRide = scheduleRide;
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

    @Override
    public String toString() {
        return  user.getName()+" Está " + ((typeRide == TypeRide.ORDERED) ? "Pedindo " : "Oferecendo ")+
                "Carona De : "+origin.getName()+
                " Para : "+destination.getAdress()+
                " no horário  "+scheduleRide.getHour()+":"+scheduleRide.getMinutes()+" Hs";
    }
}
