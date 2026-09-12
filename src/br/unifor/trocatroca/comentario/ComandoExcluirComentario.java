package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.infra.Repositorio;

public class ComandoExcluirComentario implements Comando {

    private final Repositorio<Comentario> repositorio;
    private final Comentario comentario;

    public ComandoExcluirComentario(Repositorio<Comentario> repositorio, Comentario comentario) {
        this.repositorio = repositorio;
        this.comentario = comentario;
    }

    @Override
    public void executar() {
        repositorio.remover(comentario.getId());
    }

    @Override
    public void desfazer() {
        repositorio.salvar(comentario);
    }

    @Override
    public String descricao() {
        return "exclusão do comentário #" + comentario.getId();
    }
}
