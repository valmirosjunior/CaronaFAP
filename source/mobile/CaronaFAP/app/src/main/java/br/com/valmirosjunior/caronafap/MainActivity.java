package br.com.valmirosjunior.caronafap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import br.com.valmirosjunior.caronafap.util.FaceBookUtil;
import br.com.valmirosjunior.caronafap.util.HTTPUtil;
import br.com.valmirosjunior.caronafap.util.MessageUtil;

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    LoginButton loginButton;
    TextView tx;
    FaceBookUtil faceBookUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        faceBookUtil = new FaceBookUtil(this);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        if (faceBookUtil.isLoggedIn()) {
            //startActivity(this, ProfileActivity.class);
            updateUI(true);
        } else {
            callbackManager = CallbackManager.Factory.create();
            tx = (TextView) findViewById(R.id.text);
            faceBookUtil.prepareLoginButton(loginButton, callbackManager);
        }
    }

    public void openRegisterRide(View view) {
        startActivity(new Intent(this, RegisterRide.class));
    }


    public void showRide(View view) {
        startActivity(new Intent(this, ShowRider.class));
    }

    public void logout(View view) {
        MessageUtil.showProgressDialog(this);
        faceBookUtil.disconnectFromFacebook();
    }

    public void exit(View view) {
        logout(view);
        this.finish();
    }

    public void updateUI(boolean logged) {
        int visibilityLoginButton = logged ? View.INVISIBLE : View.VISIBLE;
        int visibilityLayout = logged ? View.VISIBLE : View.INVISIBLE;
        String profileId = logged ? FaceBookUtil.getCurrentProfileId() : null;
        LinearLayout layout = (LinearLayout) findViewById(R.id.LayoutOptionsLogin);

        ProfilePictureView profilePicture = (ProfilePictureView) findViewById(R.id.profilePictureUser);
        profilePicture.setProfileId(profileId);
        loginButton.setEnabled(!logged);
        loginButton.setVisibility(visibilityLoginButton);
        layout.setVisibility(visibilityLayout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            MessageUtil.showToast(this, "Ocorreu erro");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //profileTracker.stopTracking();
    }

    public void request(View view) {
        try {
            new GetPoints().execute("");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private class GetPoints extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            try {
                String url = "https://maps.googleapis.com/maps/api/directions/json?origin=place_id:ChIJh7nL8agiqwcRkFJe5pf4Vzc&destination=place_id:ChIJWQwIca14oQcR_REnbDgiKls&key=AIzaSyAYfYIE7LxDElIo4e8CJwpaEP5ll4QXHtE";

                String resultado = HTTPUtil.doGet(url);
                Log.i("resultado da consulta: ", resultado);

                return new String[]{"opa"};
            } catch (Exception e) {
                return new String[]{e.getMessage()};
            }

        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            dialog.hide();
        }
    }

}
