package br.com.felipedeveloper.gestaofinanceira.Model;

import java.util.List;

/**
 * Created by allef on 09/05/2018.
 */

public class Grupo {
    private String nomeGrupo;
    private String saldoGrupo;
    private List<Usuario> usuarioList;



    public Grupo() {
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
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
