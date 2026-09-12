package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.MenuCrud;
import br.unifor.trocatroca.infra.Repositorio;
import br.unifor.trocatroca.usuario.Usuario;

import java.util.List;

public class AnuncioMenu extends MenuCrud<Anuncio> {

    private final AnuncioServico servico;
    private final Repositorio<Usuario> usuarios;

    public AnuncioMenu(AnuncioServico servico, Repositorio<Usuario> usuarios) {
        this.servico = servico;
        this.usuarios = usuarios;
    }

    @Override
    protected String titulo() {
        return "Anúncios";
    }

    @Override
    protected String opcoesExtras() {
        return "\n6 - Reservar\n7 - Concluir troca\n8 - Reabrir";
    }

    @Override
    protected boolean opcaoExtra(int opcao) {
        switch (opcao) {
            case 6 -> {
                Anuncio anuncio = servico.buscar(console.lerLong("Id"));
                servico.reservar(anuncio.getId());
                console.info("Anúncio agora está " + anuncio.getEstado().nome() + ".");
                return true;
            }
            case 7 -> {
                Anuncio anuncio = servico.buscar(console.lerLong("Id"));
                servico.concluirTroca(anuncio.getId());
                console.info("Anúncio agora está " + anuncio.getEstado().nome() + ".");
                return true;
            }
            case 8 -> {
                Anuncio anuncio = servico.buscar(console.lerLong("Id"));
                servico.reabrir(anuncio.getId());
                console.info("Anúncio agora está " + anuncio.getEstado().nome() + ".");
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    protected void cadastrar() {
        List<Usuario> todosUsuarios = usuarios.listar();
        if (todosUsuarios.isEmpty()) {
            console.erro("Nenhum usuário cadastrado.");
            return;
        }
        todosUsuarios.forEach(u -> console.info(u.resumo()));
        long donoId = console.lerLong("Id do dono");
        String titulo = console.lerTexto("Título");
        String descricao = console.lerTexto("Descrição");
        Categoria categoria = console.lerEnum("Categoria", Categoria.class);
        Condicao condicao = console.lerEnum("Condição", Condicao.class);
        String trocaDesejada = console.lerTextoOpcional("Deseja em troca", "");
        Anuncio anuncio = servico.criar(titulo, descricao, categoria, condicao, trocaDesejada, donoId);
        console.info("Anúncio cadastrado: " + anuncio.resumo());
    }

    @Override
    protected void listar() {
        console.info("1 - Todos\n2 - Por categoria\n3 - Por dono\n4 - Por estado");
        int opcao = console.lerInt("Opção");
        FiltroAnuncio filtro = switch (opcao) {
            case 2 -> new FiltroPorCategoria(console.lerEnum("Categoria", Categoria.class));
            case 3 -> new FiltroPorDono(console.lerLong("Id do dono"));
            case 4 -> new FiltroPorEstado(console.lerTexto("Estado").toUpperCase());
            default -> a -> true;
        };
        List<Anuncio> anuncios = servico.listar(filtro);
        if (anuncios.isEmpty()) {
            console.info("Nenhum anúncio encontrado.");
            return;
        }
        anuncios.forEach(a -> console.info(a.resumo()));
    }

    @Override
    protected void detalhar() {
        long id = console.lerLong("Id");
        console.info(servico.buscar(id).detalhes());
    }

    @Override
    protected void editar() {
        long id = console.lerLong("Id");
        Anuncio atual = servico.buscar(id);
        String titulo = console.lerTextoOpcional("Título", atual.getTitulo());
        String descricao = console.lerTextoOpcional("Descrição", atual.getDescricao());
        Categoria categoria = console.lerEnumOpcional("Categoria", Categoria.class, atual.getCategoria());
        Condicao condicao = console.lerEnumOpcional("Condição", Condicao.class, atual.getCondicao());
        String trocaDesejada = console.lerTextoOpcional("Deseja em troca",
                atual.getTrocaDesejada() == null ? "" : atual.getTrocaDesejada());
        servico.editar(id, titulo, descricao, categoria, condicao, trocaDesejada);
        console.info("Anúncio atualizado.");
    }

    @Override
    protected void excluir() {
        long id = console.lerLong("Id");
        String confirmacao = console.lerTexto("Confirmar exclusão (s/n)");
        if (confirmacao.equalsIgnoreCase("s")) {
            servico.excluir(id);
            console.info("Anúncio excluído.");
        }
    }
}
