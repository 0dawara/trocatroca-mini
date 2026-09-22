package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.Console;

public class RegistroEstadoAnuncio implements ObservadorAnuncio {

    private final Console console;

    public RegistroEstadoAnuncio(Console console) {
        this.console = console;
    }

    @Override
    public void aoMudarEstado(Anuncio anuncio, EstadoAnuncio estadoAnterior) {
        console.info("[Histórico] Anúncio #" + anuncio.getId() + ": " + estadoAnterior + " -> " + anuncio.getEstado() + ".");
    }
}
