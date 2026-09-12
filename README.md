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

### Compartilhados

| Padrão | Tipo | Onde | Por quê |
|---|---|---|---|
| Template Method | GoF | `MenuCrud<T>` | O fluxo do menu (mostrar opções, ler escolha, tratar erro) é fixo; cada cadastro só implementa os passos de CRUD (`cadastrar`, `listar`, etc.) e pode adicionar opções extras via hooks. |
| Singleton | GoF | `Console` | Uma única instância concentra leitura/escrita no terminal, evitando `Scanner`s concorrentes sobre `System.in`. |
| Pure Fabrication | GRASP | `Repositorio<T>` / `RepositorioEmMemoria<T>` | Persistência em memória não é responsabilidade natural de nenhuma entidade de domínio; foi isolada em uma classe fabricada para isso. |
| Indirection | GRASP | Predicados de exclusão injetados no `Main` (ex. `anuncioRepo::existePorDono`) | Evita que `usuario` dependa de `anuncio` (ou `anuncio` de `comentario`) para checar integridade referencial na exclusão; o `Main` liga as pontas. |

### Usuário

| Padrão | Tipo | Onde | Por quê |
|---|---|---|---|
| Singleton | GoF | `UsuarioRepositorio.getInstancia()` | Garante um único repositório de usuários compartilhado por toda a aplicação, sem passar a instância manualmente entre camadas. |
| Builder | GoF | `Usuario.Builder` | `Usuario` tem vários campos opcionais (cidade, bio, interesses); o Builder monta o objeto passo a passo e centraliza a chamada a `validar()` na construção. |
| Controller | GRASP | `UsuarioMenu` | Recebe a interação do terminal e delega ao `UsuarioServico`, sem conter regra de negócio. |
| Creator | GRASP | `UsuarioServico.criar` | O serviço que já agrega os dados necessários (repositório, regras de unicidade) é quem monta o `Usuario`. |
| Information Expert | GRASP | `Usuario.validar()` | A própria entidade é quem tem os dados para decidir se nome, apelido e e-mail são válidos. |
| Pure Fabrication | GRASP | `UsuarioRepositorio` | Isola a persistência em memória do usuário, mantendo a entidade livre de lógica de armazenamento. |

### Anúncio

| Padrão | Tipo | Onde | Por quê |
|---|---|---|---|
| State | GoF | `EstadoAnuncio` + `Disponivel`/`Reservado`/`Trocado` | O comportamento de `reservar`/`concluirTroca`/`reabrir` muda conforme o estado atual; cada transição válida (e cada erro) fica isolada na classe do estado correspondente, sem `if/switch` espalhado. |
| Strategy | GoF | `FiltroAnuncio` + `FiltroPorCategoria`/`FiltroPorDono`/`FiltroPorEstado` | A forma de filtrar a listagem de anúncios varia; cada critério é uma estratégia intercambiável escolhida em tempo de execução pelo menu. |
| Controller | GRASP | `AnuncioMenu` | Traduz a interação do terminal em chamadas ao `AnuncioServico`. |
| Polymorphism | GRASP | Estados (`EstadoAnuncio`) e filtros (`FiltroAnuncio`) | Cada implementação responde de forma própria a `reservar()`/`aceita()`, eliminando condicionais por tipo. |
| Low Coupling | GRASP | `AnuncioServico` depende de `Repositorio<Usuario>`, não de `UsuarioMenu` ou `UsuarioServico` | O módulo de anúncio só conhece a abstração de repositório de usuários, reduzindo o acoplamento entre módulos. |
| Pure Fabrication | GRASP | `AnuncioRepositorio` | Isola a persistência em memória do anúncio. |

### Comentário

| Padrão | Tipo | Onde | Por quê |
|---|---|---|---|
| Observer | GoF | `ObservadorComentario` / `NotificadorDono` | O dono do anúncio precisa ser avisado de novos comentários sem que `ComentarioServico` conheça detalhes de como a notificação é entregue; novos observadores podem ser plugados sem alterar o serviço. |
| Command | GoF | `Comando`, `ComandoEditarComentario`, `ComandoExcluirComentario` | Editar e excluir viram objetos executáveis com `desfazer()`, permitindo empilhar um histórico e reverter a última operação. |
| Controller | GRASP | `ComentarioMenu` | Traduz a interação do terminal em chamadas ao `ComentarioServico`. |
| Creator | GRASP | `ComentarioServico.criar` | O serviço já tem os dados (anúncio e autor resolvidos) para montar o `Comentario`. |
| Protected Variations | GRASP | Lista de `ObservadorComentario` em `ComentarioServico` | Novos tipos de notificação são adicionados via `adicionarObservador`, sem alterar o código do serviço. |
| Pure Fabrication | GRASP | `ComentarioRepositorio` | Isola a persistência em memória do comentário. |
