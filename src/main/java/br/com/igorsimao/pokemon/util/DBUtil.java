package br.com.igorsimao.pokemon.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBUtil {
		private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/pokemon";
	    private static final String usuario = "postgres";
	    private static final String senha = "123";
	    
	    static Connection connection = null;
	
	public static Connection DbConnect() {
		
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				
				connection = DriverManager.getConnection(jdbcUrl, usuario, senha);
				
				if (connection != null) {
					System.out.println("Conexão com o banco de dados estabelecida!");
					
				}else {
					System.out.println("Ocorreu um erro ao conectar com o banco de dados!");
				}
				
			
			} catch (SQLException e) {
				e.printStackTrace();
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			
			return connection;
		}
	
	
	public static void closeConnetion(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}