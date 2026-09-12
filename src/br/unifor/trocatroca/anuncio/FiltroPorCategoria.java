package br.unifor.trocatroca.anuncio;

public class FiltroPorCategoria implements FiltroAnuncio {

    private final Categoria categoria;

    public FiltroPorCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean aceita(Anuncio anuncio) {
        return anuncio.getCategoria() == categoria;
    }
}
