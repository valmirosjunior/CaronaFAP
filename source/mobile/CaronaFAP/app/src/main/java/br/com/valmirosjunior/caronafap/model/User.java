package br.com.valmirosjunior.caronafap.model;

import java.util.List;
import java.util.Map;

/**
 * Created by junior on 10/04/17.
 */

public class User {
    private String id, name;
    private List<Map<String,String>> conversations;
    private List<Coment> coments;
    private List<String> solitationsId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Coment> getComents() {
        return coments;
    }

    public void setComents(List<Coment> coments) {
        this.coments = coments;
    }

    public List<String> getSolitationsId() {
        return solitationsId;
    }

    public void setSolitationsId(List<String> solitationsId) {
        this.solitationsId = solitationsId;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User){
            User otherUser =(User) obj;
            if (this.getId().equals(otherUser.getId())){
                return true;
            }
        }
        return false;
    }
}
