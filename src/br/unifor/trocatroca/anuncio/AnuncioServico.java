package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.Repositorio;
import br.unifor.trocatroca.infra.ValidacaoException;
import br.unifor.trocatroca.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AnuncioServico {

    private final AnuncioRepositorio repositorio;
    private final Repositorio<Usuario> usuarios;
    private final Predicate<Long> possuiComentarios;

    private final List<ObservadorAnuncio> observadores = new ArrayList<>();
    public AnuncioServico(AnuncioRepositorio repositorio, Repositorio<Usuario> usuarios, Predicate<Long> possuiComentarios) {
        this.repositorio = repositorio;
        this.usuarios = usuarios;
        this.possuiComentarios = possuiComentarios;
    }

    public void usarNotificacoes(FabricaNotificacaoAnuncio fabrica) {
        observadores.clear();
        observadores.add(fabrica.criarNotificadorDono());
        observadores.add(fabrica.criarRegistroDeEstado());
    }

    public Anuncio criar(String titulo, String descricao, Categoria categoria, Condicao condicao, String trocaDesejada, Long donoId) {
        Usuario dono = usuarios.buscarPorId(donoId)
                .orElseThrow(() -> new ValidacaoException("Usuário #" + donoId + " não encontrado."));
        Anuncio anuncio = new Anuncio(titulo, descricao, categoria, condicao, trocaDesejada, dono);
        return repositorio.salvar(anuncio);
    }

    public Anuncio buscar(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new ValidacaoException("Anúncio #" + id + " não encontrado."));
    }

    public List<Anuncio> listar() {
        return listar(null, null, null);
    }

    public List<Anuncio> listar(Categoria categoria, Long donoId, EstadoAnuncio estado) {
        return repositorio.filtrar(anuncio -> (categoria == null || anuncio.getCategoria() == categoria)
                && (donoId == null || anuncio.getDono().getId().equals(donoId))
                && (estado == null || anuncio.getEstado() == estado));
    }

    public Anuncio editar(Long id, String titulo, String descricao, Categoria categoria, Condicao condicao, String trocaDesejada) {
        Anuncio anuncio = buscar(id);
        // valida os novos dados num objeto descartável (mesmo dono) antes de tocar na entidade persistida.
        new Anuncio(titulo, descricao, categoria, condicao, trocaDesejada, anuncio.getDono());
        anuncio.setTitulo(titulo);
        anuncio.setDescricao(descricao);
        anuncio.setCategoria(categoria);
        anuncio.setCondicao(condicao);
        anuncio.setTrocaDesejada(trocaDesejada);
        return repositorio.salvar(anuncio);
    }

    public void reservar(Long id) {
        Anuncio anuncio = buscar(id);
        EstadoAnuncio anterior = anuncio.getEstado();
        anuncio.reservar();
        repositorio.salvar(anuncio);
        notificar(anuncio, anterior);
    }

    public void concluirTroca(Long id) {
        Anuncio anuncio = buscar(id);
        EstadoAnuncio anterior = anuncio.getEstado();
        anuncio.concluirTroca();
        repositorio.salvar(anuncio);
        notificar(anuncio, anterior);
    }

    public void reabrir(Long id) {
        Anuncio anuncio = buscar(id);
        EstadoAnuncio anterior = anuncio.getEstado();
        anuncio.reabrir();
        repositorio.salvar(anuncio);
        notificar(anuncio, anterior);
    }

    private void notificar(Anuncio anuncio, EstadoAnuncio estadoAnterior) {
        observadores.forEach(observador -> observador.aoMudarEstado(anuncio, estadoAnterior));
    }

    public void excluir(Long id) {
        buscar(id);
        if (possuiComentarios.test(id)) {
            throw new ValidacaoException("Anúncio possui comentários; exclua-os antes.");
        }
        repositorio.remover(id);
    }
}
