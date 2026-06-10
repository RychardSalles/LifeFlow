package org.example.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Tarefa {

    private int id;
    private String titulo;
    private String descricao;
    private LocalDate dataTarefa;
    private String statusTarefa;
    private int usuarioId;
    private LocalTime horario;
    private String categoria;
    private String prioridade;

    public Tarefa(){
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitulo(){
        return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public LocalDate getDataTarefa(){
        return dataTarefa;
    }

    public void setDataTarefa(LocalDate dataTarefa){
        this.dataTarefa = dataTarefa;
    }

    public String getStatusTarefa(){
        return statusTarefa;
    }

    public void setStatusTarefa(String statusTarefa){
        this.statusTarefa = statusTarefa;
    }

    public int getUsuarioId(){
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId){
        this.usuarioId = usuarioId;
    }

    public LocalTime getHorario() {return horario;}

    public void setHorario(LocalTime horario){this.horario = horario;}

    public String getCategoria() {return categoria;}

    public void setCategoria(String categoria) {this.categoria = categoria;}

    public String getPrioridade() {return prioridade;}

    public void setPrioridade(String prioridade) {this.prioridade = prioridade;}
}
