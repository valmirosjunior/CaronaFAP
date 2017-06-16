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

import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;

/**
 * Created by junior on 02/06/17.
 */

public class UserDAO implements Observable {
    private DatabaseReference refToUsers;
    private List<Observer> observers;
    private HashMap<String,User> userMap;
    private List<User> users;
    private User user;
    private static UserDAO userDAO;


    private UserDAO() {
        refToUsers = FirebaseFactory.getInstance().getReference("Users");
        refToUsers.keepSynced(true);
        userMap = new HashMap<>();
        users = new ArrayList<>();
        observers = new ArrayList<>();
        addChildAddEventListener();
    }

    public static UserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void saveUser(User user) {
        if (userMap.get(user.getId()) != null){
            Map<String,Object> nameMap = new HashMap<>();
            nameMap.put("name",user.getName());
            refToUsers.child(user.getId()).updateChildren(nameMap);
        }else{
            refToUsers.child(user.getId()).setValue(user);
        }
    }

    private void addChildAddEventListener() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                user = dataSnapshot.getValue(User.class);
                userMap.put(dataSnapshot.getKey(),user);
                users.add(user);
                notifyObservers();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                user = dataSnapshot.getValue(User.class);
                userMap.put(dataSnapshot.getKey(),user);
                users.add(user);
                notifyObservers();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                userMap.remove(dataSnapshot.getKey());
                users.remove(user);
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
        refToUsers.addChildEventListener(childEventListener);
    }

    public User getUser(String idUser) {
        return userMap.get(idUser);
    }

    public void getUser(String idUser, final Observer observer) {
        User user = userMap.get(idUser);
        if (user== null){
            refToUsers.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public List<User> getUsers() {
        return users;
    }

    private void updateListUser(DataSnapshot dataSnapshot){
        user = dataSnapshot.getValue(User.class);
        userMap.put(dataSnapshot.getKey(),user);
        users.add(user);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer: observers)
                observer.update(this);

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
