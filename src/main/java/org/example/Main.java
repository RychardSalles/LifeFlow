package org.example;

import org.example.dao.TarefaDao;
import org.example.model.Tarefa;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        Tarefa tarefa = new Tarefa();

        tarefa.setTitulo("Estudar Java");
        tarefa.setDescricao("Fazer o DAO de tarefas");
        tarefa.setDataTarefa(LocalDate.of(2026, 6, 10));
        tarefa.setHorario(LocalTime.of(19, 30));
        tarefa.setStatusTarefa("Pendente");
        tarefa.setCategoria("Vida");
        tarefa.setPrioridade("Alta");
        tarefa.setUsuarioId(6);

        TarefaDao tarefaDao = new TarefaDao();

        tarefaDao.cadastrarTarefa(tarefa);
        tarefaDao.listarTarefas();
    }
}
