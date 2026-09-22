package br.unifor.trocatroca.anuncio;

public interface FabricaNotificacaoAnuncio {

    ObservadorAnuncio criarNotificadorDono();

    ObservadorAnuncio criarRegistroDeEstado();
}
