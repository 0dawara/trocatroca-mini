package br.unifor.trocatroca.comentario;

public interface Comando {

    void executar();

    void desfazer();

    String descricao();
}
