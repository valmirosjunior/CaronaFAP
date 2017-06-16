package br.com.valmirosjunior.caronafap.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.controller.activities.ShowOneRideActivity;
import br.com.valmirosjunior.caronafap.model.Ride;
import br.com.valmirosjunior.caronafap.util.Constants;

/**
 * Created by junior on 22/04/17.
 */

public class RideAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    private List<Ride> rides;
    private Context context;
    private TextView textView;
    private ProfilePictureView profilePictureView;

    private static LayoutInflater inflater=null;


    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    public RideAdapter(Context context, List<Ride> rides ) {
        this.rides =rides;
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return rides.size();
    }

    @Override
    public Object getItem(int position) {
        return rides.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Ride ride = rides.get(position);
        View rowView;
        rowView = inflater.inflate(R.layout.row_ride_list, null);
        textView =(TextView) rowView.findViewById(R.id.textViewDescriptoinRide);
        profilePictureView = (ProfilePictureView) rowView.findViewById(R.id.profilePictureUserListView);

        textView.setText(Html.fromHtml(ride.showShortDescrpition()));
        profilePictureView.setProfileId(ride.getUser().getId());
        return rowView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Ride ride = (Ride) getItem(position);
        Intent intent = new Intent(context, ShowOneRideActivity.class);
        intent.putExtra(Constants.ID_RIDE,ride.getId());
        Log.d("Ride", ride.toString());
        context.startActivity(intent);
    }
}
