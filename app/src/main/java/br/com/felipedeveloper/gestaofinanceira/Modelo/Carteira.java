package br.com.felipedeveloper.gestaofinanceira.Modelo;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


/**
 * classe de modelo da carteira
 */
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

    /**
     * Adiconando credito ao Hashmap de cartão , neste caso so atualizando o
     * saldo do cartão em questão
     * @param c
     * @param v2
     * @return
     */
    @Exclude
    public Map<String, Object> MapCarteiraCredito(Carteira c, Double v2) {
        HashMap<String, Object> result = new HashMap<>();
        Double saldoAtualizado = c.getSaldoCarteira() + v2; // atualizando valor com a soma
        result.put("idCarteira", c.getIdCarteira());
        result.put("saldoCarteira", saldoAtualizado);
        result.put("tituloCarteira", c.getTituloCarteira());
        return result;
    }

    /**
     * Allef
     * atualizando o valor da carteira atualizando o seu valor realizando a subtração do valor enviado
     * @param c carteira a ser atualizado
     * @param valor valor a ser debitado
     * @return  hash map de carteira que sera retornado e salvo no banco de dados
     */
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
            result.put("saldoCarteira", saldoAtualizado);
            result.put("tituloCarteira", c.getTituloCarteira());

        }
        return result;
    }
}
