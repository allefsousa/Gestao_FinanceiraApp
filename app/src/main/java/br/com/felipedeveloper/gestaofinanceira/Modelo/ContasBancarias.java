package br.com.felipedeveloper.gestaofinanceira.Modelo;

import java.util.HashMap;
import java.util.Map;


/**
 * classe de modelo da conta bancaria
 */
public class ContasBancarias {
    private String idContaBanco;
    private String tituloContabanco;
    private Double saldoContabancaria;


    public Double getSaldoContabancaria() {
        return saldoContabancaria;
    }

    public ContasBancarias() {
    }

    public String getIdContaBanco() {
        return idContaBanco;
    }

    public void setIdContaBanco(String idContaBanco) {
        this.idContaBanco = idContaBanco;
    }

    public String getTituloContabanco() {
        return tituloContabanco;
    }

    public void setTituloContabanco(String tituloContabanco) {
        this.tituloContabanco = tituloContabanco;
    }


    public void setSaldoContabancaria(Double saldoContabancaria) {
        this.saldoContabancaria = saldoContabancaria;
    }

    /**
     * metodo responsavel por atualizar o hash map da conta bancaria e neste caso executar a operação de debito de seu valor
     * para posteriormente ser salva no bd
     * @param ban
     * @param valor
     * @return
     */
    public Map<String,Object> MapBancoDebita(ContasBancarias ban, Double valor) {
        HashMap<String, Object> result = new HashMap<>();
        /**
         * validando valor do saldo da conta nao pode ser menor que o valor a ser debitado enviando erro.
         */
        if (ban.getSaldoContabancaria() < valor){
            result = null;
        }else {
            Double saldoAtualizado = ban.getSaldoContabancaria()-valor;
            result.put("idContaBanco", ban.getIdContaBanco());
            result.put("saldoContabancaria", saldoAtualizado);
            result.put("tituloContabanco", ban.getTituloContabanco());
        }

        return result;

    }
    /**
     * metodo responsavel por atualizar o hash map da conta bancaria e neste caso executar a operação de credito de seu valor
     * para posteriormente ser salva no bd
     * @param ban
     * @param valor
     * @return
     */
    public Map<String,Object> MapBancoCredita(ContasBancarias ban, Double valor) {
        Double saldoAtualizado = ban.getSaldoContabancaria()+valor;
        HashMap<String, Object> result = new HashMap<>();
        result.put("idContaBanco", ban.getIdContaBanco());
        result.put("saldoContabancaria", saldoAtualizado);
        result.put("tituloContabanco", ban.getTituloContabanco());
        return result;

    }
}
