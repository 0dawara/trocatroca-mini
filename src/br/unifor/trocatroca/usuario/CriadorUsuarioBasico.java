package br.unifor.trocatroca.usuario;

import java.util.List;

public class CriadorUsuarioBasico extends CriadorUsuario {

    @Override
    protected Usuario instanciar(String nome, String apelido, String email, String cidade, String bio, List<String> interesses) {
        return new Usuario(nome, apelido, email, "", "", List.of());
    }

    @Override
    public String rotulo() {
        return "cadastro rápido";
    }
}
