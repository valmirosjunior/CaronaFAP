package br.com.valmirosjunior.caronafap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;

import br.com.valmirosjunior.caronafap.util.DialogsdMessages;
import br.com.valmirosjunior.caronafap.util.FaceBookUtil;

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

        faceBookUtil=new FaceBookUtil(this);

        if(faceBookUtil.estaLogado()){
            //startActivity(new Intent(this,PedirCarona.class));
            DialogsdMessages.showCustomToast(this,"opa já está logado");
        }else {

            callbackManager = CallbackManager.Factory.create();
            loginButton = (LoginButton) findViewById(R.id.login_button);
            tx = (TextView) findViewById(R.id.text);

            faceBookUtil.prepareLoginButton(loginButton, callbackManager);
        }
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
