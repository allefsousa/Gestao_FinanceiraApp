package br.com.felipedeveloper.gestaofinanceira.Model;

/**
 * Created by allef on 04/04/2018.
 */

public class Cartao {
    private int saldoCartao;
    private String tituloCartao;

    public Cartao() {
    }

    public int getSaldoCartao() {
        return saldoCartao;
    }

    public void setSaldoCartao(int saldoCartao) {
        this.saldoCartao = saldoCartao;
    }

    public String getDetalhesCartao() {
        return tituloCartao;
    }

    public void setDetalhesCartao(String detalhesCartao) {
        this.tituloCartao = detalhesCartao;
    }

}
