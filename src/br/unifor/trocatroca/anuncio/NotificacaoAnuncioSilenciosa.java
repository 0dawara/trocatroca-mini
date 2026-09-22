package br.unifor.trocatroca.anuncio;

public class NotificacaoAnuncioSilenciosa implements FabricaNotificacaoAnuncio {

    @Override
    public ObservadorAnuncio criarNotificadorDono() {
        return new ObservadorAnuncioSilencioso();
    }

    @Override
    public ObservadorAnuncio criarRegistroDeEstado() {
        return new ObservadorAnuncioSilencioso();
    }
}
