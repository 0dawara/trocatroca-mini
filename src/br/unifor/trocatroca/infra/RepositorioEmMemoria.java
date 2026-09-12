package br.unifor.trocatroca.infra;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RepositorioEmMemoria<T extends Entidade> implements Repositorio<T> {

    private final Map<Long, T> dados = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(0);

    @Override
    public T salvar(T entidade) {
        if (entidade.getId() == null) {
            entidade.setId(sequencia.incrementAndGet());
        } else {
            sequencia.accumulateAndGet(entidade.getId(), Math::max);
        }
        dados.put(entidade.getId(), entidade);
        return entidade;
    }

    @Override
    public Optional<T> buscarPorId(Long id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<T> listar() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public boolean remover(Long id) {
        return dados.remove(id) != null;
    }

    @Override
    public List<T> filtrar(Predicate<T> criterio) {
        return listar().stream().filter(criterio).collect(Collectors.toList());
    }
}
