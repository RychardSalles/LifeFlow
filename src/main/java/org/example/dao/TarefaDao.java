package org.example.dao;

import org.example.connection.Conexao;
import org.example.model.Tarefa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

public class TarefaDao {

    public void cadastrarTarefa(Tarefa tarefa){
        String sql = "INSERT INTO tarefas (titulo, descricao, data_tarefa, status_tarefa, usuario_id) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setDate(3, Date.valueOf(tarefa.getDataTarefa()));
            stmt.setString(4, tarefa.getStatusTarefa());
            stmt.setInt(5, tarefa.getUsuarioId());

            stmt.executeUpdate();

            System.out.println("Tarefa cadastrada com sucesso!");

            stmt.close();
            conexao.close();
        }catch (Exception e){
            System.out.println("Erro ao cadastrar tarefa");
            e.printStackTrace();
        }
    }
    public void listarTarefas() {
        String sql = "SELECT * FROM tarefas";

        try {
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                System.out.println("ID: " + resultado.getInt("id"));
                System.out.println("Título: " + resultado.getString("titulo"));
                System.out.println("Descrição: " + resultado.getString("descricao"));
                System.out.println("Data: " + resultado.getDate("data_tarefa"));
                System.out.println("Status: " + resultado.getString("status_tarefa"));
                System.out.println("Usuário ID: " + resultado.getInt("usuario_id"));
                System.out.println("---------------------------------");
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao listar tarefas");
            e.printStackTrace();
        }
    }

    public void atualizarTarefa(Tarefa tarefa) {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ?, data_tarefa = ?, status_tarefa = ?, usuario_id = ? WHERE id = ?";

        try {
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setDate(3, Date.valueOf(tarefa.getDataTarefa()));
            stmt.setString(4, tarefa.getStatusTarefa());
            stmt.setInt(5, tarefa.getUsuarioId());
            stmt.setInt(6, tarefa.getId());

            stmt.executeUpdate();

            System.out.println("Tarefa atualizada com sucesso!");

            stmt.close();
            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao atualizar tarefa");
            e.printStackTrace();
        }
    }

    public void deletarTarefa(int id) {
        String sql = "DELETE FROM tarefas WHERE id = ?";

        try {
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            stmt.executeUpdate();

            System.out.println("Tarefa deletada com sucesso!");

            stmt.close();
            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao deletar tarefa");
            e.printStackTrace();
        }
    }
}
