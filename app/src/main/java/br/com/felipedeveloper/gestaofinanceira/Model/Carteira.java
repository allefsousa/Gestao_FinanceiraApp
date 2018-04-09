package br.com.felipedeveloper.gestaofinanceira.Model;

/**
 * Created by allef on 07/04/2018.
 */

public class Carteira {
    private String titulo;
    private Double valor;
    private String data;
    private int StatusOp;


    public Carteira() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatusOp() {
        return StatusOp;
    }

    public void setStatusOp(int statusOp) {
        StatusOp = statusOp;
    }

    public Double adicionaDinheiro(Double dinheiro){
        Double saldo = valor;
        saldo = saldo + dinheiro;
        return saldo;


    }
    public Double retiraDinheiro(Double dinheiro){
        Double saldo = valor;
        saldo = saldo - dinheiro;
        return saldo;
    }
}
