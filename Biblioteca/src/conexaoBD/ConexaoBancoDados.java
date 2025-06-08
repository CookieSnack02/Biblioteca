package conexaoBD;

import java.sql.*;

public class ConexaoBancoDados{
	
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";
	private static final String DATA_URL = "jdbc:mysql://localhost:3306/Biblioteca";
	
	
	public static Connection createConnectionToMySQL() throws SQLException {
		 							//Classe carregada pelo JVM
		
		Connection conexao = DriverManager.getConnection(DATA_URL, USERNAME, PASSWORD); 
		
		return conexao;
	}
	
	
	public static void main(String[] args) throws SQLException, Exception{
		Connection con = createConnectionToMySQL();
		//Conexao com o banco de dados 
		
		if(con != null) {
			System.out.println("Conexao realizada com sucesso! " + con);
			con.close();
		} else {
			System.out.println("Erro ao conectar! ");
		}
		
		
	}


}
