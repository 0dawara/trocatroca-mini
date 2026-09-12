package br.unifor.trocatroca;

import br.unifor.trocatroca.anuncio.AnuncioMenu;
import br.unifor.trocatroca.anuncio.AnuncioRepositorio;
import br.unifor.trocatroca.anuncio.AnuncioServico;
import br.unifor.trocatroca.infra.Console;
import br.unifor.trocatroca.infra.DadosIniciais;
import br.unifor.trocatroca.usuario.UsuarioMenu;
import br.unifor.trocatroca.usuario.UsuarioRepositorio;
import br.unifor.trocatroca.usuario.UsuarioServico;

public class Main {

    public static void main(String[] args) {
        UsuarioRepositorio usuarioRepo = UsuarioRepositorio.getInstancia();
        AnuncioRepositorio anuncioRepo = new AnuncioRepositorio();

        UsuarioServico usuarioServico = new UsuarioServico(usuarioRepo, anuncioRepo::existePorDono);
        AnuncioServico anuncioServico = new AnuncioServico(anuncioRepo, usuarioRepo, id -> false);

        DadosIniciais.carregar(usuarioServico, anuncioServico);

        UsuarioMenu usuarioMenu = new UsuarioMenu(usuarioServico);
        AnuncioMenu anuncioMenu = new AnuncioMenu(anuncioServico, usuarioRepo);

        Console console = Console.get();
        while (true) {
            console.titulo("TrocaTroca Mini");
            console.info("1 - Usuários\n2 - Anúncios\n0 - Sair");
            int opcao = console.lerInt("Opção");
            switch (opcao) {
                case 1 -> usuarioMenu.executar();
                case 2 -> anuncioMenu.executar();
                case 0 -> {
                    console.info("Até logo!");
                    return;
                }
                default -> console.erro("Opção inválida.");
            }
        }
    }
}
