package br.com.valmirosjunior.caronafap.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import br.com.valmirosjunior.caronafap.AskRide;


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
                context.startActivity(new Intent(context, AskRide.class));
                fireBaseUtil.handleFacebookAccessToken(loginResult.getAccessToken(),context);
                MessageUtil.showToast(context,"Só sucesso");
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
        if(!estaLogado())
            return;
        try {

            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    LoginManager.getInstance().logOut();
                    MessageUtil.showToast(context,"Você acaba de sair do Facebook");
                }
            }).executeAsync();

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public boolean estaLogado(){
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
                    }
                }
        ).executeAsync();
    }

}
