# Padrões de Projeto — TrocaTroca Mini

**Disciplina:** Projeto e Arquitetura de Sistemas  
**Professor:** Américo Sampaio  
**Equipe:** Equipe TrocaTroca  
- Francisco Alzir Lima Junior
- Leonardo Oliveira Freitas de Matos
- Thiago Leal Menezes

---

## 1. Visão Geral

O **TrocaTroca Mini** foi estruturado em três módulos principais de domínio — **Usuário**, **Anúncio** e **Comentário** —, além de uma camada de infraestrutura compartilhada. 

Para atender aos requisitos da disciplina, cada integrante ficou responsável por um cadastro contendo **exatamente 2 padrões GoF e 2 padrões GRASP**. Os padrões de infraestrutura geral são de uso comum e não computam na cota individual.

| Integrante | Módulo / Cadastro | Padrões GoF | Padrões GRASP |
|---|---|---|---|
| **Thiago Leal Menezes** | Usuário | Singleton, Factory Method | Creator, Controller |
| **Francisco Alzir Lima Junior** | Anúncio | Abstract Factory, Observer | Creator, Controller |
| **Leonardo Oliveira Freitas de Matos** | Comentário | Abstract Factory, Observer | Low Coupling, High Cohesion |

---

## 2. Thiago Leal Menezes — Cadastro de Usuários

### Padrões GoF

#### 1. Singleton (Criacional)
- **Onde foi aplicado:** `UsuarioRepositorio.getInstancia()`.
- **Problema resolvido:** Múltiplas partes do sistema precisam consultar ou validar usuários (por exemplo, `AnuncioServico` valida se o dono existe; `ComentarioServico` valida o autor do comentário; `UsuarioMenu` lista e edita). Se cada classe criasse ou recebesse instâncias separadas de repositório em memória, haveria risco de inconsistência de estado ou necessidade de repassar a dependência manualmente por múltiplas camadas.
- **Como funciona:** O construtor de `UsuarioRepositorio` é privado e o acesso ocorre pelo método estático thread-safe `getInstancia()`, garantindo um único ponto global de consulta e armazenamento em memória para usuários.

#### 2. Factory Method (Criacional)
- **Onde foi aplicado:** `CriadorUsuario` (classe abstrata), `CriadorUsuarioBasico` e `CriadorUsuarioCompleto`.
- **Problema resolvido:** O sistema possui duas modalidades de cadastro: **rápido** (apenas nome, apelido e e-mail) e **completo** (inclui cidade, biografia e lista de interesses). Em vez de poluir o menu com lógica condicional de instanciação ou construtores sobrecarregados espalhados, delegou-se a criação para classes especializadas.
- **Como funciona:** `CriadorUsuario` define a operação `criar(...)`, que atua como método template chamando o método fábrica protegido `instanciar(...)` e executando a validação obrigatória (`usuario.validar()`). As subclasses concretas decidem quais campos preencher, permitindo adicionar novos formatos de criação sem modificar o serviço nem os criadores existentes (Open/Closed Principle).

### Padrões GRASP

#### 1. Creator
- **Onde foi aplicado:** `UsuarioServico.criar(...)`.
- **Problema resolvido:** Determinar quem deve ser responsável pela criação de uma entidade `Usuario` no fluxo de negócio.
- **Como funciona:** O `UsuarioServico` possui a responsabilidade de coordenar a validação de regras de negócio (como unicidade de apelido e e-mail no repositório) antes da persistência. Ele recebe o `CriadorUsuario` escolhido pelo menu, valida os dados prévios e salva a entidade criada no repositório, mantendo o encapsulamento do ciclo de vida da entidade.

#### 2. Controller
- **Onde foi aplicado:** `UsuarioMenu`.
- **Problema resolvido:** Separar a captura de entrada do usuário via terminal da execução das operações de domínio.
- **Como funciona:** `UsuarioMenu` é o ponto de entrada da interface para o módulo de usuários. Ele apenas recebe as ações do console (ler strings, escolher opção do menu) e delega imediatamente as requisições ao `UsuarioServico`. Não há regras de validação cadastral ou persistência no menu.

---

## 3. Francisco Alzir Lima Junior — Cadastro de Anúncios

### Padrões GoF

#### 1. Abstract Factory (Criacional)
- **Onde foi aplicado:** `FabricaNotificacaoAnuncio` (interface), `NotificacaoAnuncioConsole` e `NotificacaoAnuncioSilenciosa`.
- **Problema resolvido:** O anúncio gera dois tipos de saídas ao mudar de estado (`DISPONIVEL` → `RESERVADO` → `TROCADO`): notificar o dono e registrar a mudança no histórico. No entanto, durante a carga inicial de dados (`DadosIniciais`), essas saídas devem ser suprimidas para não poluir o terminal, enquanto durante a navegação interativa devem ser impressas no console.
- **Como funciona:** A interface abstrata define os métodos `criarNotificadorDono()` e `criarRegistroDeEstado()`. O `Main` configura a fábrica silenciosa antes de carregar a massa de teste e depois alterna para a fábrica de console com uma única chamada (`anuncioServico.usarNotificacoes(...)`), garantindo que famílias completas e compatíveis de observadores sejam trocadas em tempo de execução.

#### 2. Observer (Comportamental)
- **Onde foi aplicado:** `ObservadorAnuncio` (interface do observador), `NotificadorDonoAnuncio` e `RegistroEstadoAnuncio` (observadores concretos), e `AnuncioServico` (sujeito notificador).
- **Problema resolvido:** `AnuncioServico` precisa avisar quando um anúncio é reservado, trocado ou reaberto, mas acoplar o serviço diretamente com `System.out` ou rotinas de log violaria o Princípio da Responsabilidade Única.
- **Como funciona:** `AnuncioServico` mantém uma lista de `ObservadorAnuncio`. Sempre que ocorrem transições de estado (`reservar`, `concluirTroca`, `reabrir`), o método `notificar(...)` dispara o evento `aoMudarEstado(anuncio, estadoAnterior)` para todos os observadores registrados, sem que o serviço saiba detalhes de exibição.

### Padrões GRASP

#### 1. Creator
- **Onde foi aplicado:** `AnuncioServico.criar(...)`.
- **Problema resolvido:** Atribuir a criação da instância de `Anuncio` à classe que agrega as informações necessárias para sua inicialização válida.
- **Como funciona:** Um anúncio exige um dono válido (entidade `Usuario`). O `AnuncioServico` recebe o `donoId`, valida sua existência consultando o `Repositorio<Usuario>`, instancia o `Anuncio` associado e o persiste no repositório.

#### 2. Controller
- **Onde foi aplicado:** `AnuncioMenu`.
- **Problema resolvido:** Mediar as operações de terminal do usuário sobre anúncios sem sobrecarregar a camada de domínio.
- **Como funciona:** O menu captura parâmetros de busca, filtros de categoria, condição de uso e solicitações de transição de estado, delegando cada operação diretamente para os métodos correspondentes de `AnuncioServico`.

---

## 4. Leonardo Oliveira Freitas de Matos — Cadastro de Comentários

### Padrões GoF

#### 1. Abstract Factory (Criacional)
- **Onde foi aplicado:** `FabricaNotificacaoComentario` (interface), `NotificacaoComentarioConsole` e `NotificacaoComentarioSilenciosa`.
- **Problema resolvido:** Similarmente ao módulo de anúncios, a publicação de comentários dispara avisos ao dono do anúncio e registros de atividade. Na inicialização automática do sistema, esses alertas devem ser silenciados, e durante o uso normal pelo usuário, devem ser encaminhados ao console.
- **Como funciona:** A fábrica abstrata define `criarNotificadorDono()` e `criarRegistroAtividade()`. As implementações concretas produzem objetos que realizam saída visual ou não fazem nada (no-op), permitindo que `ComentarioServico` alterne entre o modo silencioso e o interativo sem alterar nenhuma linha de sua lógica interna.

#### 2. Observer (Comportamental)
- **Onde foi aplicado:** `ObservadorComentario` (interface), `NotificadorDono` e `RegistroAtividadeComentario` (observadores concretos), e `ComentarioServico` (sujeito notificador).
- **Problema resolvido:** Quando um comentário é criado em um anúncio, o dono deve ser avisado (exceto se for o próprio dono comentando) e o log de atividades deve registrar o evento, sem que o serviço de comentários dependa de consoles ou formatos de impressão.
- **Como funciona:** O método `criar(...)` em `ComentarioServico` notifica os ouvintes chamando `o.aoNovoComentario(comentario)`. O `NotificadorDono` checa se o autor é diferente do dono e exibe a notificação formatada, enquanto o `RegistroAtividadeComentario` formata o log, garantindo extensibilidade (podendo-se adicionar envio por e-mail no futuro sem alterar o serviço).

### Padrões GRASP

#### 1. Low Coupling (Baixo Acoplamento)
- **Onde foi aplicado:** Estrutura e dependências de `ComentarioServico`.
- **Problema resolvido:** Evitar que o módulo de comentários dependa diretamente de implementações concretas dos outros módulos (como menus, serviços ou repositórios específicos de Usuário e Anúncio).
- **Como funciona:** `ComentarioServico` depende apenas de interfaces e abstrações: `Repositorio<Anuncio>`, `Repositorio<Usuario>`, `ObservadorComentario` e `FabricaNotificacaoComentario`. Isso garante que mudanças internas nos módulos de anúncio ou usuário não quebrem o serviço de comentários.

#### 2. High Cohesion (Alta Coesão)
- **Onde foi aplicado:** Separação entre `ComentarioServico`, `HistoricoComentario` e `ComentarioRepositorio`.
- **Problema resolvido:** Manter o serviço focado apenas em regras de negócio de comentários, sem acumular lógica de pilha de desfazer (undo) ou manipulação direta de estruturas de armazenamento.
- **Como funciona:** A responsabilidade de desfazer a última edição ou exclusão de comentário foi isolada em `HistoricoComentario`, que gerencia uma pilha interna de alterações (`Deque<Alteracao>`). O serviço apenas delega a gravação e o comando `desfazer()`, mantendo cada classe pequena, altamente focada e fácil de manter e testar.

---

## 5. Padrões Compartilhados (Infraestrutura Comum)

Para manter a consistência e evitar duplicação de código arquitetural, a camada `infra` adota padrões que servem de suporte para todos os módulos:

| Padrão | Tipo | Onde | Justificativa |
|---|---|---|---|
| **Template Method** | GoF | `MenuCrud<T>` | Define o esqueleto do fluxo de interação de um CRUD no terminal (exibir opções, ler escolha, chamar operação, tratar exceções amigavelmente). As subclasses (`UsuarioMenu`, `AnuncioMenu`, `ComentarioMenu`) implementam os passos específicos (`cadastrar`, `listar`, `detalhar`, `editar`, `excluir`) e hooks de opções extras. |
| **Singleton** | GoF | `Console` | Centraliza a leitura (`Scanner`) e escrita formatada no terminal através de uma única instância acessível via `Console.get()`, evitando conflitos na manipulação concorrente de `System.in`. |
| **Pure Fabrication** | GRASP | `Repositorio<T>` / `RepositorioEmMemoria<T>` | Gerenciamento de persistência em memória não é responsabilidade de negócio de entidades de domínio (`Usuario`, `Anuncio`, `Comentario`). Criou-se uma fabricação pura para abstrair operações de busca, salvamento e remoção. |
| **Indirection** | GRASP | Predicados injetados no `Main` (ex.: `anuncioRepo::existePorDono`) | Impede dependências circulares entre módulos para verificação de integridade referencial na exclusão. O `Main` conecta a checagem através de `Predicate<Long>` sem que `UsuarioServico` precise conhecer `AnuncioServico` ou `AnuncioRepositorio`. |
