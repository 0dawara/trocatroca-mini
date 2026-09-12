package br.unifor.trocatroca.usuario;

import br.unifor.trocatroca.infra.RepositorioEmMemoria;

import java.util.Optional;

public class UsuarioRepositorio extends RepositorioEmMemoria<Usuario> {

    private static UsuarioRepositorio instancia;

    private UsuarioRepositorio() {
    }

    public static synchronized UsuarioRepositorio getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioRepositorio();
        }
        return instancia;
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return listar().stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public Optional<Usuario> buscarPorApelido(String apelido) {
        return listar().stream().filter(u -> u.getApelido().equalsIgnoreCase(apelido)).findFirst();
    }
}
