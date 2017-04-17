package br.com.valmirosjunior.caronafap.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.valmirosjunior.caronafap.MainActivity;

/**
 * Created by junior on 07/04/17.
 */

public class FireBaseUtil {

    private String TAG = "Firebase Auth";


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public FireBaseUtil() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    public void logout(Context context){
        mAuth.signOut();
        ((MainActivity)context).updateUI(false);
        MessageUtil.hideProgressDialog();
    }

    public void handleFacebookAccessToken(final AccessToken token, final Context context) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        MessageUtil.showProgressDialog(context);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText((Activity) context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                             ((MainActivity)context).updateUI(true);
                        }
                        MessageUtil.hideProgressDialog();
                    }
                });
    }

}
