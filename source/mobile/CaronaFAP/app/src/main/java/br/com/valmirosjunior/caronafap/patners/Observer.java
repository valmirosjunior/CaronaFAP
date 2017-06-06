package br.com.valmirosjunior.caronafap.patners;

import br.com.valmirosjunior.caronafap.model.enums.Type;

/**
 * Created by junior on 10/05/17.
 */

public interface Observer {

    public void update (Object object);

    public void update (Observable observable, Object object);

    public Type getType();
}
