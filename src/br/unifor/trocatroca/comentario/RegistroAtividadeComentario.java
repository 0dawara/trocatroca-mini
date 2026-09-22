package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.infra.Console;

public class RegistroAtividadeComentario implements ObservadorComentario {

    private final Console console;

    public RegistroAtividadeComentario(Console console) {
        this.console = console;
    }

    @Override
    public void aoNovoComentario(Comentario comentario) {
        console.info("[Registro] Comentário #" + comentario.getId() + " no anúncio #"
                + comentario.getAnuncio().getId() + " por @" + comentario.getAutor().getApelido() + ".");
    }
}
