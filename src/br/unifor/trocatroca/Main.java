package br.unifor.trocatroca;

import br.unifor.trocatroca.anuncio.AnuncioMenu;
import br.unifor.trocatroca.anuncio.AnuncioRepositorio;
import br.unifor.trocatroca.anuncio.AnuncioServico;
import br.unifor.trocatroca.anuncio.NotificacaoAnuncioConsole;
import br.unifor.trocatroca.anuncio.NotificacaoAnuncioSilenciosa;
import br.unifor.trocatroca.comentario.ComentarioMenu;
import br.unifor.trocatroca.comentario.ComentarioRepositorio;
import br.unifor.trocatroca.comentario.ComentarioServico;
import br.unifor.trocatroca.comentario.NotificadorDono;
import br.unifor.trocatroca.infra.Console;
import br.unifor.trocatroca.infra.DadosIniciais;
import br.unifor.trocatroca.usuario.UsuarioMenu;
import br.unifor.trocatroca.usuario.UsuarioRepositorio;
import br.unifor.trocatroca.usuario.UsuarioServico;

import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) {
        UsuarioRepositorio usuarioRepo = UsuarioRepositorio.getInstancia();
        AnuncioRepositorio anuncioRepo = new AnuncioRepositorio();
        ComentarioRepositorio comentarioRepo = new ComentarioRepositorio();

        UsuarioServico usuarioServico = new UsuarioServico(usuarioRepo, anuncioRepo::existePorDono);
        AnuncioServico anuncioServico = new AnuncioServico(anuncioRepo, usuarioRepo, comentarioRepo::existePorAnuncio);
        ComentarioServico comentarioServico = new ComentarioServico(comentarioRepo, anuncioRepo, usuarioRepo);

        anuncioServico.usarNotificacoes(new NotificacaoAnuncioSilenciosa());

        DadosIniciais.carregar(usuarioServico, anuncioServico, comentarioServico);

        anuncioServico.usarNotificacoes(new NotificacaoAnuncioConsole(Console.get()));
        comentarioServico.adicionarObservador(new NotificadorDono(Console.get()));

        UsuarioMenu usuarioMenu = new UsuarioMenu(usuarioServico);
        AnuncioMenu anuncioMenu = new AnuncioMenu(anuncioServico, usuarioRepo);
        ComentarioMenu comentarioMenu = new ComentarioMenu(comentarioServico);

        Console console = Console.get();
        try {
            while (true) {
                console.titulo("TrocaTroca Mini");
                console.info("1 - Usuários\n2 - Anúncios\n3 - Comentários\n0 - Sair");
                int opcao = console.lerInt("Opção");
                switch (opcao) {
                    case 1 -> usuarioMenu.executar();
                    case 2 -> anuncioMenu.executar();
                    case 3 -> comentarioMenu.executar();
                    case 0 -> {
                        console.info("Até logo!");
                        return;
                    }
                    default -> console.erro("Opção inválida.");
                }
            }
        } catch (NoSuchElementException e) {
            console.info("\nAté logo!");
        }
    }
}
