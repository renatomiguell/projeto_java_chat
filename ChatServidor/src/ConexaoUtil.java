import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoUtil {
	// Criando o objeto de conex�o.
	private static ConexaoUtil conexaoUtil;
	
	// Criando o m�todo que vai retornar uma conex�o
	public static ConexaoUtil getInstance() {
		
		// Se n�o tiver um objeto instanciado, ele vai instaciar.
		if(conexaoUtil == null) {
			conexaoUtil = new ConexaoUtil();
		}
		return conexaoUtil;
	}
	
	// M�todo que ira fazer a conex�o com o Banco de Dados.
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		// Driver da conex�o com MYSQL.
		Class.forName("com.mysql.jdbc.Driver");
		
		// Retorna a conex�o.
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/banco_conversas", "root", "");
	}
	
	/** Testa a conex�o.
	public static void main(String[] args) {
		try {
			System.out.println(getInstance().getConnection());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}**/
}