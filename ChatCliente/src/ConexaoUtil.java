import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoUtil {
	// Criando o objeto de conexão.
	private static ConexaoUtil conexaoUtil;
	
	// Criando o método que vai retornar uma conexão
	public static ConexaoUtil getInstance() {
		
		// Se não tiver um objeto instanciado, ele vai instaciar.
		if(conexaoUtil == null) {
			conexaoUtil = new ConexaoUtil();
		}
		return conexaoUtil;
	}
	
	// Método que ira fazer a conexão com o Banco de Dados.
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		// Driver da conexão com MYSQL.
		Class.forName("com.mysql.jdbc.Driver");
		
		// Retorna a conexão.
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/banco_conversas", "root", "");
	}
	
	/** Testa a conexão.
	public static void main(String[] args) {
		try {
			System.out.println(getInstance().getConnection());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}**/
}