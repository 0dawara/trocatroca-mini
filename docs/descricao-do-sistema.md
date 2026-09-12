# Descrição do Sistema — TrocaTroca Mini

**Disciplina:** Projeto e Arquitetura de Sistemas
**Professor:** Américo Sampaio
**Equipe:** Equipe TrocaTroca — Francisco Alzir Lima Junior, Leonardo Oliveira Freitas de Matos, Thiago Leal Menezes

## Propósito

O TrocaTroca é uma plataforma de escambo peer-to-peer: pessoas anunciam itens que possuem e não usam mais, descrevem o que gostariam de receber em troca, e o acordo é fechado diretamente entre as duas partes, sem intermediação financeira. A proposta reduz o descarte de itens em bom estado e cria uma alternativa à compra e venda tradicional para quem prefere negociar por troca direta.

O TrocaTroca Mini é o núcleo desse sistema, reimplementado como aplicação de terminal em Java para o Trabalho 1 da disciplina. Ele concentra os três cadastros que sustentam o fluxo de escambo — usuários, anúncios e comentários — deixando de fora, propositalmente, tudo que depende de infraestrutura externa (autenticação, persistência em banco, notificações por e-mail, etc.), para que o foco da entrega fique nos padrões de projeto GoF e GRASP aplicados a cada cadastro.

## Principais funcionalidades

- **Cadastro de usuários**: qualquer pessoa que participa do sistema — seja para anunciar um item, seja para comentar interesse em um anúncio de outra pessoa — é um usuário. O cadastro cobre nome, apelido único, e-mail único, cidade, uma breve biografia e uma lista de interesses, com validação de unicidade de apelido e e-mail.
- **Cadastro de anúncios**: cada anúncio pertence a um usuário (o dono), descreve o item ofertado (título, descrição, categoria e condição de uso) e o que se deseja receber em troca. Um anúncio percorre um ciclo de vida simples — `DISPONIVEL` → `RESERVADO` → `TROCADO`, com a possibilidade de reabrir uma reserva — e pode ser filtrado por categoria, por dono ou por estado ao ser listado.
- **Cadastro de comentários**: cada anúncio tem um mural público de comentários, onde qualquer usuário interessado pode se manifestar (perguntar sobre o item, propor condições, confirmar interesse). Ao registrar um novo comentário, o dono do anúncio é notificado (exceto quando o próprio dono comenta em seu anúncio). Edições e exclusões de comentário podem ser desfeitas, recuperando o estado anterior.

## Principais usuários

O sistema não distingue papéis administrativos: toda pessoa cadastrada é um "usuário" e pode, ao mesmo tempo, ser:

- **Anunciante/dono**: quando cria um anúncio para um item que possui.
- **Interessado/comentarista**: quando comenta em anúncios de terceiros manifestando interesse em uma troca.

Essa simetria reflete o próprio modelo de escambo P2P: não existe comprador nem vendedor fixos, apenas duas partes trocando papéis conforme o anúncio.

## Escopo desta entrega

Esta versão roda inteiramente no terminal, com dados mantidos em memória (perdidos ao encerrar o programa) e uma pequena massa de dados inicial carregada na inicialização para permitir a navegação e os testes sem cadastro manual prévio. Não há tela de login: o sistema assume que qualquer usuário listado pode operar qualquer cadastro, já que o objetivo da entrega é demonstrar os padrões de projeto por trás de cada CRUD, não um controle de acesso completo. A integridade referencial entre os três cadastros (por exemplo, impedir a exclusão de um usuário que ainda possui anúncios) é garantida por predicados injetados no ponto de composição da aplicação, mantendo a direção de dependência sempre de comentário para anúncio e de anúncio para usuário — nunca o inverso.
