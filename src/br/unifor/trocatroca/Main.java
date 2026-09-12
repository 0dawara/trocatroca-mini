package br.unifor.trocatroca;

import br.unifor.trocatroca.infra.Console;
import br.unifor.trocatroca.infra.DadosIniciais;
import br.unifor.trocatroca.usuario.UsuarioMenu;
import br.unifor.trocatroca.usuario.UsuarioRepositorio;
import br.unifor.trocatroca.usuario.UsuarioServico;

public class Main {

    public static void main(String[] args) {
        UsuarioRepositorio usuarioRepo = UsuarioRepositorio.getInstancia();
        UsuarioServico usuarioServico = new UsuarioServico(usuarioRepo, id -> false);

        DadosIniciais.carregar(usuarioServico);

        UsuarioMenu usuarioMenu = new UsuarioMenu(usuarioServico);

        Console console = Console.get();
        while (true) {
            console.titulo("TrocaTroca Mini");
            console.info("1 - Usuários\n0 - Sair");
            int opcao = console.lerInt("Opção");
            switch (opcao) {
                case 1 -> usuarioMenu.executar();
                case 0 -> {
                    console.info("Até logo!");
                    return;
                }
                default -> console.erro("Opção inválida.");
            }
        }
    }
}
