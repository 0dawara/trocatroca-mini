package br.unifor.trocatroca.usuario;

import br.unifor.trocatroca.infra.ValidacaoException;

import java.util.List;
import java.util.function.Predicate;

public class UsuarioServico {

    private final UsuarioRepositorio repositorio;
    private final Predicate<Long> possuiAnuncios;

    public UsuarioServico(UsuarioRepositorio repositorio, Predicate<Long> possuiAnuncios) {
        this.repositorio = repositorio;
        this.possuiAnuncios = possuiAnuncios;
    }

    public Usuario criar(String nome, String apelido, String email, String cidade, String bio, List<String> interesses) {
        Usuario usuario = Usuario.builder()
                .nome(nome).apelido(apelido).email(email).cidade(cidade).bio(bio).interesses(interesses)
                .build();
        verificarUnicidade(usuario, null);
        return repositorio.salvar(usuario);
    }

    public Usuario buscar(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new ValidacaoException("Usuário #" + id + " não encontrado."));
    }

    public List<Usuario> listar() {
        return repositorio.listar();
    }

    public Usuario editar(Long id, String nome, String apelido, String email, String cidade, String bio, List<String> interesses) {
        Usuario usuario = buscar(id);
        usuario.setNome(nome);
        usuario.setApelido(apelido);
        usuario.setEmail(email);
        usuario.setCidade(cidade);
        usuario.setBio(bio);
        usuario.setInteresses(interesses);
        usuario.validar();
        verificarUnicidade(usuario, id);
        return repositorio.salvar(usuario);
    }

    public void excluir(Long id) {
        buscar(id);
        if (possuiAnuncios.test(id)) {
            throw new ValidacaoException("Usuário possui anúncios; exclua-os antes.");
        }
        repositorio.remover(id);
    }

    private void verificarUnicidade(Usuario usuario, Long idAtual) {
        repositorio.buscarPorEmail(usuario.getEmail())
                .filter(outro -> !outro.getId().equals(idAtual))
                .ifPresent(outro -> {
                    throw new ValidacaoException("E-mail já cadastrado.");
                });
        repositorio.buscarPorApelido(usuario.getApelido())
                .filter(outro -> !outro.getId().equals(idAtual))
                .ifPresent(outro -> {
                    throw new ValidacaoException("Apelido já em uso.");
                });
    }
}
