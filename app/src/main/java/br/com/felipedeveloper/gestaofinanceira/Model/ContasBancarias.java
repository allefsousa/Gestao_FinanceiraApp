package br.com.felipedeveloper.gestaofinanceira.Model;

/**
 * Created by allef on 04/04/2018.
 */

public class ContasBancarias {
    private String idContabancaria;
    private String tituloContabancaria;
    private String SaldoContabancaria;
    // TODO: 20/04/2018  atualizar saldo para double corrigir possiveis falhas 



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

    public String getSaldoContabancaria() {
        return SaldoContabancaria;
    }

    public void setSaldoContabancaria(String saldoContabancaria) {
        SaldoContabancaria = saldoContabancaria;
    }

}
