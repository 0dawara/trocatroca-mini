package br.unifor.trocatroca.anuncio;

import br.unifor.trocatroca.infra.Entidade;
import br.unifor.trocatroca.infra.ValidacaoException;
import br.unifor.trocatroca.usuario.Usuario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Anuncio implements Entidade {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Long id;
    private String titulo;
    private String descricao;
    private Categoria categoria;
    private Condicao condicao;
    private String trocaDesejada;
    private final Usuario dono;
    private EstadoAnuncio estado = Disponivel.INSTANCIA;
    private final LocalDateTime criadoEm = LocalDateTime.now();

    public Anuncio(String titulo, String descricao, Categoria categoria, Condicao condicao, String trocaDesejada, Usuario dono) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.condicao = condicao;
        this.trocaDesejada = trocaDesejada;
        this.dono = dono;
        validar();
    }

    public void validar() {
        if (titulo == null || titulo.isBlank()) {
            throw new ValidacaoException("Título é obrigatório.");
        }
        if (titulo.length() > 60) {
            throw new ValidacaoException("Título deve ter até 60 caracteres.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new ValidacaoException("Descrição é obrigatória.");
        }
        if (dono == null) {
            throw new ValidacaoException("Dono é obrigatório.");
        }
    }

    public void reservar() {
        estado = estado.reservar();
    }

    public void concluirTroca() {
        estado = estado.concluirTroca();
    }

    public void reabrir() {
        estado = estado.reabrir();
    }

    public String resumo() {
        return String.format("#%d [%s] %s - %s/%s - @%s", id, estado.nome(), titulo, categoria, condicao, dono.getApelido());
    }

    public String detalhes() {
        return "Id: " + id
                + "\nTítulo: " + titulo
                + "\nDescrição: " + descricao
                + "\nCategoria: " + categoria
                + "\nCondição: " + condicao
                + "\nDeseja em troca: " + (trocaDesejada == null || trocaDesejada.isBlank() ? "-" : trocaDesejada)
                + "\nDono: @" + dono.getApelido() + " - " + dono.getNome()
                + "\nEstado: " + estado.nome()
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Condicao getCondicao() {
        return condicao;
    }

    public void setCondicao(Condicao condicao) {
        this.condicao = condicao;
    }

    public String getTrocaDesejada() {
        return trocaDesejada;
    }

    public void setTrocaDesejada(String trocaDesejada) {
        this.trocaDesejada = trocaDesejada;
    }

    public Usuario getDono() {
        return dono;
    }

    public EstadoAnuncio getEstado() {
        return estado;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
