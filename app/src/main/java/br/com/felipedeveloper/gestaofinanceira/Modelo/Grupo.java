package br.com.felipedeveloper.gestaofinanceira.Modelo;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * classe de modelo utilizada prara criar os Grupos
 */

public class Grupo {
    private String nomeGrupo;
    private Double saldoGrupo;
    List<String> usuarioList;
    private String idGrupo;



    public Grupo() {
    }

    public List<String> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<String> usuarioList) {
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


    /**
     * metodo responsavel por Adiconar dinheiro a grupo
     * neste caso so atualiza o Hash map do grupo com o valor a ser adicionado
     * @param c grupo a ser adiconado
     * @param v2 valor para adiconar
     * @return hash map retornado para ser salvo no Bd
     */
    @Exclude
    public Map<String, Object> mapCreditaGrupo(Grupo c, Double v2) {
        HashMap<String, Object> result = new HashMap<>();
        Double saldoAtualizado = c.getSaldoGrupo() + v2;
        result.put("idGrupo", c.getIdGrupo());
        result.put("saldoGrupo", (saldoAtualizado));
        result.put("nomeGrupo", c.getNomeGrupo());
        result.put("usuarioList", c.getUsuarioList());
        return result;
    }

    /**
     * metodo responsavel por realizar o debito do grupo neste caso atualizar o hash map
     * para que o mesmo possas ser atualizado no Bd
     * operacao de subtração.
     * @param c
     * @param v2
     * @return
     */
    @Exclude
    public Map<String, Object> mapDebitaGrupo(Grupo c, Double v2) {
        HashMap<String, Object> result = new HashMap<>();

        if (v2 > c.getSaldoGrupo()) {
             result = null;
        } else {
            Double saldoAtualizado = c.getSaldoGrupo() - v2;
            result.put("idGrupo", c.getIdGrupo());
            result.put("saldoGrupo", (saldoAtualizado));
            result.put("nomeGrupo", c.getNomeGrupo());
            result.put("usuarioList", c.getUsuarioList());

        }
        return result;
    }
}
