package org.example.model;

import java.time.LocalDateTime;

public class Evento {

    private int id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataEvento;
    private String localEvento;
    private int usuarioId;

    public Evento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDateTime dataEvento) {
        this.dataEvento = dataEvento;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getLocalEvento() {return localEvento;}

    public void setLocalEvento(String localEvento) {this.localEvento = localEvento;}
}