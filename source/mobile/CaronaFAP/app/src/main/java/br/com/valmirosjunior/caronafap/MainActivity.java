package br.com.valmirosjunior.caronafap;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import br.com.valmirosjunior.caronafap.model.TypeRide;
import br.com.valmirosjunior.caronafap.util.Constants;
import br.com.valmirosjunior.caronafap.util.FaceBookUtil;
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
        faceBookUtil=new FaceBookUtil(this);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        if(faceBookUtil.isLoggedIn()){
            updateUI(true);
        }else {
            callbackManager = CallbackManager.Factory.create();
            tx = (TextView) findViewById(R.id.text);
            faceBookUtil.prepareLoginButton(loginButton, callbackManager);
        }
    }

    public void askRide(View view){
        OpenRegisterRide(TypeRide.ORDERED);
    }


    public void offerRide(View view){
        OpenRegisterRide(TypeRide.OFFERED);
    }

    private void OpenRegisterRide(TypeRide typeRide){
        Intent intent =new Intent(this,RegisterRide.class);
        intent.putExtra(Constants.TYPE_RIDE_EXTRA,typeRide.ordinal());
        startActivity(intent);
    }

    public void showRide(View view){
        startActivity(new Intent(this,ShowRider.class));
    }

    public void logout(View view){
        MessageUtil.showProgressDialog(this);
        faceBookUtil.disconnectFromFacebook();
    }

    public void exit (View view){
        logout(view);
        this.finish();
    }

    public void updateUI(boolean logged){
        int visibilityLoginButton = logged ? View.INVISIBLE : View.VISIBLE;
        int visibilityLayout = logged ?  View.VISIBLE : View.INVISIBLE;
        String profileId= logged ? FaceBookUtil.getCurrentProfileId() : null;
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }
}
