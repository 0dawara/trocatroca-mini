package br.unifor.trocatroca.anuncio;

public interface EstadoAnuncio {

    String nome();

    EstadoAnuncio reservar();

    EstadoAnuncio concluirTroca();

    EstadoAnuncio reabrir();
}
