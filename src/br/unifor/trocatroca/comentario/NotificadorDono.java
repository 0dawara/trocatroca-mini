package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.infra.Console;
import br.unifor.trocatroca.usuario.Usuario;

public class NotificadorDono implements ObservadorComentario {

    private final Console console;

    public NotificadorDono(Console console) {
        this.console = console;
    }

    @Override
    public void aoNovoComentario(Comentario comentario) {
        Usuario autor = comentario.getAutor();
        Usuario dono = comentario.getAnuncio().getDono();
        if (autor.getId().equals(dono.getId())) {
            return;
        }
        console.info("[Notificação para @" + dono.getApelido() + "] @" + autor.getApelido()
                + " comentou no anúncio \"" + comentario.getAnuncio().getTitulo() + "\".");
    }
}
