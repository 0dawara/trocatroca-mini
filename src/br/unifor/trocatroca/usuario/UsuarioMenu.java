package br.unifor.trocatroca.usuario;

import br.unifor.trocatroca.infra.MenuCrud;

import java.util.ArrayList;
import java.util.List;

public class UsuarioMenu extends MenuCrud<Usuario> {

    private final UsuarioServico servico;

    public UsuarioMenu(UsuarioServico servico) {
        this.servico = servico;
    }

    @Override
    protected String titulo() {
        return "Usuários";
    }

    @Override
    protected void cadastrar() {
        String nome = console.lerTexto("Nome");
        String apelido = console.lerTexto("Apelido");
        String email = console.lerTexto("E-mail");
        String cidade = console.lerTextoOpcional("Cidade", "");
        String bio = console.lerTextoOpcional("Bio", "");
        List<String> interesses = lerInteresses(console.lerTextoOpcional("Interesses (separados por vírgula)", ""));
        Usuario usuario = servico.criar(nome, apelido, email, cidade, bio, interesses);
        console.info("Usuário cadastrado: " + usuario.resumo());
    }

    @Override
    protected void listar() {
        List<Usuario> usuarios = servico.listar();
        if (usuarios.isEmpty()) {
            console.info("Nenhum usuário cadastrado.");
            return;
        }
        usuarios.forEach(u -> console.info(u.resumo()));
    }

    @Override
    protected void detalhar() {
        long id = console.lerLong("Id");
        console.info(servico.buscar(id).detalhes());
    }

    @Override
    protected void editar() {
        long id = console.lerLong("Id");
        Usuario atual = servico.buscar(id);
        String nome = console.lerTextoOpcional("Nome", atual.getNome());
        String apelido = console.lerTextoOpcional("Apelido", atual.getApelido());
        String email = console.lerTextoOpcional("E-mail", atual.getEmail());
        String cidade = console.lerTextoOpcional("Cidade", atual.getCidade() == null ? "" : atual.getCidade());
        String bio = console.lerTextoOpcional("Bio", atual.getBio() == null ? "" : atual.getBio());
        String interessesAtuais = String.join(", ", atual.getInteresses());
        List<String> interesses = lerInteresses(console.lerTextoOpcional("Interesses (separados por vírgula)", interessesAtuais));
        servico.editar(id, nome, apelido, email, cidade, bio, interesses);
        console.info("Usuário atualizado.");
    }

    @Override
    protected void excluir() {
        long id = console.lerLong("Id");
        String confirmacao = console.lerTexto("Confirmar exclusão (s/n)");
        if (confirmacao.equalsIgnoreCase("s")) {
            servico.excluir(id);
            console.info("Usuário excluído.");
        }
    }

    private List<String> lerInteresses(String texto) {
        if (texto == null || texto.isBlank()) {
            return new ArrayList<>();
        }
        List<String> resultado = new ArrayList<>();
        for (String parte : texto.split(",")) {
            String trimado = parte.trim();
            if (!trimado.isEmpty()) {
                resultado.add(trimado);
            }
        }
        return resultado;
    }
}
