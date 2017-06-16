package br.com.valmirosjunior.caronafap.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by junior on 10/04/17.
 */

public class Util {
    @NonNull
    public static String getValueStringResource(int stringId){
        return Resources.getSystem().getString(stringId);
    }

    public static int convertStringTimeToSchedule(String time){
        String[] timeSplit = time.split(":");
        int hour, minutes;
        hour = Integer.parseInt(timeSplit[0]);
        minutes = Integer.parseInt(timeSplit[1]);
        return (hour *60) +minutes;
    }

    public static String getDateFormaterFromCalendar (Calendar calendar){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(calendar.getTime());
    }

    public static List convertMaptoList (Map<Object,Object> map){
        ArrayList list = new ArrayList();
        for (Map.Entry<Object, Object> entry : map.entrySet())        {
            list.add(entry.getValue());
        }
        return list;
    }

    public static void seeProfile(Context context,String idUser){
        Intent intent ;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+idUser));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try{
                context.getPackageManager().getPackageInfo("com.facebook.lite", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+idUser));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }catch (Exception ex){
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://facebook.com/"+idUser));
                context.startActivity(intent);
            }
        }

    }


}
