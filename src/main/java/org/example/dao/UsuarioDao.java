package org.example.dao;

import org.example.connection.Conexao;
import org.example.model.Usuario;
import java.sql.*;

public class UsuarioDao {

    public void cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nome, email, senha) VALUES (?, ?, ?)";
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarUsuarios() {
        String sql = "SELECT * FROM usuarios";
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                System.out.println("ID: " + resultado.getInt("id"));
                System.out.println("Nome: " + resultado.getString("nome"));
                System.out.println("Email: " + resultado.getString("email"));
                System.out.println("Senha: " + resultado.getString("senha"));
                System.out.println("---------------------------------");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar usuários");
            e.printStackTrace();
        }
    }

    public void atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
            System.out.println("Usuário atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar usuário");
            e.printStackTrace();
        }
    }

    public void deletarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Usuário deletado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao deletar usuário");
            e.printStackTrace();
        }
    }

    /**
     * Valida as credenciais e retorna o objeto Usuario completo.
     * Retorna null caso o e-mail ou senha estejam incorretos.
     */
    public Usuario validarLogin(String email, String senha) {
        String sql = "SELECT id, nome, email, senha FROM usuarios WHERE email = ? AND senha = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    return usuario;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao validar login no banco de dados.");
            e.printStackTrace();
        }
        return null;
    }

    public boolean fazerLogin(String email, String senha) {
        String sql = "SELECT 1 FROM usuarios WHERE email = ? AND senha = ?";
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
