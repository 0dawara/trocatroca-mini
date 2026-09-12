package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.ValidacaoException;

public final class Disponivel implements EstadoAnuncio {

    public static final Disponivel INSTANCIA = new Disponivel();

    private Disponivel() {
    }

    @Override
    public String nome() {
        return "DISPONIVEL";
    }

    @Override
    public EstadoAnuncio reservar() {
        return Reservado.INSTANCIA;
    }

    @Override
    public EstadoAnuncio concluirTroca() {
        throw new ValidacaoException("Anúncio disponível precisa ser reservado antes de concluir a troca.");
    }

    @Override
    public EstadoAnuncio reabrir() {
        throw new ValidacaoException("Anúncio já está disponível.");
    }
}
