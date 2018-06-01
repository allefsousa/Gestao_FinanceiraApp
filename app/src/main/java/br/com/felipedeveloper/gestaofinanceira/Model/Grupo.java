package br.com.felipedeveloper.gestaofinanceira.Model;

import com.google.firebase.database.Exclude;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by allef on 09/05/2018.
 */

public class Grupo {
    private String nomeGrupo;
    private Double saldoGrupo;
    private List<Usuario> usuarioList;
    private String idGrupo;



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

    public Double getSaldoGrupo() {
        return saldoGrupo;
    }

    public void setSaldoGrupo(Double saldoGrupo) {
        this.saldoGrupo = saldoGrupo;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    @Exclude
    public Map<String, Object> mapCreditaGrupo(Grupo c, Double v2) {
        DecimalFormat df = new DecimalFormat("#,###,##");
        HashMap<String, Object> result = new HashMap<>();
        Double saldoAtualizado = c.getSaldoGrupo() + v2;
        result.put("idGrupo", c.getIdGrupo());
        result.put("saldoGrupo", df.format(saldoAtualizado));
        result.put("nomeGrupo", c.getNomeGrupo());
        result.put("usuarioList", c.getUsuarioList());
        return result;
    }
    @Exclude
    public Map<String, Object> mapDebitaGrupo(Grupo c, Double v2) {
        HashMap<String, Object> result = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#,###,##");
        if (v2 > c.getSaldoGrupo()) {
             result = null;
        } else {
            Double saldoAtualizado = c.getSaldoGrupo() - v2;
            result.put("idGrupo", c.getIdGrupo());
            result.put("saldoGrupo", df.format(saldoAtualizado));
            result.put("nomeGrupo", c.getNomeGrupo());
            result.put("usuarioList", c.getUsuarioList());

        }
        return result;
    }
}
