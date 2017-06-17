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

import br.com.valmirosjunior.caronafap.model.Notification;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.util.FaceBookManager;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;

/**
 * Created by junior on 10/05/17.
 */

public class NotificationDAO implements Observable{

    private static NotificationDAO notificationDAO;
    private DatabaseReference refToNotification,refToSendNotification,ref;
    private List<Observer> observers;
    private HashMap<String,Notification> notificationMap;
    private List<Notification> notifications;
    private static  User user;


    private NotificationDAO() {
        user = FaceBookManager.getCurrentUser();
        refToNotification = FirebaseFactory.getInstance().getReference("Notifcations/"+user.getId());
        refToSendNotification = FirebaseFactory.getInstance().getReference("Notifcations");
        refToNotification.keepSynced(true);
        notificationMap = new HashMap<>();
        observers = new ArrayList<>();
        addChildAddEventListenerToNotifications();
    }

    public static NotificationDAO getInstance() {
        if (notificationDAO == null) {
            notificationDAO = new NotificationDAO();
        }
        return notificationDAO;
    }

    public void sendNotification(Notification notification){
        if(notification.getId() == null){
            ref=refToSendNotification.child(notification.getRide().getUser().getId())
                    .child(notification.getSender().getId());
            notification.setId(ref.getKey());
            ref.setValue(notification);
        }else{
            refToSendNotification.child(notification.getRide().getUser().getId()).
                    child(notification.getId()).setValue(notification);
        }

    }

    public void removeNotifications(Notification notification) {
        refToNotification.child(notification.getRide().getUser().getId()).
                child(notification.getId()).removeValue();
    }

    private void addChildAddEventListenerToNotifications() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                notificationMap.put(dataSnapshot.getKey(),dataSnapshot.getValue(Notification.class));
                notifyObservers();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                notificationMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(Notification.class));
                notifyObservers();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                notificationMap.remove(dataSnapshot.getKey());
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

    public Notification getNotification(String idNotfication) {
        return notificationMap.get(idNotfication);
    }

    public void getNotification(String idNotification, final Observer observer) {
        Notification notification = notificationMap.get(idNotification);
        if (notification== null){
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


    public List<Notification> getNotifications() {
        notifications = new ArrayList<>();
        for (Map.Entry<String, Notification> notificationEntry : notificationMap.entrySet()){
            notifications.add(notificationEntry.getValue());
        }
        return notifications;
    }




    @Override
    public void notifyObservers() {
        Type type = null;
        List<Notification> notifications =null;
        for (Observer observer: observers){
            if(notifications == null) {
                notifications = getNotifications();
                observer.update(this, notifications);
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

