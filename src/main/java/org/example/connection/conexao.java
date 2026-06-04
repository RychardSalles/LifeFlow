package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;


public class conexao {
    static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/lifeflow";
        String usuario = "root";
        String senha = "0Turob25@";

        try{
            Connection conexao = DriverManager.getConnection(url, usuario,senha);

            System.out.println("Conectado com Sucesso!");
        }catch (Exception e){
            System.out.println("Erro ao conectar");
            e.printStackTrace();
        }
    }
}
