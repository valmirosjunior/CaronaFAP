package br.com.valmirosjunior.caronafap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import br.com.valmirosjunior.caronafap.model.MyLocation;
import br.com.valmirosjunior.caronafap.util.Constants;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MyLocation locationOrigin,locationDestination;
    private MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        locationOrigin = (MyLocation) intent.getSerializableExtra(Constants.ORIGIN_RIDE);
        locationDestination = (MyLocation) intent.getSerializableExtra(Constants.DESTINATION_RIDE);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng origin, destination;
        origin=new LatLng(locationOrigin.getLatitude(),locationOrigin.getLongitude());
        destination=new LatLng(locationDestination.getLatitude(),locationDestination.getLongitude());
        addMakerOrigin(origin);
        addMakerDestination(destination);
        addPolyline(origin,destination);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
    }

    private void addMakerOrigin(LatLng origin){
        addMarker(origin,BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    }

    private void addMakerDestination(LatLng destination){
        addMarker(destination,BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
    }

   private void addMarker(LatLng point, BitmapDescriptor bitmapDescriptor){
       markerOptions = new MarkerOptions();
       markerOptions.position(point);
       markerOptions.title("someTitle");
       markerOptions.snippet("someDesc");
       markerOptions.icon(bitmapDescriptor);
       mMap.addMarker(markerOptions);
   }

   private void addPolyline(LatLng origin, LatLng destination){
       PolylineOptions polylineOptions = new PolylineOptions();
       polylineOptions.add(origin)
               .add(destination);
       mMap.addPolyline(polylineOptions);

   }





}
