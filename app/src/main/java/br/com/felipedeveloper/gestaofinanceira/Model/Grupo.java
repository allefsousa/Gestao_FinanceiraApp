package br.com.felipedeveloper.gestaofinanceira.Model;

/**
 * Created by allef on 09/05/2018.
 */

public class Grupo {
    private String nomeGrupo;
    private String saldoGrupo;


    public Grupo() {
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    public String getSaldoGrupo() {
        return saldoGrupo;
    }

    public void setSaldoGrupo(String saldoGrupo) {
        this.saldoGrupo = saldoGrupo;
    }
}
