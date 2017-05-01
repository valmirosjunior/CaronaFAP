package br.com.valmirosjunior.caronafap.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.com.valmirosjunior.caronafap.R;

/**
 * Created by junior on 10/03/17.
 */

public class MessageUtil {

    private static ProgressDialog progressDialog;;

    public MessageUtil(){
    }

    public  static void showToast(Context context,String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();

    }

    public static void showCustomToast(Activity activity, String message){
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) activity.findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void showProgressDialog(Context context){
        if (progressDialog == null){
            progressDialog=new ProgressDialog(context);
            progressDialog.setMessage("Loading ....");
        }
        progressDialog.show();
    }

    public static void hideProgressDialog(){
        if(progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    public static AlertDialog.Builder createAlertDialogBuilder(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

}
