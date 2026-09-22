package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.infra.Console;

public class NotificacaoComentarioConsole implements FabricaNotificacaoComentario {

    private final Console console;

    public NotificacaoComentarioConsole(Console console) {
        this.console = console;
    }

    @Override
    public ObservadorComentario criarNotificadorDono() {
        return new NotificadorDono(console);
    }

    @Override
    public ObservadorComentario criarRegistroAtividade() {
        return new RegistroAtividadeComentario(console);
    }
}
