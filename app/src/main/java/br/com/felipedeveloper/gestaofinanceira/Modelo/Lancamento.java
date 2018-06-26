package br.com.felipedeveloper.gestaofinanceira.Modelo;

/**
 * classe de  modelo lancamentos pessoais
 */
public class Lancamento {
    private String idLancamento;
    private String titulo;
    private Double valor;
    private String data;
    private String nomeopFinanceira;
    private int StatusOp;
    private Long createdAt;


    public String getNomeopFinanceira() {
        return nomeopFinanceira;
    }

    public void setNomeopFinanceira(String nomeopFinanceira) {
        this.nomeopFinanceira = nomeopFinanceira;
    }

    public Lancamento() {
    }

    public String getIdLancamento() {
        return idLancamento;
    }

    public void setIdLancamento(String idLancamento) {
        this.idLancamento = idLancamento;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }


}
