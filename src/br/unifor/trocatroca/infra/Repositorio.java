package br.unifor.trocatroca.infra;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Repositorio<T extends Entidade> {

    T salvar(T entidade);

    Optional<T> buscarPorId(Long id);

    List<T> listar();

    boolean remover(Long id);

    List<T> filtrar(Predicate<T> criterio);
}
