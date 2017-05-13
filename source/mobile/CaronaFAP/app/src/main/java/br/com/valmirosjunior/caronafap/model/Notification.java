package br.com.valmirosjunior.caronafap.model;

import br.com.valmirosjunior.caronafap.model.enums.Type;

/**
 * Created by junior on 10/05/17.
 */

public class Notification {
    private String idNotification;
    private User sender,receiver;
    private String idRide;
    private Type type;

    public String getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(String idNotification) {
        this.idNotification = idNotification;
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

    public void setIdRide(String idRide) {
        this.idRide = idRide;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return sender.getName()+ "\n"+((type == Type.REQUEST)? "Enviou uma solicitação" : "Ofereceu uma carona");
    }
}
