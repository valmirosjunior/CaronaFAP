package br.com.valmirosjunior.caronafap.model;

/**
 * Created by junior on 10/05/17.
 */

public interface Observable {

    public  void addObserver(Observer o) ;

    public  void deleteObserver(Observer o);

    public void deleteObservers();

    public void notifyObservers() ;
}
