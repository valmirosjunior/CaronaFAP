package br.com.valmirosjunior.caronafap.controller.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.pattern.Observable;
import br.com.valmirosjunior.caronafap.pattern.Observer;
import br.com.valmirosjunior.caronafap.model.enums.Status;
import br.com.valmirosjunior.caronafap.model.enums.Type;
import br.com.valmirosjunior.caronafap.util.FaceBookManager;
import br.com.valmirosjunior.caronafap.util.MessageUtil;

public class MainActivity extends AppCompatActivity implements Observer {
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private FaceBookManager faceBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        faceBookManager = new FaceBookManager(this);
        faceBookManager.addObserver(this);
        if (faceBookManager.isLoggedIn()) {
            startActivity(new Intent(this,ProfileUserActivity.class));
            this.finish();
        } else {
            loginButton = (LoginButton) findViewById(R.id.login_button);
            callbackManager = CallbackManager.Factory.create();
            faceBookManager.prepareLoginButton(loginButton, callbackManager);
        }
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
    public void update(Object object) {

    }

    @Override
    public void update(Observable observable, Object o) {
        Status status;
        AlertDialog.Builder buider = MessageUtil.createAlertDialogBuilder(this);
        try {
            status = (Status) o;
            switch (status){

                case SUCCESS:
                    buider.setTitle(R.string.success);
                    buider.setMessage(R.string.login_success);
                    buider.show();
                    faceBookManager.deleteObserver(this);
                    startActivity(new Intent(this, ProfileUserActivity.class));
                    this.finish();
                    break;

                case CANCELED:
                    buider.setTitle(R.string.canceled_operation);
                    buider.setMessage(R.string.login_canceled);
                    buider.setNeutralButton(R.string.ok,null);
                    buider.show();
                    break;

                case ERROR:
                    buider.setTitle(R.string.error);
                    buider.setMessage(R.string.internal_error);
                    buider.show();

            }
        }catch (Exception e){
            buider.setTitle(R.string.error);
            buider.setMessage(R.string.internal_error);
            buider.show();
        }
    }

    @Override
    public Type getType() {
        return null;
    }
}
