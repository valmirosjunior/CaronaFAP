package br.com.valmirosjunior.caronafap.model;

import java.util.List;

/**
 * Created by junior on 10/04/17.
 */

public class User {
    private String facebookId, name;
    private List<Ride> rides;

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }
}
