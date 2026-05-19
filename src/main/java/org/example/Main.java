package org.example;

import org.example.model.Usuario;

public class Main {
    public static void main(String[] args) {

        Usuario usuario = new Usuario();

        usuario.setId(1);
        usuario.setNome("Rychard");
        usuario.setEmail("rychard@email.com");
        usuario.setSenha("123456");

        System.out.println("ID: " + usuario.getId());
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Senha: " + usuario.getSenha());
    }
}
