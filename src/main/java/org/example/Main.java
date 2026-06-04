package org.example;

import org.example.dao.UsuarioDao;
import org.example.model.Usuario;

public class Main {
    public static void main(String[] args) {

        Usuario usuario = new Usuario();

        usuario.setId(1);
        usuario.setNome("Rychard");
        usuario.setEmail("rd@email.com");
        usuario.setSenha("123456");

        UsuarioDao usuarioDao = new UsuarioDao();

        usuarioDao.cadatraUsuario(usuario);
        usuarioDao.listarUsuarios();
    }
}
