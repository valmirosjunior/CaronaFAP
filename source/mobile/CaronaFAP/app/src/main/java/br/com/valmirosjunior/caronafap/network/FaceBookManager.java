package br.com.valmirosjunior.caronafap.network;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.valmirosjunior.caronafap.model.User;
import br.com.valmirosjunior.caronafap.model.enums.Status;
import br.com.valmirosjunior.caronafap.patners.Observable;
import br.com.valmirosjunior.caronafap.patners.Observer;


/**
 * Created by junior on 10/03/17.
 */

public class FaceBookManager implements Observable {
    private Context context;
    private FirebaseAuth firebaseAuth;
    private static User user;
    private static String fierbaseToken;
    private List<Observer> observers;
    private Status status;

    public FaceBookManager(Activity context){
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        this.observers = new ArrayList<>();
        this.setStatus(Status.NEUTRAL);
    }

    public void prepareLoginButton(LoginButton button,CallbackManager callbackManager){
        button.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));

        button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel(){
                FaceBookManager.this.notifyObservers(Status.CANCELED);
            }

            @Override
            public void onError(FacebookException error) {
                FaceBookManager.this.notifyObservers(Status.ERROR);
            }
        });
    }

    public void logout() {
        if(!this.isLoggedIn()) {
            return;
        }
        try {
            LoginManager.getInstance().logOut();
            firebaseAuth.signOut();
            this.notifyObservers(Status.SUCCESS);
        }catch (Exception e){
            this.notifyObservers(Status.ERROR);
            e.printStackTrace();

        }
    }

    public boolean isLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }

    private static void saveCurrentUserOnSession(){
        Profile profile = Profile.getCurrentProfile();
        User user= new User();
        user.setId(profile.getId());
        user.setName(profile.getName());
        FaceBookManager.user = user;
    }

    public static User getCurrentUser(){
        if(FaceBookManager.user == null) {
            FaceBookManager.saveCurrentUserOnSession();
        }
        return FaceBookManager.user;
    }

    private void setStatus(Status status) {
        this.status = status;
    }


    public void handleFacebookAccessToken(final AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FaceBookManager.this.setStatus(Status.NEUTRAL);
                        if (!task.isSuccessful()) {
                            FaceBookManager.this.notifyObservers(Status.ERROR);
                        }else{
                            fierbaseToken = FirebaseInstanceId .getInstance().getToken();
                            Log.i("token::::;",fierbaseToken);
                            FaceBookManager.this.notifyObservers(Status.SUCCESS);
                        }
                    }
                });
    }

    @Override
    public synchronized void addObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : this.observers) {
            o.update(this,status);
        }
    }


    private void notifyObservers(Object arg) {
        try {
            setStatus((Status) arg);
            notifyObservers();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteObservers() {
        this.observers= new ArrayList<>();
    }
}
