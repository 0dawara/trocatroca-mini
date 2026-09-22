package br.unifor.trocatroca.usuario;

import br.unifor.trocatroca.infra.Entidade;
import br.unifor.trocatroca.infra.ValidacaoException;

import java.util.ArrayList;
import java.util.List;

public class Usuario implements Entidade {

    private Long id;
    private String nome;
    private String apelido;
    private String email;
    private String cidade;
    private String bio;
    private List<String> interesses;

    public Usuario(String nome, String apelido, String email, String cidade, String bio, List<String> interesses) {
        this.nome = nome;
        this.apelido = apelido;
        this.email = email;
        this.cidade = cidade;
        this.bio = bio;
        this.interesses = interesses == null ? new ArrayList<>() : new ArrayList<>(interesses);
    }

    public void validar() {
        if (nome == null || nome.isBlank()) {
            throw new ValidacaoException("Nome é obrigatório.");
        }
        if (apelido == null || apelido.isBlank()) {
            throw new ValidacaoException("Apelido não pode conter espaços.");
        }
        if (apelido.contains(" ")) {
            throw new ValidacaoException("Apelido não pode conter espaços.");
        }
        if (email == null || !email.contains("@") || !email.substring(email.indexOf('@') + 1).contains(".")) {
            throw new ValidacaoException("E-mail inválido.");
        }
    }

    public String resumo() {
        return String.format("#%d @%s - %s (%s)", id, apelido, nome,
                (cidade == null || cidade.isBlank()) ? "sem cidade" : cidade);
    }

    public String detalhes() {
        return "Id: " + id
                + "\nNome: " + nome
                + "\nApelido: " + apelido
                + "\nE-mail: " + email
                + "\nCidade: " + (cidade == null || cidade.isBlank() ? "-" : cidade)
                + "\nBio: " + (bio == null || bio.isBlank() ? "-" : bio)
                + "\nInteresses: " + (interesses.isEmpty() ? "-" : String.join(", ", interesses));
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getInteresses() {
        return interesses;
    }

    public void setInteresses(List<String> interesses) {
        this.interesses = interesses;
    }

}
