package br.com.valmirosjunior.caronafap.model.enums;

/**
 * Created by junior on 10/04/17.
 */

public enum Type {

    ORDERED("ORDERED"),OFFERED("OFFERED");

   private String value;

    /**
     * @param value {@link String}
     */
    Type(String value) {
        this.value=value;
    }

    /*public String getValue() {
        return this.value;
    }
    */
}
