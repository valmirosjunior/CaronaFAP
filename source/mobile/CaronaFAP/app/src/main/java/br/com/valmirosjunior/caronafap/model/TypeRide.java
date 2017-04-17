package br.com.valmirosjunior.caronafap.model;

/**
 * Created by junior on 10/04/17.
 */

public enum  TypeRide {

    ORDERED ("Ordereds"),
    OFFERED("Offereds");

    private String value;

    private TypeRide(String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
