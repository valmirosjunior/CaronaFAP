package br.com.valmirosjunior.caronafap.model;

/**
 * Created by junior on 16/04/17.
 */

public class MyLocation {
    private double latitude,longitude;
    String idPlace;

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
}
