package br.com.felipedeveloper.gestaofinanceira.Helper;

/**
 * Created by allef on 30/05/2018.
 */

public enum OpcoesFinanceirasEnum {
    carteira  ("carteira"),
    banco  ("banco"),
    cartao  ("cartao");
    private String texto;

    OpcoesFinanceirasEnum(String texto) {
        this.texto = texto;
    }

    public String getValor() {
        return texto;
    }
}
