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
- **Linux/macOS**: execute `sh run.sh` na raiz do projeto (usa `javac`/`java` do `PATH`; requer JDK ≥ 21 instalado; não depende do bit de execução do arquivo).
- **Requisito**: JDK 25 (ou qualquer JDK ≥ 21; o código não usa recursos exclusivos do 25). O script `run.bat` detecta automaticamente o JDK instalado no sistema (verificando `JAVA_HOME`, `PATH`, diretórios do IntelliJ IDEA, Program Files, gerenciadores de pacotes como Scoop/Chocolatey e Registro do Windows, priorizando JDK 25 e versões recentes). Caso queira apontar para um JDK específico manualmente, defina a variável de ambiente `JAVA_HOME_OVERRIDE`.
- **IntelliJ IDEA**: abra a pasta `trocatroca-mini` como projeto e rode a classe `br.unifor.trocatroca.Main` (o source root `src` já está configurado).

## Cadastros

| Cadastro | Operações | Responsável |
|---|---|---|
| Usuário | Cadastrar, listar, detalhar, editar, excluir | Thiago |
| Anúncio | Cadastrar, listar (com filtros), detalhar, editar, excluir, reservar, concluir troca, reabrir | Alzir |
| Comentário | Cadastrar, listar, listar por anúncio, detalhar, editar, excluir, desfazer última edição/exclusão | Leonardo |

## Padrões utilizados

Cada integrante é responsável por um cadastro contendo exatamente **2 padrões GoF + 2 padrões GRASP**. Os padrões compartilhados abaixo pertencem à infraestrutura comum do projeto e **não contam na cota individual**.

### Compartilhados

| Padrão | Tipo | Onde | Por quê |
|---|---|---|---|
| Template Method | GoF | `MenuCrud<T>` | O fluxo do menu (mostrar opções, ler escolha, tratar erro) é fixo; cada cadastro só implementa os passos de CRUD (`cadastrar`, `listar`, etc.) e pode adicionar opções extras via hooks. |
| Singleton | GoF | `Console` | Uma única instância concentra leitura/escrita no terminal, evitando `Scanner`s concorrentes sobre `System.in`. |
| Pure Fabrication | GRASP | `Repositorio<T>` / `RepositorioEmMemoria<T>` | Persistência em memória não é responsabilidade natural de nenhuma entidade de domínio; foi isolada em uma classe fabricada para isso. |
| Indirection | GRASP | Predicados de exclusão injetados no `Main` (ex. `anuncioRepo::existePorDono`) | Evita que `usuario` dependa de `anuncio` (ou `anuncio` de `comentario`) para checar integridade referencial na exclusão; o `Main` liga as pontas. |

### Usuário — Thiago Leal Menezes

| Padrão | Tipo | Onde | Por quê |
|---|---|---|---|
| Singleton | GoF | `UsuarioRepositorio.getInstancia()` | Garante um único repositório de usuários compartilhado por toda a aplicação, sem passar a instância manualmente entre camadas. |
| Factory Method | GoF | `CriadorUsuario` (`CriadorUsuarioBasico` / `CriadorUsuarioCompleto`) | O menu escolhe o criador (`cadastro rápido` vs `cadastro completo`) e cada subclasse decide como instanciar o `Usuario`; o método template `criar` centraliza a validação comum. |
| Creator | GRASP | `UsuarioServico.criar` | O serviço que já agrega os dados necessários (repositório, regras de unicidade) é quem coordena a criação do `Usuario`, delegando a instanciação ao criador. |
| Controller | GRASP | `UsuarioMenu` | Recebe a interação do terminal e delega ao `UsuarioServico`, sem conter regra de negócio. |

### Anúncio — Francisco Alzir Lima Junior

| Padrão | Tipo | Onde | Por quê |
|---|---|---|---|
| Abstract Factory | GoF | `FabricaNotificacaoAnuncio` (`NotificacaoAnuncioConsole` / `NotificacaoAnuncioSilenciosa`) | Cria a família coerente de observadores (notificador do dono e registro de histórico); o `Main` troca a família inteira para carregar a massa inicial em silêncio. |
| Observer | GoF | `ObservadorAnuncio` (`NotificadorDonoAnuncio`, `RegistroEstadoAnuncio`) | O serviço avisa mudanças de estado sem conhecer o destino das mensagens; novos observadores podem ser plugados sem alterar o serviço. |
| Creator | GRASP | `AnuncioServico.criar` | O serviço que já tem as dependências necessárias (resolve o dono pelo id e valida) instancia o `Anuncio`. |
| Controller | GRASP | `AnuncioMenu` | Traduz a interação do terminal em chamadas ao `AnuncioServico`. |

### Comentário — Leonardo Oliveira Freitas de Matos

| Padrão | Tipo | Onde | Por quê |
|---|---|---|---|
| Abstract Factory | GoF | `FabricaNotificacaoComentario` (`NotificacaoComentarioConsole` / `NotificacaoComentarioSilenciosa`) | Cria a família coerente de observadores (notificador do dono e registro de atividade); permite alternar entre saída em console e execução silenciosa na carga inicial. |
| Observer | GoF | `ObservadorComentario` (`NotificadorDono`, `RegistroAtividadeComentario`) | O dono do anúncio é avisado de novos comentários e a atividade é registrada sem que `ComentarioServico` conheça detalhes da saída. |
| Low Coupling | GRASP | `ComentarioServico` | Depende apenas de abstrações (`Repositorio<Anuncio>`, `Repositorio<Usuario>`, `ObservadorComentario`, `FabricaNotificacaoComentario`), nunca dos menus ou serviços concretos de outros módulos. |
| High Cohesion | GRASP | `HistoricoComentario` e separação de responsabilidades | Responsabilidades claramente isoladas em classes coesas: `ComentarioServico` cuida das regras de negócio, `HistoricoComentario` gerencia o desfazer, `ComentarioRepositorio` a persistência e os observadores a saída. |

## Prints

Colocar as capturas de tela em `docs/prints/` (arquivo `.gitkeep` reservando a pasta), cobrindo pelo menos:

- Menu principal.
- Listar e cadastrar de cada cadastro (Usuário, Anúncio, Comentário).
- Transição de estado de um anúncio (reservar/concluir troca/reabrir).
- Notificação ao dono ao cadastrar um comentário.
- Notificação ao dono e registro no histórico ao reservar ou concluir a troca de um anúncio.
- Desfazer de uma edição ou exclusão de comentário.
