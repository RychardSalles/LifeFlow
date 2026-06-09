package org.example;

import org.example.dao.TarefaDao;
import org.example.model.Tarefa;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Tarefa tarefa = new Tarefa();

        tarefa.setTitulo("Estudar Java");
        tarefa.setDescricao("Fazer o DAO de tarefas");
        tarefa.setDataTarefa(LocalDate.of(2026, 6, ));
        tarefa.setStatusTarefa("Pendente");
        tarefa.setUsuarioId(6);

        TarefaDao tarefaDao = new TarefaDao();

        tarefaDao.cadastrarTarefa(tarefa);
        tarefaDao.listarTarefas();
    }
}
