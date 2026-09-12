package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.infra.Repositorio;

public class ComandoEditarComentario implements Comando {

    private final Repositorio<Comentario> repositorio;
    private final Comentario comentario;
    private final String novoConteudo;
    private String conteudoAnterior;

    public ComandoEditarComentario(Repositorio<Comentario> repositorio, Comentario comentario, String novoConteudo) {
        this.repositorio = repositorio;
        this.comentario = comentario;
        this.novoConteudo = novoConteudo;
    }

    @Override
    public void executar() {
        // valida o novo conteúdo num objeto descartável antes de tocar no comentário persistido.
        new Comentario(comentario.getAnuncio(), comentario.getAutor(), novoConteudo);
        conteudoAnterior = comentario.getConteudo();
        comentario.setConteudo(novoConteudo);
        repositorio.salvar(comentario);
    }

    @Override
    public void desfazer() {
        comentario.setConteudo(conteudoAnterior);
        repositorio.salvar(comentario);
    }

    @Override
    public String descricao() {
        return "edição do comentário #" + comentario.getId();
    }
}
