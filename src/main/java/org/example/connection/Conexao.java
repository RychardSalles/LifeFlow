package org.example.connection;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexao {

        private static final String url = "jdbc:mysql://localhost:3306/lifeflow";
        private static final String usuario = "root";
        private static final String senha = "";

        public static Connection conectar(){

            try {
                return DriverManager.getConnection(url, usuario, senha);

            } catch (SQLException e) {
                throw new RuntimeException("Erro ao conectar com o banco", e);
            }
        }
}
