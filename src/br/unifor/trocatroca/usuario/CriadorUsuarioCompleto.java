package br.unifor.trocatroca.usuario;

import java.util.List;

public class CriadorUsuarioCompleto extends CriadorUsuario {

    @Override
    protected Usuario instanciar(String nome, String apelido, String email, String cidade, String bio, List<String> interesses) {
        return new Usuario(nome, apelido, email, cidade, bio, interesses);
    }

    @Override
    public String rotulo() {
        return "cadastro completo";
    }
}
