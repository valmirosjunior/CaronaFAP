package br.com.valmirosjunior.caronafap.model.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.valmirosjunior.caronafap.model.Solicitation;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.util.FaceBookManager;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;

/**
 * Created by junior on 10/05/17.
 */

public class SolicitationDAO implements Observable{

    private static SolicitationDAO solicitationDAO;
    private DatabaseReference refToNotification,refToSendNotification,ref;
    private List<Observer> observers;
    private HashMap<String,Solicitation> solicitationMap;
    private List<Solicitation> solicitations;
    private static  User user;


    private SolicitationDAO() {
        user = FaceBookManager.getCurrentUser();
        refToNotification = FirebaseFactory.getInstance().getReference("Solicitations/"+user.getId());
        refToSendNotification = FirebaseFactory.getInstance().getReference("Solicitations");
        refToNotification.keepSynced(true);
        solicitationMap = new HashMap<>();
        observers = new ArrayList<>();
        addChildAddEventListenerToNotifications();
    }

    public static SolicitationDAO getInstance() {
        if (solicitationDAO == null) {
            solicitationDAO = new SolicitationDAO();
        }
        return solicitationDAO;
    }

    public void sendNotification(Solicitation solicitation){
        User receiver = solicitation.getRide().getUser();
        User sender = solicitation.getSender();
        if(solicitation.getId() == null){
            ref=refToSendNotification.child(receiver.getId())
                    .child(sender.getId());
            solicitation.setId(ref.getKey());
            ref.setValue(solicitation);
        }else{
            refToSendNotification.child(solicitation.getRide().getUser().getId()).
                    child(solicitation.getId()).setValue(solicitation);
        }
        UserDAO.getInstance().addSolicitation(sender,receiver.getId());

    }

    public void removeNotifications(Solicitation solicitation) {
        refToNotification.child(solicitation.getRide().getUser().getId()).
                child(solicitation.getId()).removeValue();
    }

    private void addChildAddEventListenerToNotifications() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                solicitationMap.put(dataSnapshot.getKey(),dataSnapshot.getValue(Solicitation.class));
                notifyObservers();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                solicitationMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(Solicitation.class));
                notifyObservers();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                solicitationMap.remove(dataSnapshot.getKey());
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
        refToNotification.addChildEventListener(childEventListener);
    }

    public Solicitation getNotification(String idNotfication) {
        return solicitationMap.get(idNotfication);
    }

    public void getNotification(String idNotification, final Observer observer) {
        Solicitation solicitation = solicitationMap.get(idNotification);
        if (solicitation == null){
            refToNotification.child(idNotification).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    observer.update(dataSnapshot.getValue(User.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    observer.update(null);
                }
            });
        }else{
            observer.update(user);
        }
    }


    public List<Solicitation> getSolicitations() {
        solicitations = new ArrayList<>();
        for (Map.Entry<String, Solicitation> notificationEntry : solicitationMap.entrySet()){
            solicitations.add(notificationEntry.getValue());
        }
        return solicitations;
    }




    @Override
    public void notifyObservers() {
        Type type = null;
        List<Solicitation> solicitations =null;
        for (Observer observer: observers){
            if(solicitations == null) {
                solicitations = getSolicitations();
                observer.update(this, solicitations);
            }
        }
    }

    public synchronized void deleteObservers() {
        observers = new ArrayList<>();
    }

    public void addObserver(Observer o) {
        if(!observers.contains(o)) {
            observers.add(o);
            notifyObservers();
        }

    }

    public void deleteObserver(Observer o) {
        if (observers.contains(o)){
            observers.remove(o);
        }
    }

}

