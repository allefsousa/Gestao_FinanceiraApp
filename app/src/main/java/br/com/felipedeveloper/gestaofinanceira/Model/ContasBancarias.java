package br.com.felipedeveloper.gestaofinanceira.Model;

/**
 * Created by allef on 04/04/2018.
 */

public class ContasBancarias {
    private String idContabancaria;
    private String tituloContabancaria;
    private Double SaldoContabancaria;
    private String agencia;


    public ContasBancarias() {
    }

    public String getIdContabancaria() {
        return idContabancaria;
    }

    public void setIdContabancaria(String idContabancaria) {
        this.idContabancaria = idContabancaria;
    }

    public String getTituloContabancaria() {
        return tituloContabancaria;
    }

    public void setTituloContabancaria(String tituloContabancaria) {
        this.tituloContabancaria = tituloContabancaria;
    }

    public Double getSaldoContabancaria() {
        return SaldoContabancaria;
    }

    public void setSaldoContabancaria(Double saldoContabancaria) {
        SaldoContabancaria = saldoContabancaria;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }
}
