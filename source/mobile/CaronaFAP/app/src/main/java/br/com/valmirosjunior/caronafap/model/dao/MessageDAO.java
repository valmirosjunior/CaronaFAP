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

import br.com.valmirosjunior.caronafap.model.Message;
import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.network.FaceBookManager;
import br.com.valmirosjunior.caronafap.patners.Observable;
import br.com.valmirosjunior.caronafap.patners.Observer;

/**
 * Created by junior on 04/06/17.
 */

public class MessageDAO implements Observable{
    private DatabaseReference refToMessages;
    private List<Observer> observers;
    private HashMap<String,Message> messageMap;
    private List<Message> messages;
    private Message message;
    private static MessageDAO messageDAO;


    private MessageDAO() {
        refToMessages = FirebaseFactory.getInstance().getReference("Messages");
        refToMessages.keepSynced(true);
        messageMap = new HashMap<>();
        observers = new ArrayList<>();
        addChildAddEventListenerToRides();
    }

    public static MessageDAO getInstance() {
        if (messageDAO == null) {
            messageDAO = new MessageDAO();
        }
        return messageDAO;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void saveMessage(Message message) {
        if (message.getId() == null) {
            message.setId(refToMessages.push().getKey());
        }
        refToMessages.child(message.getId()).setValue(message);
    }

    public void removeMessage(String idMessage) {
        refToMessages.child(idMessage).removeValue();

    }

    private void addChildAddEventListenerToRides() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                messageMap.put(dataSnapshot.getKey(),dataSnapshot.getValue(Message.class));
                notifyObservers();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                messageMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(Message.class));
                notifyObservers();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                messageMap.remove(dataSnapshot.getKey());
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
        refToMessages.addChildEventListener(childEventListener);
    }

    public Message getRide(String idMessage) {
        return messageMap.get(idMessage);
    }

    public void getMessage(String idMessage, final Observer observer) {
        Message message = messageMap.get(idMessage);
        if (message== null){
            refToMessages.child(idMessage).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    observer.update(dataSnapshot.getValue(Ride.class));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    observer.update(null);
                }
            });
        }else{
            observer.update(message);
        }
    }

    public List<Message> getMessages() {
        messages = new ArrayList<>();
        for (Map.Entry<String, Message> rideEntry : messageMap.entrySet()){
            messages.add(rideEntry.getValue());
        }
        return messages;
    }

    public List<Message> getMyMessages(){
        User user = FaceBookManager.getCurrentUser();
        Message message;
        messages = new ArrayList<>();
        for (Map.Entry<String, Message> rideEntry : messageMap.entrySet()){
            message= rideEntry.getValue();
        }
        return messages;

    }



    @Override
    public void notifyObservers() {
        for (Observer observer: observers){
                observer.update(this);
        }
    }

    @Override
    public void deleteObservers() {
        observers = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer o) {
        if (!observers.contains(o)){
            observers.add(o);
        }

    }

    @Override
    public void deleteObserver(Observer o) {
        observers.remove(o);
    }

}
