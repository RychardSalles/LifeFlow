package org.example.dao;
import org.example.model.Usuario;

import java.sql.*;

public class UsuarioDao {
    private final String url = "jdbc:mysql://localhost:3306/lifeflow";
    private final String usuarioBanco = "root";
    private final String senhaBanco = "0Turob25@";

    public void cadatraUsuario(Usuario Usuario) {
        String sql = "insert into usuarios (nome, email, senha) VALUE (?,?,?)";

        try {
            Connection conexao = DriverManager.getConnection(url, usuarioBanco, senhaBanco);

            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, Usuario.getNome());
            stmt.setString(2, Usuario.getEmail());
            stmt.setString(3, Usuario.getSenha());

            stmt.executeUpdate();

            System.out.println("Usuario cadastrado com sucesso!");

            stmt.close();
            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar Usuário");
            e.printStackTrace();
        }
    }

    public void listarUsuarios() {
        String sql = "SELECT * FROM usuarios";

        try {
            Connection conexao = DriverManager.getConnection(url, usuarioBanco, senhaBanco);
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                System.out.println("ID: " + resultado.getInt("id"));
                System.out.println("Nome: " + resultado.getString("nome"));
                System.out.println("Email: " + resultado.getString("email"));
                System.out.println("Senha: " + resultado.getString("senha"));
                System.out.println("---------------------------------");
            }

            resultado.close();
            stmt.close();
            conexao.close();

        }catch (Exception e) {
        System.out.println("Erro ao listar usuários");
        e.printStackTrace();
        }
}
    public void atualizarUsuario(Usuario usuario){
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";

        try{
            Connection conexao = DriverManager.getConnection(url, usuarioBanco, senhaBanco);
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, usuario.getId());

            stmt.executeUpdate();

            System.out.println("Usuário atualizado com sucesso!");

            stmt.close();
            conexao.close();
        }catch (Exception e){
            System.out.println("Erro ao atualizar usuário");
            e.printStackTrace();
        }
    }
    public void deletarUsuario(int id){
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try{
            Connection conexao = DriverManager.getConnection(url,usuarioBanco,senhaBanco);
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1,id);

            stmt.executeUpdate();

            System.out.println("Usuário deletado com sucesso!");

            stmt.close();
            conexao.close();
        }catch (Exception e){
            System.out.println("Erro ao deletar usuário");
            e.printStackTrace();
        }
    }
}
