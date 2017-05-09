package br.com.valmirosjunior.caronafap.model.enums;

/**
 * Created by junior on 02/05/17.
 */

public enum Status {
    CANCELED(-2),
    ERROR(-1),
    NEUTRAL(0),
    SUCCESS(1),
    REGISTERED(2),
    RESOLVED(3),
    READED(4),
    SENT(5);

    private int value;

    /**
     * @param value {@link String}
     */
    Status (int value){
        this.value =value;
    }

    public int getValue(){
        return value;
    }

}
