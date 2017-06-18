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

import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.model.Solicitation;
import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.util.FaceBookManager;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;

/**
 * Created by junior on 10/05/17.
 */

public class SolicitationDAO implements Observable,Observer{

    private static SolicitationDAO solicitationDAO;
    private DatabaseReference refToSolitations,ref;
    private List<Observer> observers;
    private HashMap<String,Solicitation> solicitationMap,auxMap;
    private List<Solicitation> solicitations;
    private Solicitation solicitation;
    private RideDAO rideDAO;
    private UserDAO userDAO;
    private static  User user;


    private SolicitationDAO() {
        user = FaceBookManager.getCurrentUser();
        refToSolitations = FirebaseFactory.getInstance().getReference("Solicitations");
        refToSolitations.keepSynced(true);
        solicitationMap = new HashMap<>();
        auxMap = new HashMap<>();
        observers = new ArrayList<>();
        rideDAO = RideDAO.getInstance();
        userDAO = UserDAO.getInstance();
        addValueEventListiner(refToSolitations);
    }

    public static SolicitationDAO getInstance() {
        if (solicitationDAO == null) {
            solicitationDAO = new SolicitationDAO();
        }
        return solicitationDAO;
    }

    public void sendSolicitation(Solicitation solicitation){
        if(solicitation.getId() == null){
            ref= refToSolitations.push();
            solicitation.setId(ref.getKey());
            ref.setValue(solicitation);
        }else{
            refToSolitations.child(solicitation.getId()).setValue(solicitation);
        }
    }

    private void updateMapSolicitation(DataSnapshot dataSnapshot){
        try {
            solicitation = dataSnapshot.getValue(Solicitation.class);
            Ride ride = rideDAO.getRide(solicitation.getIdRide());
            User user = userDAO.getUser(solicitation.getIdSender());
            if (ride == null || user == null) {
                auxMap.put(dataSnapshot.getKey(), solicitation);
            }
            solicitation.setSender(user);
            solicitation.setRide(ride);
            solicitationMap.put(dataSnapshot.getKey(), solicitation);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addValueEventListiner(final DatabaseReference ref){
        final ValueEventListener valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        solicitationMap = new HashMap<>();
                        for (DataSnapshot solitationSnapshot : dataSnapshot.getChildren()) {
                            updateMapSolicitation(solitationSnapshot);
                        }
                        notifyObservers();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Cancelled Read Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(valueListener);
    }

    private void addChildAddEventListenerToNotifications() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateMapSolicitation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateMapSolicitation(dataSnapshot);
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
        refToSolitations.addChildEventListener(childEventListener);
    }

    public Solicitation getSolicitation(String idNotfication) {
        return solicitationMap.get(idNotfication);
    }

    public List<Solicitation> getSolicitations() {
        solicitations = new ArrayList<>();
        for (Map.Entry<String, Solicitation> notificationEntry : solicitationMap.entrySet()){
            solicitations.add(notificationEntry.getValue());
        }
        return solicitations;
    }

    public List<Solicitation> getSolicitationsReceived(){
        User user = FaceBookManager.getCurrentUser();
        solicitations= new ArrayList<>();
        for (Map.Entry<String, Solicitation> solicitationEntry : solicitationMap.entrySet()){
            solicitation= solicitationEntry.getValue();
            if(user.equals(solicitation.getRide().getUser())){
                solicitations.add(solicitationEntry.getValue());
            }
        }
        return solicitations;

    }

    public List<Solicitation> getSolicitationsSended(){
        User user = FaceBookManager.getCurrentUser();
        solicitations= new ArrayList<>();
        for (Map.Entry<String, Solicitation> solicitationEntry : solicitationMap.entrySet()){
            solicitation= solicitationEntry.getValue();
            if(user.equals(solicitation.getSender())){
                solicitations.add(solicitationEntry.getValue());
            }
        }
        return solicitations;

    }




    @Override
    public void notifyObservers() {
        if(auxMap.size()>0){
            rideDAO.addObserver(this);
            rideDAO.notifyObservers();
            userDAO.addObserver(this);
            userDAO.notifyObservers();
        }else{
            Type type = null;
            List<Solicitation> send = null,receive = null;
            for (Observer observer: observers){
                if(solicitations == null) {
                    solicitations = getSolicitations();
                    observer.update(this, solicitations);
                }
                type = observer.getType();
                if(type == Type.SEND){
                    if( send == null){
                        send = getSolicitationsSended();
                    }
                    observer.update(this,send);
                } else {
                    if (receive == null){
                        receive = getSolicitationsReceived();
                    }
                    observer.update(this,receive);
                }
            }
        }
    }

    @Override
    public void deleteObservers() {
        observers = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer o) {
        if(!observers.contains(o)) {
            observers.add(o);
            notifyObservers();
        }

    }

    @Override
    public void deleteObserver(Observer o) {
        if (observers.contains(o)){
            observers.remove(o);
        }
    }

    @Override
    public void update(Object object) {
        Ride ride;
        User user;
        for (Map.Entry<String, Solicitation> solicitationEntry : auxMap.entrySet()){
            ride = rideDAO.getRide(solicitationEntry.getValue().getIdRide());
            user = userDAO.getUser(solicitationEntry.getValue().getIdSender());
            if(ride!= null){
                solicitationEntry.getValue().setRide(ride);
                return;
            }
            if(user!= null){
                solicitationEntry.getValue().setSender(user);
                return;
            }
            auxMap.remove(solicitationEntry.getKey());
        }
        notifyObservers();
    }

    @Override
    public void update(Observable observable, Object object) {
        update(object);
    }

    @Override
    public Type getType() {
        return null;
    }
}

