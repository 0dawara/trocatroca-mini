package br.unifor.trocatroca.infra;

public abstract class MenuCrud<T> {

    protected final Console console = Console.get();

    public final void executar() {
        while (true) {
            console.titulo(titulo());
            console.info("1 - Cadastrar\n2 - Listar\n3 - Detalhar\n4 - Editar\n5 - Excluir" + opcoesExtras() + "\n0 - Voltar");
            int opcao = console.lerInt("Opção");
            if (opcao == 0) {
                return;
            }
            try {
                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> listar();
                    case 3 -> detalhar();
                    case 4 -> editar();
                    case 5 -> excluir();
                    default -> {
                        if (!opcaoExtra(opcao)) {
                            console.erro("Opção inválida.");
                        }
                    }
                }
            } catch (ValidacaoException e) {
                console.erro(e.getMessage());
            }
        }
    }

    protected abstract String titulo();

    protected abstract void cadastrar();

    protected abstract void listar();

    protected abstract void detalhar();

    protected abstract void editar();

    protected abstract void excluir();

    protected String opcoesExtras() {
        return "";
    }

    protected boolean opcaoExtra(int opcao) {
        return false;
    }
}
