package br.com.valmirosjunior.caronafap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.model.Ride;

/**
 * Created by junior on 22/04/17.
 */

public class CustomAdapterRide extends BaseAdapter {
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

    public CustomAdapterRide(Context context, List<Ride> rides ) {
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

        textView.setText(ride.toString());
        profilePictureView.setProfileId(ride.getUser().getId());
        return rowView;
    }


}
