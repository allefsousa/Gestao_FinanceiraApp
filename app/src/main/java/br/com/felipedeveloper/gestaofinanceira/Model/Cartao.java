package br.com.felipedeveloper.gestaofinanceira.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class Cartao {
    private String idcartao;
    private Double saldoCartao;
    private String tituloCartao;

    public Cartao() {
    }

    public String getIdcartao() {
        return idcartao;
    }

    public void setIdcartao(String idcartao) {
        this.idcartao = idcartao;
    }

    public String getTituloCartao() {
        return tituloCartao;
    }

    public void setTituloCartao(String tituloCartao) {
        this.tituloCartao = tituloCartao;
    }

    public Double getSaldoCartao() {
        return saldoCartao;
    }

    public void setSaldoCartao(Double saldoCartao) {
        this.saldoCartao = saldoCartao;
    }

    @Exclude
    public Map<String, Object> MapCartaoCredito(Cartao c, Double v2) {
        HashMap<String, Object> result = new HashMap<>();
            Double saldoAtualizado = c.getSaldoCartao() + v2;
            result.put("idcartao", c.getIdcartao());
            result.put("saldoCartao", saldoAtualizado);
            result.put("tituloCartao", c.getTituloCartao());
        return result;
    }


    public Map<String, Object> MapcartaoDebito(Cartao c, Double valor) {
        HashMap<String, Object> result = new HashMap<>();

        /**
         * validando valor do saldo da conta nao pode ser menor que o valor a ser debitado enviando erro.
         */
        if (c.getSaldoCartao() < valor) {
            result = null;
        } else {
            Double saldoAtualizado = c.getSaldoCartao() - valor;
            result.put("idcartao", c.getIdcartao());
            result.put("saldoCartao", saldoAtualizado);
            result.put("tituloCartao", c.getTituloCartao());

        }
        return result;
    }
}
