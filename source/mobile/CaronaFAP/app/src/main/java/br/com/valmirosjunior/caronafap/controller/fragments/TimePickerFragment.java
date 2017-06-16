package br.com.valmirosjunior.caronafap.controller.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import br.com.valmirosjunior.caronafap.R;

/**
 * Created by junior on 12/04/17.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String time = (hourOfDay<10 ? "0"+hourOfDay : hourOfDay)+":"+(minute<10? "0"+minute:minute);
        EditText edit =(EditText) getActivity().findViewById(R.id.editTimeRide);

        edit.setText(time);

        Log.d("datapicker",time);

    }
}