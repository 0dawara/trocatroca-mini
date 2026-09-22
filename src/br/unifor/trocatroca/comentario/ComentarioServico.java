package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.anuncio.Anuncio;
import br.unifor.trocatroca.infra.Repositorio;
import br.unifor.trocatroca.infra.ValidacaoException;
import br.unifor.trocatroca.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ComentarioServico {

    private final ComentarioRepositorio repositorio;
    private final Repositorio<Anuncio> anuncios;
    private final Repositorio<Usuario> usuarios;
    private final List<ObservadorComentario> observadores = new ArrayList<>();
    private final HistoricoComentario historico;

    public ComentarioServico(ComentarioRepositorio repositorio, Repositorio<Anuncio> anuncios, Repositorio<Usuario> usuarios) {
        this.repositorio = repositorio;
        this.anuncios = anuncios;
        this.usuarios = usuarios;
        this.historico = new HistoricoComentario(repositorio);
    }

    public void usarNotificacoes(FabricaNotificacaoComentario fabrica) {
        observadores.clear();
        observadores.add(fabrica.criarNotificadorDono());
        observadores.add(fabrica.criarRegistroAtividade());
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
        Comentario comentario = buscar(id);
        // valida o novo conteúdo num objeto descartável antes de tocar no comentário persistido.
        new Comentario(comentario.getAnuncio(), comentario.getAutor(), novoConteudo);
        String conteudoAnterior = comentario.getConteudo();
        comentario.setConteudo(novoConteudo);
        repositorio.salvar(comentario);
        historico.registrarEdicao(comentario, conteudoAnterior);
    }

    public void excluir(Long id) {
        Comentario comentario = buscar(id);
        repositorio.remover(id);
        historico.registrarExclusao(comentario);
    }

    public String desfazer() {
        return historico.desfazer();
    }
}
