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
import java.time.LocalTime;
import java.util.List;


public class ApiServer {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {
            responder(exchange, 200, "{\"mensagem\":\"API LifeFlow rodando\"}");
        });

        server.createContext("/api/login", exchange -> {
            try {
                if (isOptions(exchange)) return;

                if ("POST".equals(exchange.getRequestMethod())) {
                    String json = lerBody(exchange);
                    String email = pegarValor(json, "email");
                    String password = pegarValor(json, "password");

                    UsuarioDao usuarioDao = new UsuarioDao();
                    Usuario usuario = usuarioDao.validarLogin(email, password);

                    if (usuario != null) {
                        String resposta = String.format(
                                "{\"token\":\"token-logado-sucesso\",\"nome\":\"%s\",\"id\":%d,\"email\":\"%s\"}",
                                usuario.getNome(), usuario.getId(), usuario.getEmail()
                        );
                        responder(exchange, 200, resposta);
                    } else {
                        responder(exchange, 401, "{\"erro\":\"E-mail ou senha incorretos\"}");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                responder(exchange, 500, "{\"erro\":\"Erro interno no login\"}");
            }
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


        server.createContext("/api/tarefas", exchange -> {
            try {
                if (isOptions(exchange)) return;

                if ("POST".equals(exchange.getRequestMethod())) {
                    String json = lerBody(exchange);
                    Tarefa tarefa = new Tarefa();
                    tarefa.setTitulo(pegarValor(json, "titulo"));
                    tarefa.setDescricao(pegarValor(json, "descricao"));
                    String dataStr = pegarValor(json, "dataTarefa");
                    if (!dataStr.isEmpty()) tarefa.setDataTarefa(LocalDate.parse(dataStr));
                    String horarioStr = pegarValor(json, "horario");
                    if (!horarioStr.isEmpty()) tarefa.setHorario(LocalTime.parse(horarioStr));
                    tarefa.setCategoria(pegarValor(json, "categoria"));
                    tarefa.setPrioridade(pegarValor(json, "prioridade"));
                    tarefa.setStatusTarefa(pegarValor(json, "statusTarefa"));
                    String usuarioIdStr = pegarValor(json, "usuarioId");
                    if (!usuarioIdStr.isEmpty()) tarefa.setUsuarioId(Integer.parseInt(usuarioIdStr));

                    new TarefaDao().cadastrarTarefa(tarefa);
                    responder(exchange, 200, "{\"mensagem\":\"Tarefa cadastrada com sucesso!\"}");
                } else if ("GET".equals(exchange.getRequestMethod())) {
                    String query = exchange.getRequestURI().getQuery();
                    if (query != null && query.contains("usuarioId=")) {
                        int usuarioId = Integer.parseInt(query.split("usuarioId=")[1].split("&")[0]);
                        List<Tarefa> tarefas = new TarefaDao().listarPorUsuario(usuarioId);

                        StringBuilder sb = new StringBuilder("[");
                        for (int i = 0; i < tarefas.size(); i++) {
                            Tarefa t = tarefas.get(i);
                            sb.append(String.format(
                                    "{\"id\":%d, \"titulo\":\"%s\", \"dataTarefa\":\"%s\", \"statusTarefa\":\"%s\"}",
                                    t.getId(), t.getTitulo(), t.getDataTarefa(), t.getStatusTarefa()
                            ));
                            if (i < tarefas.size() - 1) sb.append(",");
                        }
                        sb.append("]");
                        responder(exchange, 200, sb.toString());
                    }
                } else if ("PUT".equals(exchange.getRequestMethod())) {
                    String json = lerBody(exchange);
                    int id = Integer.parseInt(pegarValor(json, "id"));
                    String novoStatus = pegarValor(json, "statusTarefa");
                    new TarefaDao().atualizarStatus(id, novoStatus);
                    responder(exchange, 200, "{\"mensagem\":\"Status atualizado!\"}");
                } else if ("DELETE".equals(exchange.getRequestMethod())) {
                    String query = exchange.getRequestURI().getQuery();
                    if (query != null && query.contains("id=")) {
                        int id = Integer.parseInt(query.split("id=")[1].split("&")[0]);
                        new TarefaDao().deletarTarefa(id);
                        responder(exchange, 200, "{\"mensagem\":\"Tarefa excluída!\"}");
                    }
                } else {
                    responder(exchange, 405, "{\"erro\":\"Método não permitido\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                responder(exchange, 500, "{\"erro\":\"Erro no servidor\"}");
            }
        });

        server.createContext("/api/eventos", exchange -> {
            try {
                if (isOptions(exchange)) return;

                if ("POST".equals(exchange.getRequestMethod())) {
                    String json = lerBody(exchange);
                    Evento evento = new Evento();
                    evento.setTitulo(pegarValor(json, "titulo"));
                    evento.setDescricao(pegarValor(json, "descricao"));
                    String dataEventoStr = pegarValor(json, "dataEvento");
                    if (!dataEventoStr.isEmpty()) evento.setDataEvento(LocalDateTime.parse(dataEventoStr));
                    evento.setUsuarioId(Integer.parseInt(pegarValor(json, "usuarioId")));

                    new EventoDao().cadastrarEvento(evento);
                    responder(exchange, 200, "{\"mensagem\":\"Evento cadastrado com sucesso!\"}");
                } else if ("GET".equals(exchange.getRequestMethod())) {
                    String query = exchange.getRequestURI().getQuery();
                    if (query != null && query.contains("usuarioId=")) {
                        int usuarioId = Integer.parseInt(query.split("usuarioId=")[1].split("&")[0]);
                        // Você deve implementar listarPorUsuario no EventoDao similar ao TarefaDao
                        responder(exchange, 200, "[]");
                    }
                } else {
                    responder(exchange, 405, "{\"erro\":\"Método não permitido\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                responder(exchange, 500, "{\"erro\":\"Erro no servidor\"}");
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
        String buscaChave = "\"" + campo + "\"";
        int indexChave = json.indexOf(buscaChave);
        if (indexChave == -1) return "";

        int indexDoisPontos = json.indexOf(":", indexChave + buscaChave.length());
        if (indexDoisPontos == -1) return "";

        int i = indexDoisPontos + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) {
            i++;
        }

        if (i < json.length() && json.charAt(i) == '\"') {
            int inicio = i + 1;
            int fim = json.indexOf("\"", inicio);
            return (fim != -1) ? json.substring(inicio, fim) : "";
        } else {
            int inicio = i;
            int fim = i;
            while (fim < json.length() && json.charAt(fim) != ',' && json.charAt(fim) != '}' && json.charAt(fim) != ']') {
                fim++;
            }
            return json.substring(inicio, fim).trim();
        }
    }

    private static void responder(HttpExchange exchange, int status, String resposta) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        byte[] bytes = resposta.getBytes();
        exchange.sendResponseHeaders(status, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
