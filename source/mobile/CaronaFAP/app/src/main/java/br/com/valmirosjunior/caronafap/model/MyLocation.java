package br.com.valmirosjunior.caronafap.model;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by junior on 16/04/17.
 */

public class MyLocation implements Serializable{

    private double latitude,longitude;
    private String idPlace, name, adress;

    public double getLatitude() {

        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }


    public double distanceToLocation(MyLocation otherLocation){
        Location la,lb;
        la= new Location("this");
        lb = new Location("other");
        la.setLatitude(latitude);
        la.setLongitude(longitude);
        lb.setLatitude(otherLocation.latitude);
        lb.setLongitude(otherLocation.longitude);
        return la.distanceTo(lb);
    }

    public String shortAdress(){
        String fields[]= adress.split(",");
        String result =" ";
        for(int i=0;i<fields.length -2 ; i++){
            result += fields[i]+", ";
        }
        return result.substring(0,result.length()-2);
    }


}
