package br.com.felipedeveloper.gestaofinanceira.Model;

/**
 * Created by allef on 03/04/2018.
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

    public boolean Verificasenha(String senha1,String senha2){
        if (senha1.equals(senha2)){
            return true;
        }else {
            return false;
        }
    }
}
