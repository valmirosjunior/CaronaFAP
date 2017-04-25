package br.com.valmirosjunior.caronafap.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.valmirosjunior.caronafap.MapsActivity;
import br.com.valmirosjunior.caronafap.R;
import br.com.valmirosjunior.caronafap.model.Ride;

/**
 * Created by junior on 22/04/17.
 */

public class CustomAdapterRide extends BaseAdapter {
    private ArrayList<Ride> rides;
    private Context context;
    private TextView tv;
    private ProfilePictureView profilePictureView;

    private static LayoutInflater inflater=null;

    public ArrayList<Ride> getRides() {
        return rides;
    }

    public void setRides(ArrayList<Ride> rides) {
        this.rides = rides;
    }

    public void setRidesFromMap(HashMap<String,Ride> mapRides) {
        setRides(convertMaptoList(mapRides));
    }

    public CustomAdapterRide(Context context,ArrayList<Ride> rides ) {
        this.rides =rides;
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CustomAdapterRide(Context context) {
        this(context,new ArrayList<Ride>());
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
        tv =(TextView) rowView.findViewById(R.id.textViewDescriptoinRide);
        profilePictureView = (ProfilePictureView) rowView.findViewById(R.id.profilePictureUserListView);

        tv.setText(ride.toString());
        profilePictureView.setProfileId(ride.getUser().getFacebookId());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra(Constants.ORIGIN_RIDE,ride.getOrigin());
                intent.putExtra(Constants.DESTINATION_RIDE,ride.getDestination());
                context.startActivity(intent);
            }
        });
        return rowView;
    }

    private ArrayList<Ride> convertMaptoList (HashMap<String,Ride> map){
        ArrayList<Ride> list = new ArrayList();
        for (HashMap.Entry entry : map.entrySet())        {
            list.add((Ride) entry.getValue());
        }
        return list;
    }
}
