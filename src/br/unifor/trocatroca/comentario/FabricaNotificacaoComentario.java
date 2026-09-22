package br.unifor.trocatroca.comentario;

public interface FabricaNotificacaoComentario {

    ObservadorComentario criarNotificadorDono();

    ObservadorComentario criarRegistroAtividade();
}
