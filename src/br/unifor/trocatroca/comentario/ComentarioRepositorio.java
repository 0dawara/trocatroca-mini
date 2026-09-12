package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.infra.RepositorioEmMemoria;

import java.util.List;
import java.util.stream.Collectors;

public class ComentarioRepositorio extends RepositorioEmMemoria<Comentario> {

    public boolean existePorAnuncio(Long anuncioId) {
        return listar().stream().anyMatch(c -> c.getAnuncio().getId().equals(anuncioId));
    }

    public List<Comentario> listarPorAnuncio(Long anuncioId) {
        return listar().stream()
                .filter(c -> c.getAnuncio().getId().equals(anuncioId))
                .collect(Collectors.toList());
    }
}
