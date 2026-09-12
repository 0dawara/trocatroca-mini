package br.unifor.trocatroca.anuncio;

public class FiltroPorEstado implements FiltroAnuncio {

    private final String nomeEstado;

    public FiltroPorEstado(String nomeEstado) {
        this.nomeEstado = nomeEstado;
    }

    @Override
    public boolean aceita(Anuncio anuncio) {
        return anuncio.getEstado().nome().equals(nomeEstado);
    }
}
