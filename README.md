# TrocaTroca Mini — Equipe TrocaTroca

## Integrantes

- Francisco Alzir Lima Junior — [github.com/alzirlima](https://github.com/alzirlima)
- Leonardo Oliveira Freitas de Matos — [github.com/oliveiraleonardo918-ui](https://github.com/oliveiraleonardo918-ui)
- Thiago Leal Menezes — [github.com/0dawara](https://github.com/0dawara)

## Sobre

TrocaTroca é uma plataforma de escambo peer-to-peer: em vez de comprar e vender com dinheiro, cada pessoa anuncia um item que possui e diz o que gostaria de receber em troca, e o combinado é fechado diretamente entre as duas partes através de comentários públicos no anúncio.

O TrocaTroca Mini é uma versão terminal, em memória, do núcleo desse sistema, desenvolvida como Trabalho 1 da disciplina Projeto e Arquitetura de Sistemas. Ela reúne três cadastros — Usuário, Anúncio e Comentário — cada um implementado com um conjunto de padrões de projeto GoF e GRASP, documentados na seção "Padrões utilizados" abaixo.

## Como executar

- **Windows**: execute `run.bat` na raiz do projeto (compila e roda automaticamente).
- **Requisito**: JDK 25 (ou qualquer JDK ≥ 21; o código não usa recursos exclusivos do 25). O script procura o JDK em `%USERPROFILE%\.jdks\temurin-25.0.4.1`; se não existir, defina a variável de ambiente `JAVA_HOME_OVERRIDE` apontando para outro JDK instalado.
- **IntelliJ IDEA**: abra a pasta `trocatroca-mini` como projeto e rode a classe `br.unifor.trocatroca.Main` (o source root `src` já está configurado).

## Cadastros

| Cadastro | Operações | Responsável |
|---|---|---|
| Usuário | Cadastrar, listar, detalhar, editar, excluir | Thiago |
| Anúncio | Cadastrar, listar (com filtros), detalhar, editar, excluir, reservar, concluir troca, reabrir | Alzir |
| Comentário | Cadastrar, listar, listar por anúncio, detalhar, editar, excluir, desfazer última edição/exclusão | Leonardo |

## Padrões utilizados
