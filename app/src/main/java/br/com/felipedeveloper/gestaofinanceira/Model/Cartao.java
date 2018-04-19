package br.com.felipedeveloper.gestaofinanceira.Model;

/**
 * Created by allef on 04/04/2018.
 */

public class Cartao {
    private String idcartao;
    private String saldoCartao;
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

    public String getSaldoCartao() {
        return saldoCartao;
    }

    public void setSaldoCartao(String saldoCartao) {
        this.saldoCartao = saldoCartao;
    }


}
