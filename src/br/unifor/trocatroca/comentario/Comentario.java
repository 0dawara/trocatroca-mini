package br.unifor.trocatroca.comentario;

import br.unifor.trocatroca.anuncio.Anuncio;
import br.unifor.trocatroca.infra.Entidade;
import br.unifor.trocatroca.infra.ValidacaoException;
import br.unifor.trocatroca.usuario.Usuario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comentario implements Entidade {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Long id;
    private final Anuncio anuncio;
    private final Usuario autor;
    private String conteudo;
    private final LocalDateTime criadoEm = LocalDateTime.now();

    public Comentario(Anuncio anuncio, Usuario autor, String conteudo) {
        this.anuncio = anuncio;
        this.autor = autor;
        this.conteudo = conteudo;
        validar();
    }

    public void validar() {
        if (conteudo == null || conteudo.isBlank()) {
            throw new ValidacaoException("Comentário não pode ser vazio.");
        }
        if (conteudo.length() > 500) {
            throw new ValidacaoException("Comentário deve ter até 500 caracteres.");
        }
    }

    public String resumo() {
        String texto = conteudo.length() > 40 ? conteudo.substring(0, 40) + "..." : conteudo;
        return String.format("#%d [anúncio #%d %s] @%s: %s", id, anuncio.getId(), anuncio.getTitulo(), autor.getApelido(), texto);
    }

    public String detalhes() {
        return "Id: " + id
                + "\nAnúncio: #" + anuncio.getId() + " - " + anuncio.getTitulo()
                + "\nAutor: @" + autor.getApelido()
                + "\nConteúdo: " + conteudo
                + "\nCriado em: " + criadoEm.format(FORMATO_DATA);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public Usuario getAutor() {
        return autor;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
