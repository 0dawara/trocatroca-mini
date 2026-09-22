package br.unifor.trocatroca.usuario;

import br.unifor.trocatroca.infra.ValidacaoException;

import java.util.List;
import java.util.function.Predicate;

public class UsuarioServico {

    private static final CriadorUsuario CRIADOR_COMPLETO = new CriadorUsuarioCompleto();
    private final UsuarioRepositorio repositorio;
    private final Predicate<Long> possuiAnuncios;

    public UsuarioServico(UsuarioRepositorio repositorio, Predicate<Long> possuiAnuncios) {
        this.repositorio = repositorio;
        this.possuiAnuncios = possuiAnuncios;
    }

    public Usuario criar(CriadorUsuario criador, String nome, String apelido, String email, String cidade, String bio, List<String> interesses) {
        verificarUnicidade(apelido, email, null);
        Usuario usuario = criador.criar(nome, apelido, email, cidade, bio, interesses);
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
        // valida o formato dos novos dados num objeto descartável antes de tocar na entidade persistida.
        CRIADOR_COMPLETO.criar(nome, apelido, email, cidade, bio, interesses);
        verificarUnicidade(apelido, email, id);
        usuario.setNome(nome);
        usuario.setApelido(apelido);
        usuario.setEmail(email);
        usuario.setCidade(cidade);
        usuario.setBio(bio);
        usuario.setInteresses(interesses);
        return repositorio.salvar(usuario);
    }

    public void excluir(Long id) {
        buscar(id);
        if (possuiAnuncios.test(id)) {
            throw new ValidacaoException("Usuário possui anúncios; exclua-os antes.");
        }
        repositorio.remover(id);
    }

    private void verificarUnicidade(String apelido, String email, Long idAtual) {
        boolean emailEmUso = repositorio.listar().stream()
                .anyMatch(u -> !u.getId().equals(idAtual) && u.getEmail().equalsIgnoreCase(email));
        if (emailEmUso) {
            throw new ValidacaoException("E-mail já cadastrado.");
        }
        boolean apelidoEmUso = repositorio.listar().stream()
                .anyMatch(u -> !u.getId().equals(idAtual) && u.getApelido().equalsIgnoreCase(apelido));
        if (apelidoEmUso) {
            throw new ValidacaoException("Apelido já em uso.");
        }
    }
}
