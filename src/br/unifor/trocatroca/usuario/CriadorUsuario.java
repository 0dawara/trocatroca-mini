package br.unifor.trocatroca.usuario;

import java.util.List;

public abstract class CriadorUsuario {

    public final Usuario criar(String nome, String apelido, String email, String cidade, String bio, List<String> interesses) {
        Usuario usuario = instanciar(nome, apelido, email, cidade, bio, interesses);
        usuario.validar();
        return usuario;
    }

    protected abstract Usuario instanciar(String nome, String apelido, String email, String cidade, String bio, List<String> interesses);

    public abstract String rotulo();
}
