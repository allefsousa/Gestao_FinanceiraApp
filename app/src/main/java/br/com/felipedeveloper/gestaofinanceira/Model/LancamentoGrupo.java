package br.com.felipedeveloper.gestaofinanceira.Model;

/**
 * classe de modelo de lancamento em grupo
 */
public class LancamentoGrupo {

    private String titulo;
    private String nomeColaborador;
    private Double valor;
    private String data;
    private String nomeopFinanceira;
    private String nomeGrupo;
    private int StatusOp;
    private Long createdAt;

    public String getNomeColaborador() {
        return nomeColaborador;
    }

    public void setNomeColaborador(String nomeColaborador) {
        this.nomeColaborador = nomeColaborador;
    }

    public String getNomeopFinanceira() {
        return nomeopFinanceira;
    }

    public void setNomeopFinanceira(String nomeopFinanceira) {
        this.nomeopFinanceira = nomeopFinanceira;
    }

    public LancamentoGrupo() {
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

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

}
