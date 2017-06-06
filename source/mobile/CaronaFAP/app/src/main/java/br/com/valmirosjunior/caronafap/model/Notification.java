package br.com.valmirosjunior.caronafap.model;

import br.com.valmirosjunior.caronafap.model.enums.Type;

/**
 * Created by junior on 10/05/17.
 */

public class Notification {
    private String id;
    private User sender,receiver;
    private String idRide;
    private Type send, receive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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
