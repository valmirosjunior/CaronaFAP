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

import br.com.valmirosjunior.caronafap.model.Coment;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.network.FaceBookManager;
import br.com.valmirosjunior.caronafap.patners.Observable;
import br.com.valmirosjunior.caronafap.patners.Observer;

/**
 * Created by junior on 04/06/17.
 */

public class ComentDAO implements Observable {
    private static ComentDAO comentDAO;
    private DatabaseReference refToMyComents, refToOtherComent,ref;
    private ChildEventListener childEventListener;
    private List<Observer> observers;
    private HashMap<String,Coment> comentMap, otherComentMap;
    private List<Coment> myComents,otherComents;
    private static User user;
    private static String idOtherUser;


    private ComentDAO() {
        user = FaceBookManager.getCurrentUser();
        refToMyComents = FirebaseFactory.getInstance().getReference("Coments/"+user.getId());
        ref = FirebaseFactory.getInstance().getReference("Coments");
        comentMap = new HashMap<>();
        otherComentMap = new HashMap<>();
        observers = new ArrayList<>();
        addChildAddEventListener(refToMyComents,comentMap);
    }

    public static ComentDAO getInstance() {
        if (comentDAO == null) {
            comentDAO = new ComentDAO();
        }
        return comentDAO;
    }

    public void sendComent(Coment coment){
        if(coment.getId() == null){
            ref = refToOtherComent.child(coment.getUser().getId()).push();
            coment.setId(ref.getKey());
            ref.setValue(coment);
        }else{
            refToOtherComent.child(coment.getUser().getId()).
                    child(coment.getId()).setValue(coment);
        }

    }

    public void removeComent(Coment coment) {
        refToMyComents.child(coment.getUser().getId()).
                child(coment.getId()).removeValue();
    }

    public Coment getComent(String idNotfication) {
        return comentMap.get(idNotfication);
    }

    public void getComent(String idComent, final Observer observer) {
        Coment coment = comentMap.get(idComent);
        if (coment== null){
            refToMyComents.child(idComent).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public List<Coment> getMyComents() {
        myComents = new ArrayList<>();
        for (Map.Entry<String, Coment> notificationEntry : comentMap.entrySet()){
            myComents.add(notificationEntry.getValue());
        }
        return myComents;
    }

    public List<Coment> getOtherComents() {
        otherComents = new ArrayList<>();
        for (Map.Entry<String, Coment> notificationEntry : otherComentMap.entrySet()){
            otherComents.add(notificationEntry.getValue());
        }
        return otherComents;
    }

    public void updateCometsOtherUser(String idUser){
        if(!idUser.equals(idOtherUser)){
            if(idOtherUser != null){
                refToOtherComent.removeEventListener(childEventListener);
            }
            idOtherUser = idUser;
            refToOtherComent = ref.child(idOtherUser);
            addChildAddEventListener(refToOtherComent,otherComentMap);
        }
    }


    @Override
    public void notifyObservers() {
        Type type = null;
        List<Coment> myComents =null,otherComents = null;
        for (Observer observer: observers){
            type = observer.getType();
            if(type == Type.MINE){
                if(myComents == null){
                    myComents = getMyComents();
                }
                observer.update(this,myComents);
            }else{
                if(otherComents == null){
                    otherComents = getOtherComents();
                }
                observer.update(this,otherComents);
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

    private void addChildAddEventListener(DatabaseReference ref, final HashMap<String,Coment> map) {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                map.put(dataSnapshot.getKey(),dataSnapshot.getValue(Coment.class));
                notifyObservers();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                map.put(dataSnapshot.getKey(), dataSnapshot.getValue(Coment.class));
                notifyObservers();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                map.remove(dataSnapshot.getKey());
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
        ref.addChildEventListener(childEventListener);
        ref.keepSynced(true);
    }

}
