package br.com.felipedeveloper.gestaofinanceira.Model;

import java.math.BigDecimal;

/**
 * Created by allef on 04/04/2018.
 */

public class Cartao {
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


}
