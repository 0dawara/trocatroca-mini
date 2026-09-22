package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.Console;

public class NotificacaoAnuncioConsole implements FabricaNotificacaoAnuncio {

    private final Console console;

    public NotificacaoAnuncioConsole(Console console) {
        this.console = console;
    }

    @Override
    public ObservadorAnuncio criarNotificadorDono() {
        return new NotificadorDonoAnuncio(console);
    }

    @Override
    public ObservadorAnuncio criarRegistroDeEstado() {
        return new RegistroEstadoAnuncio(console);
    }
}
