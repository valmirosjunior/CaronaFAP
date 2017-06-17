package br.com.valmirosjunior.caronafap.model;

import com.google.firebase.database.Exclude;

import br.com.valmirosjunior.caronafap.model.enums.Type;

/**
 * Created by junior on 10/05/17.
 */

public class Solicitation {
    private String id;
    private Ride ride;
    private User sender;
    private String idRide;
    private Type send, receive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        if(ride != null){
            setIdRide(ride.getId());
        }
        this.ride = ride;
    }


    @Exclude
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getIdRide() {
        return idRide;
    }

    public void setIdRide(String idRideReceiver) {
        this.idRide = idRideReceiver;
    }




    public Type getSend() {
        return send;
    }

    public void setSend(Type send) {
        this.send = send;
    }

    public Type getReceive() {
        return receive;
    }

    public void setReceive(Type receive) {
        this.receive = receive;
    }

    @Override
    public String toString() {
        return sender.getName()+ "\n"+((send == Type.REQUEST)? "Enviou uma solicitação" :
                ((send == Type.CONFIRM)? "Aceitou sua solicitação" :"Rejeitou sua  Solicitação"));
    }
}
