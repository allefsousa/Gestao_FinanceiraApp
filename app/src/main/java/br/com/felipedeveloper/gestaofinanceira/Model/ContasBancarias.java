package br.com.felipedeveloper.gestaofinanceira.Model;

import java.util.HashMap;
import java.util.Map;



public class ContasBancarias {
    private String idContaBanco;
    private String tituloContabanco;
    private Double SaldoContabancaria;
    // TODO: 20/04/2018  atualizar saldo para double corrigir possiveis falhas 



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

    public Double getSaldoContabancaria() {
        return SaldoContabancaria;
    }

    public void setSaldoContabancaria(Double saldoContabancaria) {
        SaldoContabancaria = saldoContabancaria;
    }

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
            result.put("SaldoContabancaria", saldoAtualizado);
            result.put("tituloContabanco", ban.getTituloContabanco());
        }

        return result;

    }
    public Map<String,Object> MapBancoCredita(ContasBancarias ban, Double valor) {
        Double saldoAtualizado = ban.getSaldoContabancaria()+valor;
        HashMap<String, Object> result = new HashMap<>();
        result.put("idContaBanco", ban.getIdContaBanco());
        result.put("SaldoContabancaria", saldoAtualizado);
        result.put("tituloContabanco", ban.getTituloContabanco());
        return result;

    }
}
