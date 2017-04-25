package br.com.valmirosjunior.caronafap.util;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import br.com.valmirosjunior.caronafap.model.Schedule;

/**
 * Created by junior on 10/04/17.
 */

public class Util {
    @NonNull
    public static String getValueStringResource(int stringId){
        return Resources.getSystem().getString(stringId);
    }

    public static Schedule convertStringTimeToSchedule(String time){
        String[] timeSplit = time.split(":");
        int hour, minutes;
        hour = Integer.parseInt(timeSplit[0]);
        minutes = Integer.parseInt(timeSplit[1]);
        Schedule schedule= new Schedule();
        schedule.setHour(hour);
        schedule.setMinutes(minutes);
        return  schedule;
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

}
