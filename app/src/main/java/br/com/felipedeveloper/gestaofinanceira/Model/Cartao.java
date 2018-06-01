package br.com.felipedeveloper.gestaofinanceira.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe de modelo de Cartao
 */
public class Cartao {
    // variaveis
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

    /**
     * Metodo responsavel por Adiconar credito ao cartao .
     * neste caso somente a atualização do HashMap para ser enviado ao firebase
     * todas as operações com credto de cartao são realizadas por esse metodo
     * @param c Cartaõ que sera atualizado
     * @param v2 Valor a ser adiconado
     * @return Map de retorno para ser persistido no firebase
     */
    @Exclude
    public Map<String, Object> MapCartaoCredito(Cartao c, Double v2) {
        HashMap<String, Object> result = new HashMap<>();
            Double saldoAtualizado = c.getSaldoCartao() + v2; // operacção de soma
            result.put("idcartao", c.getIdcartao());
            result.put("saldoCartao", saldoAtualizado);
            result.put("tituloCartao", c.getTituloCartao());
        return result;
    }


    /**
     * Metodo responsavel por debitar o valor enviado ao cartão e retornar o map para que o mesmo seja salvo no Banco
     * @param c Cartoa a ser debitado
     * @param valor valor do debito
     * @return Map de retorno para ser salvo no firebase
     */
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
