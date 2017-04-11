package br.com.valmirosjunior.caronafap.model;

/**
 * Created by junior on 28/03/17.
 */

public class Ride {

    public Ride() {

    }

    public Ride(String codigo, String origem, String dedstino) {
        this.codigo = codigo;
        this.origem = origem;
        this.dedstino = dedstino;
    }

    private String codigo,origem, dedstino;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDedstino() {
        return dedstino;
    }

    public void setDedstino(String dedstino) {
        this.dedstino = dedstino;
    }
}
