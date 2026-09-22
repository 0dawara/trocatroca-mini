package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.Console;

public class NotificadorDonoAnuncio implements ObservadorAnuncio {

    private final Console console;

    public NotificadorDonoAnuncio(Console console) {
        this.console = console;
    }

    @Override
    public void aoMudarEstado(Anuncio anuncio, EstadoAnuncio estadoAnterior) {
        console.info("[Notificação para @" + anuncio.getDono().getApelido() + "] Seu anúncio \""
                + anuncio.getTitulo() + "\" passou de " + estadoAnterior + " para " + anuncio.getEstado() + ".");
    }
}
