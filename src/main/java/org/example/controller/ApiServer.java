package org.example.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.example.dao.EventoDao;
import org.example.dao.TarefaDao;
import org.example.dao.UsuarioDao;
import org.example.model.Evento;
import org.example.model.Tarefa;
import org.example.model.Usuario;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class ApiServer {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {
            responder(exchange, 200, "{\"mensagem\":\"API LifeFlow rodando\"}");
        });

        server.createContext("/api/cadastro", exchange -> {
            if (isOptions(exchange)) return;

            if ("POST".equals(exchange.getRequestMethod())) {
                String json = lerBody(exchange);

                Usuario usuario = new Usuario();
                usuario.setNome(pegarValor(json, "nome"));
                usuario.setEmail(pegarValor(json, "email"));
                String senha = pegarValor(json, "senha");

                if(senha.isEmpty()) {
                    senha = pegarValor(json, "password");
                }

                usuario.setSenha(senha);

                UsuarioDao usuarioDao = new UsuarioDao();
                usuarioDao.cadastrarUsuario(usuario);

                responder(exchange, 200, "{\"mensagem\":\"Cadastro realizado com sucesso!\"}");
            } else {
                responder(exchange, 405, "{\"erro\":\"Método não permitido\"}");
            }
        });

        server.createContext("/api/login", exchange -> {
            if (isOptions(exchange)) return;

            if ("POST".equals(exchange.getRequestMethod())) {
                String json = lerBody(exchange);

                String email = pegarValor(json, "email");
                String senha = pegarValor(json, "senha");
                if (senha.isEmpty()){
                    senha = pegarValor(json, "password");
                }

                UsuarioDao usuarioDao = new UsuarioDao();
                boolean loginValido = usuarioDao.fazerLogin(email, senha);

                if (loginValido) {
                    responder(exchange, 200, "{\"mensagem\":\"Login realizado com sucesso!\"}");
                } else {
                    responder(exchange, 401, "{\"erro\":\"Email ou senha inválidos\"}");
                }
            } else {
                responder(exchange, 405, "{\"erro\":\"Método não permitido\"}");
            }
        });

        server.createContext("/api/tarefas", exchange -> {
            if (isOptions(exchange)) return;

            if ("POST".equals(exchange.getRequestMethod())) {
                String json = lerBody(exchange);

                Tarefa tarefa = new Tarefa();
                tarefa.setTitulo(pegarValor(json, "titulo"));
                tarefa.setDescricao(pegarValor(json, "descricao"));
                tarefa.setDataTarefa(LocalDate.parse(pegarValor(json, "dataTarefa")));
                tarefa.setStatusTarefa(pegarValor(json, "statusTarefa"));
                tarefa.setUsuarioId(Integer.parseInt(pegarValor(json, "usuarioId")));

                TarefaDao tarefaDao = new TarefaDao();
                tarefaDao.cadastrarTarefa(tarefa);

                responder(exchange, 200, "{\"mensagem\":\"Tarefa cadastrada com sucesso!\"}");
            } else {
                responder(exchange, 405, "{\"erro\":\"Método não permitido\"}");
            }
        });

        server.createContext("/api/eventos", exchange -> {
            if (isOptions(exchange)) return;

            if ("POST".equals(exchange.getRequestMethod())) {
                String json = lerBody(exchange);

                Evento evento = new Evento();
                evento.setTitulo(pegarValor(json, "titulo"));
                evento.setDescricao(pegarValor(json, "descricao"));
                evento.setDataEvento(LocalDateTime.parse(pegarValor(json, "dataEvento")));
                evento.setUsuarioId(Integer.parseInt(pegarValor(json, "usuarioId")));

                EventoDao eventoDao = new EventoDao();
                eventoDao.cadastrarEvento(evento);

                responder(exchange, 200, "{\"mensagem\":\"Evento cadastrado com sucesso!\"}");
            } else {
                responder(exchange, 405, "{\"erro\":\"Método não permitido\"}");
            }
        });

        server.start();
        System.out.println("API rodando em http://localhost:8080");
    }

    private static boolean isOptions(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            responder(exchange, 200, "");
            return true;
        }
        return false;
    }

    private static String lerBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes());
    }

    private static String pegarValor(String json, String campo) {
        String busca = "\"" + campo + "\":\"";
        int inicio = json.indexOf(busca);

        if (inicio == -1) {
            return "";
        }

        inicio += busca.length();
        int fim = json.indexOf("\"", inicio);

        return json.substring(inicio, fim);
    }

    private static void responder(HttpExchange exchange, int status, String resposta) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        byte[] bytes = resposta.getBytes();
        exchange.sendResponseHeaders(status, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}