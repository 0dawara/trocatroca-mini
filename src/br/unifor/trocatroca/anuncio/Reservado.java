package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.ValidacaoException;

public final class Reservado implements EstadoAnuncio {

    public static final Reservado INSTANCIA = new Reservado();

    private Reservado() {
    }

    @Override
    public String nome() {
        return "RESERVADO";
    }

    @Override
    public EstadoAnuncio reservar() {
        throw new ValidacaoException("Anúncio já está reservado.");
    }

    @Override
    public EstadoAnuncio concluirTroca() {
        return Trocado.INSTANCIA;
    }

    @Override
    public EstadoAnuncio reabrir() {
        return Disponivel.INSTANCIA;
    }
}
