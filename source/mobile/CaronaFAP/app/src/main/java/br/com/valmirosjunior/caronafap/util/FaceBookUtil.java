package br.com.valmirosjunior.caronafap.util;

import android.app.Activity;
import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import br.com.valmirosjunior.caronafap.model.User;


/**
 * Created by junior on 10/03/17.
 */

public class FaceBookUtil {
    private Context context;
    private FireBaseUtil fireBaseUtil;

    public FaceBookUtil(Activity context){
        this.context = context;
        fireBaseUtil = new FireBaseUtil();
    }

    public void prepareLoginButton(LoginButton button,CallbackManager callbackManager){
        button.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));


        button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fireBaseUtil.handleFacebookAccessToken(loginResult.getAccessToken(),context);
            }

            @Override
            public void onCancel() {
                MessageUtil.showToast(context,"Login cancelado Facebook");
            }

            @Override
            public void onError(FacebookException error) {
                MessageUtil.showToast(context,"Ocorreu um erro ao tentar efetuar o Login");
            }
        });
    }

    public void disconnectFromFacebook() {
        if(!isLoggedIn()) {
            return;
        }
        try {

            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null,
                    HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    LoginManager.getInstance().logOut();
                    fireBaseUtil.logout(context);
                    MessageUtil.showToast(context,"Você acaba de sair");
                }
            }).executeAsync();

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public boolean isLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void getListFrinds(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/{friend-list-id}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        MessageUtil.showToast(context,"Você está desconectado do facebook");
                    }
                }
        ).executeAsync();
    }

    public User getCurrentUser(){
        Profile profile=Profile.getCurrentProfile();
        User user=new User();
        user.setName(profile.getName());
        user.setFacebookId(profile.getId());
        return user;
    }

    public static String getCurrentProfileId(){
        return Profile.getCurrentProfile().getId();
    }


}
