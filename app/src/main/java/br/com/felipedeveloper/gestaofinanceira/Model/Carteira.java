package br.com.felipedeveloper.gestaofinanceira.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;



public class Carteira {
    private String idCarteira;
    private Double SaldoCarteira;
    private String tituloCarteira;

    public String getIdCarteira() {
        return idCarteira;
    }

    public void setIdCarteira(String idCarteira) {
        this.idCarteira = idCarteira;
    }

    public Double getSaldoCarteira() {
        return SaldoCarteira;
    }

    public void setSaldoCarteira(Double saldoCarteira) {
        SaldoCarteira = saldoCarteira;
    }

    public String getTituloCarteira() {
        return tituloCarteira;
    }

    public void setTituloCarteira(String tituloCarteira) {
        this.tituloCarteira = tituloCarteira;
    }

    @Exclude
    public Map<String, Object> MapCarteiraCredito(Carteira c, Double v2) {
        HashMap<String, Object> result = new HashMap<>();
        Double saldoAtualizado = c.getSaldoCarteira() + v2;
        result.put("idCarteira", c.getIdCarteira());
        result.put("SaldoCarteira", saldoAtualizado);
        result.put("tituloCarteira", c.getTituloCarteira());
        return result;
    }


    public Map<String, Object> MapCarteiraDebito(Carteira c, Double valor) {
        HashMap<String, Object> result = new HashMap<>();

        /**
         * validando valor do saldo da conta nao pode ser menor que o valor a ser debitado enviando erro.
         */
        if (c.getSaldoCarteira() < valor) {
            result = null;
        } else {
            Double saldoAtualizado = c.getSaldoCarteira() - valor;
            result.put("idCarteira", c.getIdCarteira());
            result.put("SaldoCarteira", saldoAtualizado);
            result.put("tituloCarteira", c.getTituloCarteira());

        }
        return result;
    }
}
