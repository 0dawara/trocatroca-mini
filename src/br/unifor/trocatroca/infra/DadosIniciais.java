package br.unifor.trocatroca.infra;

import br.unifor.trocatroca.usuario.UsuarioServico;

import java.util.List;

public final class DadosIniciais {

    private DadosIniciais() {
    }

    public static void carregar(UsuarioServico usuarios) {
        usuarios.criar("Francisco Alzir Lima Junior", "alzir", "alzir@unifor.br", "Fortaleza",
                "Colecionador de games retrô", List.of("Jogos", "Eletrônicos"));
        usuarios.criar("Leonardo Oliveira Freitas de Matos", "leo", "leo@unifor.br", "Fortaleza",
                "Leitor voraz", List.of("Livros", "Quadrinhos"));
        usuarios.criar("Thiago Leal Menezes", "thiago", "thiago@unifor.br", "Fortaleza",
                "Desapegando do que não uso", List.of("Livros", "Roupas"));
    }
}
