package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.infra.Repositorio;
import br.unifor.trocatroca.infra.ValidacaoException;

import java.util.ArrayDeque;
import java.util.Deque;

public class HistoricoComentario {

    private enum Tipo { EDICAO, EXCLUSAO }

    private record Alteracao(Tipo tipo, Comentario comentario, String conteudoAnterior) {
    }

    private final Repositorio<Comentario> repositorio;
    private final Deque<Alteracao> alteracoes = new ArrayDeque<>();

    public HistoricoComentario(Repositorio<Comentario> repositorio) {
        this.repositorio = repositorio;
    }

    public void registrarEdicao(Comentario comentario, String conteudoAnterior) {
        alteracoes.push(new Alteracao(Tipo.EDICAO, comentario, conteudoAnterior));
    }

    public void registrarExclusao(Comentario comentario) {
        alteracoes.push(new Alteracao(Tipo.EXCLUSAO, comentario, comentario.getConteudo()));
    }

    public String desfazer() {
        if (alteracoes.isEmpty()) {
            throw new ValidacaoException("Nada para desfazer.");
        }
        Alteracao alteracao = alteracoes.pop();
        return switch (alteracao.tipo()) {
            case EDICAO -> {
                alteracao.comentario().setConteudo(alteracao.conteudoAnterior());
                repositorio.salvar(alteracao.comentario());
                yield "edição do comentário #" + alteracao.comentario().getId();
            }
            case EXCLUSAO -> {
                repositorio.salvar(alteracao.comentario());
                yield "exclusão do comentário #" + alteracao.comentario().getId();
            }
        };
    }
}
