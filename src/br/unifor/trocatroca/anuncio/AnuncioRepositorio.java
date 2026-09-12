package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.RepositorioEmMemoria;

public class AnuncioRepositorio extends RepositorioEmMemoria<Anuncio> {

    public boolean existePorDono(Long donoId) {
        return listar().stream().anyMatch(a -> a.getDono().getId().equals(donoId));
    }
}
