package br.com.valmirosjunior.caronafap.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

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

    public static void showProgressDialog(Context context){
        if (progressDialog == null){
            progressDialog=new ProgressDialog(context);
            progressDialog.setMessage("...........");
        }
        progressDialog.show();
    }

    public static void hideProgressDialog(){
        if (progressDialog != null){
            if(progressDialog.isShowing()) {
                progressDialog.hide();
            }
        }
    }

    public static AlertDialog.Builder createAlertDialogBuilder(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

}
