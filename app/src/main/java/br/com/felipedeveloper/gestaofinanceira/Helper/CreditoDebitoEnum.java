package br.com.felipedeveloper.gestaofinanceira.Helper;


/**
 * Constantes do app
 */
public enum CreditoDebitoEnum {
    Credito(1),
    Debito(0);

    private int op;

     CreditoDebitoEnum(int oop) {
        this.op = oop;
    }
    public int getValor() {
        return op;
    }




}
