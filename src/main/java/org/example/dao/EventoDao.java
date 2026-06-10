package org.example.dao;

import org.example.connection.Conexao;
import org.example.model.Evento;

import java.sql.*;

public class EventoDao {

    public void cadastrarEvento(Evento evento) {

        String sql =
                "INSERT INTO eventos(titulo, descricao, data_evento, local_evento, usuario_id) VALUES (?, ?, ?, ?, ?)";

        try {

            Connection conexao = Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(1, evento.getTitulo());
            stmt.setString(2, evento.getDescricao());

            stmt.setTimestamp(
                    3,
                    java.sql.Timestamp.valueOf(
                            evento.getDataEvento()
                    )
            );

            stmt.setString(4, evento.getLocalEvento());

            stmt.setInt(5, evento.getUsuarioId());

            stmt.executeUpdate();

            stmt.close();
            conexao.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarEventos() {

        String sql = "SELECT * FROM eventos";

        try {
            Connection conexao = Conexao.conectar();

            PreparedStatement stmt = conexao.prepareStatement(sql);

            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {

                System.out.println("ID: " + resultado.getInt("id"));
                System.out.println("Título: " + resultado.getString("titulo"));
                System.out.println("Descrição: " + resultado.getString("descricao"));
                System.out.println("Data Evento: " + resultado.getTimestamp("data_evento"));
                System.out.println("Usuário ID: " + resultado.getInt("usuario_id"));
                System.out.println("----------------------------------");
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao listar eventos");
            e.printStackTrace();
        }
    }

    public void atualizarEvento(Evento evento) {

        String sql = "UPDATE eventos SET titulo = ?, descricao = ?, data_evento = ?, usuario_id = ? WHERE id = ?";

        try {
            Connection conexao = Conexao.conectar();

            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, evento.getTitulo());
            stmt.setString(2, evento.getDescricao());
            stmt.setTimestamp(3, Timestamp.valueOf(evento.getDataEvento()));
            stmt.setInt(4, evento.getUsuarioId());
            stmt.setInt(5, evento.getId());

            stmt.executeUpdate();

            System.out.println("Evento atualizado com sucesso!");

            stmt.close();
            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao atualizar evento");
            e.printStackTrace();
        }
    }

    public void deletarEvento(int id) {

        String sql = "DELETE FROM eventos WHERE id = ?";

        try {
            Connection conexao = Conexao.conectar();

            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            stmt.executeUpdate();

            System.out.println("Evento deletado com sucesso!");

            stmt.close();
            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao deletar evento");
            e.printStackTrace();
        }
    }
}
