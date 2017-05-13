package br.com.valmirosjunior.caronafap.model;

import br.com.valmirosjunior.caronafap.model.enums.Type;

/**
 * Created by junior on 10/05/17.
 */

public interface Observer {

    public void update (Observable observable, Object object);

    public Type getType();
}
