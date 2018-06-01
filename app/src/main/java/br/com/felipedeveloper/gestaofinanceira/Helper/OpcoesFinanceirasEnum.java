package br.com.felipedeveloper.gestaofinanceira.Helper;

/**
 * constantes do app
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
