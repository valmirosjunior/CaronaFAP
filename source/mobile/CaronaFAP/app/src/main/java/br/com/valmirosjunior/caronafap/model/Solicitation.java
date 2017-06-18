package br.com.valmirosjunior.caronafap.model;

import com.google.firebase.database.Exclude;

import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.util.FaceBookManager;

/**
 * Created by junior on 10/05/17.
 */

public class Solicitation {
    private String id,idSender;
    private Ride ride;
    private User sender;
    private String idRide;
    private Type messageSended, messageResponse;

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

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    @Exclude
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        if(sender != null){
            setIdSender(sender.getId());
        }
        this.sender = sender;
    }

    public String getIdRide() {
        return idRide;
    }

    public void setIdRide(String idRideReceiver) {
        this.idRide = idRideReceiver;
    }



    public Type getMessageSended() {
        return messageSended;
    }

    public void setMessageSended(Type messageSended) {
        this.messageSended = messageSended;
    }

    public Type getMessageResponse() {
        return messageResponse;
    }

    public void setMessageResponse(Type messageResponse) {
        this.messageResponse = messageResponse;
    }

    @Override
    public String toString() {
        return  "Solitation "+id;
//        return sender.getName()+ "\n"+((messageSended == Type.REQUEST)? "Enviou uma solicitação" :
//                ((messageSended == Type.CONFIRM)? "Aceitou sua solicitação" :"Rejeitou sua  Solicitação"));
    }

    private boolean isMine(){
        return FaceBookManager.getCurrentUser().equals(getSender());
    }

    public String showDescription() {
        String senderName = isMine()? "Você": getSender().getName();
        String receiveName = !senderName.equals("Você")? "Você" : getRide().getUser().getName();
        return  "<strong>"+senderName+"</strong> "+
                "Enviou Uma solicitação!</strong> <br><strong>" +
                 ((messageResponse == null) ? "No entanto "+receiveName+" ainda não respondeu" :
                  ((messageResponse == Type.REJECT) ?
                                 "Porém "+ receiveName+" Não aceitou!" : receiveName+" Confirmou a solicitação!"))
                +"</strong>";

    }
}
