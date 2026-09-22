package br.unifor.trocatroca.comentario;

public class NotificacaoComentarioSilenciosa implements FabricaNotificacaoComentario {

    @Override
    public ObservadorComentario criarNotificadorDono() {
        return new ObservadorComentarioSilencioso();
    }

    @Override
    public ObservadorComentario criarRegistroAtividade() {
        return new ObservadorComentarioSilencioso();
    }
}
