package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.anuncio.Anuncio;
import br.unifor.trocatroca.infra.Repositorio;
import br.unifor.trocatroca.infra.ValidacaoException;
import br.unifor.trocatroca.usuario.Usuario;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ComentarioServico {

    private final ComentarioRepositorio repositorio;
    private final Repositorio<Anuncio> anuncios;
    private final Repositorio<Usuario> usuarios;
    private final List<ObservadorComentario> observadores = new ArrayList<>();
    private final Deque<Comando> historico = new ArrayDeque<>();

    public ComentarioServico(ComentarioRepositorio repositorio, Repositorio<Anuncio> anuncios, Repositorio<Usuario> usuarios) {
        this.repositorio = repositorio;
        this.anuncios = anuncios;
        this.usuarios = usuarios;
    }

    public void adicionarObservador(ObservadorComentario observador) {
        observadores.add(observador);
    }

    public Comentario criar(Long anuncioId, Long autorId, String conteudo) {
        Anuncio anuncio = anuncios.buscarPorId(anuncioId)
                .orElseThrow(() -> new ValidacaoException("Anúncio #" + anuncioId + " não encontrado."));
        Usuario autor = usuarios.buscarPorId(autorId)
                .orElseThrow(() -> new ValidacaoException("Usuário #" + autorId + " não encontrado."));
        Comentario comentario = new Comentario(anuncio, autor, conteudo);
        repositorio.salvar(comentario);
        observadores.forEach(o -> o.aoNovoComentario(comentario));
        return comentario;
    }

    public Comentario buscar(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new ValidacaoException("Comentário #" + id + " não encontrado."));
    }

    public List<Comentario> listar() {
        return repositorio.listar();
    }

    public List<Comentario> listarPorAnuncio(Long anuncioId) {
        return repositorio.listarPorAnuncio(anuncioId);
    }

    public void editar(Long id, String novoConteudo) {
        Comando comando = new ComandoEditarComentario(repositorio, buscar(id), novoConteudo);
        comando.executar();
        historico.push(comando);
    }

    public void excluir(Long id) {
        Comando comando = new ComandoExcluirComentario(repositorio, buscar(id));
        comando.executar();
        historico.push(comando);
    }

    public String desfazer() {
        if (historico.isEmpty()) {
            throw new ValidacaoException("Nada para desfazer.");
        }
        Comando comando = historico.pop();
        comando.desfazer();
        return comando.descricao();
    }
}
