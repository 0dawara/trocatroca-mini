package br.unifor.trocatroca.anuncio;

public class FiltroPorDono implements FiltroAnuncio {

    private final Long donoId;

    public FiltroPorDono(Long donoId) {
        this.donoId = donoId;
    }

    @Override
    public boolean aceita(Anuncio anuncio) {
        return anuncio.getDono().getId().equals(donoId);
    }
}
