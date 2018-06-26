package br.com.felipedeveloper.gestaofinanceira.Modelo;

/**
 * classe de modelo de Usuario
 */
public class Usuario {

    private String idUsuario;
    private String usuarioNome;
    private String usuarioEmail;
    private String fotoUrl;
    private String usuarioSenha;
    private String usuarioconfirmaSenha;

    public Usuario() {
    }


    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public String getUsuarioSenha() {
        return usuarioSenha;
    }

    public void setUsuarioSenha(String usuarioSenha) {
        this.usuarioSenha = usuarioSenha;
    }

    public String getUsuarioconfirmaSenha() {
        return usuarioconfirmaSenha;
    }

    public void setUsuarioconfirmaSenha(String usuarioconfirmaSenha) {
        this.usuarioconfirmaSenha = usuarioconfirmaSenha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public boolean Verificasenha(){
        if (usuarioSenha.equals(usuarioconfirmaSenha)){
            return true;
        }else {
            return false;
        }
    }
}
