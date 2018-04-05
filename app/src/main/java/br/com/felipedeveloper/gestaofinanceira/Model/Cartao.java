package br.com.felipedeveloper.gestaofinanceira.Model;

/**
 * Created by allef on 04/04/2018.
 */

public class Cartao {
    private int saldoCartao;
    private String detalhesCartao;
    private String bandeiraCartao;
    // TODO: 04/04/2018 numeor de cartão ninguem vai colocar.


    public Cartao() {
    }

    public int getSaldoCartao() {
        return saldoCartao;
    }

    public void setSaldoCartao(int saldoCartao) {
        this.saldoCartao = saldoCartao;
    }

    public String getDetalhesCartao() {
        return detalhesCartao;
    }

    public void setDetalhesCartao(String detalhesCartao) {
        this.detalhesCartao = detalhesCartao;
    }

    public String getBandeiraCartao() {
        return bandeiraCartao;
    }

    public void setBandeiraCartao(String bandeiraCartao) {
        this.bandeiraCartao = bandeiraCartao;
    }
}
