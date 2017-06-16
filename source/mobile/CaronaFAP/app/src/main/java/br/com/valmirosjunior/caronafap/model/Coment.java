package br.com.valmirosjunior.caronafap.model;

import com.google.firebase.database.Exclude;

/**
 * Created by junior on 02/06/17.
 */

public class Coment {
    private String id,idAuthor;
    private float note;
    private User author;
    private String coment;
    private Long timeInMillis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(String idAuthor) {
        this.idAuthor = idAuthor;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }


    @Exclude
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public Long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(Long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }
}
