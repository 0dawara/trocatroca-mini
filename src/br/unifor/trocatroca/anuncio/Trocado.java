package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.ValidacaoException;

public final class Trocado implements EstadoAnuncio {

    public static final Trocado INSTANCIA = new Trocado();

    private Trocado() {
    }

    @Override
    public String nome() {
        return "TROCADO";
    }

    @Override
    public EstadoAnuncio reservar() {
        throw new ValidacaoException("Anúncio trocado não pode ser alterado.");
    }

    @Override
    public EstadoAnuncio concluirTroca() {
        throw new ValidacaoException("Anúncio trocado não pode ser alterado.");
    }

    @Override
    public EstadoAnuncio reabrir() {
        throw new ValidacaoException("Anúncio trocado não pode ser alterado.");
    }
}
