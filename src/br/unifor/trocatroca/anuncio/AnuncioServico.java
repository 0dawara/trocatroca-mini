package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.Repositorio;
import br.unifor.trocatroca.infra.ValidacaoException;
import br.unifor.trocatroca.usuario.Usuario;

import java.util.List;
import java.util.function.Predicate;

public class AnuncioServico {

    private final AnuncioRepositorio repositorio;
    private final Repositorio<Usuario> usuarios;
    private final Predicate<Long> possuiComentarios;

    public AnuncioServico(AnuncioRepositorio repositorio, Repositorio<Usuario> usuarios, Predicate<Long> possuiComentarios) {
        this.repositorio = repositorio;
        this.usuarios = usuarios;
        this.possuiComentarios = possuiComentarios;
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

    public List<Anuncio> listar(FiltroAnuncio filtro) {
        return repositorio.filtrar(filtro::aceita);
    }

    public Anuncio editar(Long id, String titulo, String descricao, Categoria categoria, Condicao condicao, String trocaDesejada) {
        Anuncio anuncio = buscar(id);
        anuncio.setTitulo(titulo);
        anuncio.setDescricao(descricao);
        anuncio.setCategoria(categoria);
        anuncio.setCondicao(condicao);
        anuncio.setTrocaDesejada(trocaDesejada);
        anuncio.validar();
        return repositorio.salvar(anuncio);
    }

    public void reservar(Long id) {
        Anuncio anuncio = buscar(id);
        anuncio.reservar();
        repositorio.salvar(anuncio);
    }

    public void concluirTroca(Long id) {
        Anuncio anuncio = buscar(id);
        anuncio.concluirTroca();
        repositorio.salvar(anuncio);
    }

    public void reabrir(Long id) {
        Anuncio anuncio = buscar(id);
        anuncio.reabrir();
        repositorio.salvar(anuncio);
    }

    public void excluir(Long id) {
        buscar(id);
        if (possuiComentarios.test(id)) {
            throw new ValidacaoException("Anúncio possui comentários; exclua-os antes.");
        }
        repositorio.remover(id);
    }
}
