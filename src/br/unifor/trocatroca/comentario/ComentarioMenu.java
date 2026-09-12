package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.infra.MenuCrud;

import java.util.List;

public class ComentarioMenu extends MenuCrud<Comentario> {

    private final ComentarioServico servico;

    public ComentarioMenu(ComentarioServico servico) {
        this.servico = servico;
    }

    @Override
    protected String titulo() {
        return "Comentários";
    }

    @Override
    protected String opcoesExtras() {
        return "\n6 - Listar por anúncio\n7 - Desfazer última edição/exclusão";
    }

    @Override
    protected boolean opcaoExtra(int opcao) {
        switch (opcao) {
            case 6 -> {
                long anuncioId = console.lerLong("Id do anúncio");
                List<Comentario> comentarios = servico.listarPorAnuncio(anuncioId);
                if (comentarios.isEmpty()) {
                    console.info("Nenhum comentário cadastrado.");
                } else {
                    comentarios.forEach(c -> console.info(c.resumo()));
                }
                return true;
            }
            case 7 -> {
                console.info("Desfeita: " + servico.desfazer());
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    protected void cadastrar() {
        long anuncioId = console.lerLong("Id do anúncio");
        long autorId = console.lerLong("Id do autor");
        String conteudo = console.lerTexto("Comentário");
        Comentario comentario = servico.criar(anuncioId, autorId, conteudo);
        console.info("Comentário cadastrado: " + comentario.resumo());
    }

    @Override
    protected void listar() {
        List<Comentario> comentarios = servico.listar();
        if (comentarios.isEmpty()) {
            console.info("Nenhum comentário cadastrado.");
            return;
        }
        comentarios.forEach(c -> console.info(c.resumo()));
    }

    @Override
    protected void detalhar() {
        long id = console.lerLong("Id");
        console.info(servico.buscar(id).detalhes());
    }

    @Override
    protected void editar() {
        long id = console.lerLong("Id");
        Comentario atual = servico.buscar(id);
        console.info("Conteúdo atual: " + atual.getConteudo());
        String novoConteudo = console.lerTexto("Novo conteúdo");
        servico.editar(id, novoConteudo);
        console.info("Comentário atualizado.");
    }

    @Override
    protected void excluir() {
        long id = console.lerLong("Id");
        String confirmacao = console.lerTexto("Confirmar exclusão (s/n)");
        if (confirmacao.equalsIgnoreCase("s")) {
            servico.excluir(id);
            console.info("Comentário excluído.");
        }
    }
}
