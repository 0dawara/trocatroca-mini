package br.unifor.trocatroca.infra;

import br.unifor.trocatroca.anuncio.AnuncioServico;
import br.unifor.trocatroca.anuncio.Categoria;
import br.unifor.trocatroca.anuncio.Condicao;
import br.unifor.trocatroca.comentario.ComentarioServico;
import br.unifor.trocatroca.usuario.UsuarioServico;
import br.unifor.trocatroca.usuario.CriadorUsuarioCompleto;

import java.util.List;

public final class DadosIniciais {

    private DadosIniciais() {
    }

    public static void carregar(UsuarioServico usuarios, AnuncioServico anuncios, ComentarioServico comentarios) {
        usuarios.criar(new CriadorUsuarioCompleto(), "Francisco Alzir Lima Junior", "alzir", "alzir@unifor.br", "Fortaleza",
                "Colecionador de games retrô", List.of("Jogos", "Eletrônicos"));
        usuarios.criar(new CriadorUsuarioCompleto(), "Leonardo Oliveira Freitas de Matos", "leo", "leo@unifor.br", "Fortaleza",
                "Leitor voraz", List.of("Livros", "Quadrinhos"));
        usuarios.criar(new CriadorUsuarioCompleto(), "Thiago Leal Menezes", "thiago", "thiago@unifor.br", "Fortaleza",
                "Desapegando do que não uso", List.of("Livros", "Roupas"));

        anuncios.criar("Livro Clean Code", "Edição em inglês, capa levemente amassada",
                Categoria.LIVROS, Condicao.SEMINOVO, "Design Patterns (GoF)", 3L);
        anuncios.criar("Controle Xbox Series", "Funcionando, sem pilhas",
                Categoria.JOGOS, Condicao.USADO, "Jogo de PS5", 1L);
        anuncios.criar("Camisa Ceará 2023", "Tamanho M, nunca usada",
                Categoria.ROUPAS, Condicao.NOVO, "Camisa de outro time tamanho M", 2L);
        anuncios.criar("Álbum Copa 2022 completo", "Todas as figurinhas coladas",
                Categoria.COLECIONAVEIS, Condicao.SEMINOVO, "Álbum Copa 2018", 1L);
        anuncios.reservar(4L);

        comentarios.criar(1L, 1L, "Aceita troca por Refactoring?");
        comentarios.criar(1L, 2L, "Tem marca de caneta nas páginas?");
        comentarios.criar(2L, 3L, "Ainda está disponível?");
        comentarios.criar(3L, 1L, "Topo trocar por camisa do Fortaleza M");
    }
}
