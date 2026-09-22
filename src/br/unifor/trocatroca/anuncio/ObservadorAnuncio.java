package br.unifor.trocatroca.anuncio;

public interface ObservadorAnuncio {

    void aoMudarEstado(Anuncio anuncio, EstadoAnuncio estadoAnterior);
}
