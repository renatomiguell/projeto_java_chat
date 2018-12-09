import java.rmi.*;
import java.rmi.server.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.rmi.registry.*;
import java.util.*;
import com.mysql.jdbc.Connection;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.text.DateFormat;

// Esta classe possui as funções do servidor, que são as sequintes: Enviar Mensagem, Entrar no Chat, Sair do Chat, Verificar se existe um Cliente, Inserir log de dados no Banco e Consultar todos os dados do Banco.
public class ServidorServicosCode implements ServidorServicos {
	
	// Criando a lista que exibe as mensagens durante uma conversa no chat.
	public List<ClienteServicos> client_list = new ArrayList<ClienteServicos>();
	// Inicia a variável que era os ids.
	public static int clientids=0;
	// Cria um objeto "date".
	public Date date = new Date();
	// Converte a data em string:
	public String modifiedDateTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	public String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);

	// Função que envia as mensagens.
	public void enviar_mensagem(String mensagem, ClienteServicos cliente) throws RemoteException {

			//Processa as menssagens.
			for (ClienteServicos cli : client_list ){
				
				// No terminal do cliente não repete a mensagem enviada por ele mesmo.
				if(cliente.get_cliente_id() == cli.get_cliente_id()){
				}
				// Se outros clientes estiverem conectados ao chat, recebem a mensagem.
				else{
					cli.receber_mensagem(mensagem);
				}
			}
	}
	
	// Função para se conectar ao chat.
	public String entrar(ClienteServicos cliente)throws RemoteException{
		
			// Se não existir um cliente.
			if(!clientExist(cliente)){
				
				// Ele adiciona um novo cliente na lista.
				client_list.add(cliente);
				// O Cliente recebe um "id" e recebe uma mensagem de registro feito com sucesso.
				cliente.set_cliente_id(++clientids);
				return "Você foi registrado(a) com sucesso! Converse à vontade.";
			}
			
			// Caso aconteça um erro ao entrar, a seguinte mensagem é impressa:
			return "Não foi possível entrar! Tente novamente mais tarde.";

	}

	// Função para se desconectar do chat.
	public String sair(ClienteServicos cliente)throws RemoteException{
		
				client_list.remove(cliente);
				return "Log Out feito com sucesso!";
	}

	
	// Função que verifica se existe um cliente conectado ao chat
	public boolean clientExist(ClienteServicos cliente){

			for(ClienteServicos cli : client_list){
				
				// Se sim, o usuário pode usar os serviços do chat.
				if(cliente == cli){
					return true;
				}
			}
			return false;
	}

	
	// Função para inserir os dados no Banco.
	public void inserir(ClienteServicosCode pessoaDTO)throws RemoteException{
		
		try {
			// Objeto que recebe a conexão com o Banco de Dados.
			Connection connection = (Connection) ConexaoUtil.getInstance().getConnection();
			
			// String que contém o código sql que vai ser executado no Banco.
			String sql = "INSERT INTO conversas(cliente_id, cliente_nickname, mensagem, data_hora) VALUES(?, ?, ?, ?)";
			
			// Conecta fazendo a ponte entre o Java e o Banco.
			PreparedStatement statement = connection.prepareStatement(sql);
			
			// Faz a inserção dos objetos. O primeiro parâmetro corresponde ao primeiro ponto de ?.
			statement.setInt(1, pessoaDTO.get_cliente_id());
			statement.setString(2, pessoaDTO.get_cliente_nickname());
			statement.setString(3, pessoaDTO.get_mensagem());
			statement.setString(4, modifiedDateTime);
			
			// Executa a inserção.
			statement.execute();
			// Fecha a conexão com o Banco.
			connection.close();
		
		// Caso aconteça um erro com a conexão, uma exceção é lançada.
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// Função que retorna uma lista com todos os dados armazenados no Banco.
	public static List<ClienteServicosCode> listarTodos()throws RemoteException{
		// Criando a lista.
		List<ClienteServicosCode> listaPessoas = new ArrayList<ClienteServicosCode>();
		
		try {
			// Objeto que recebe a conexão com o Banco de Dados.
			Connection connection = (Connection) ConexaoUtil.getInstance().getConnection();
			
			// String que contém o código sql que vai ser executado no Banco. Consultando os dados.
			String sql = "SELECT * FROM conversas";
			
			// Conecta fazendo a ponte entre o Java e o Banco.
			PreparedStatement statement = connection.prepareStatement(sql);
			
			// O executeQuery() retorna a consulta em uma tabela.
			ResultSet resultset = statement.executeQuery();
			
			// Loop perguntando se à linhas dentro da tabelas.
			while(resultset.next()) {
				// Criando objeto.
				ClienteServicosCode pessoaDTO = new ClienteServicosCode();
				// Obtendo os ids.
				pessoaDTO.set_cliente_id(resultset.getInt("cliente_id"));
				// Obtendo os nicknames.
				pessoaDTO.set_cliente_nickname(resultset.getString("cliente_nickname"));
				// Obtendo as mensagens.
				pessoaDTO.set_mensagem(resultset.getString("mensagem"));
				
				// Adiciona os dados na lista. 
				listaPessoas.add(pessoaDTO);
			}
			
			// Fecha a conexão com o Banco.
			connection.close();
		
		// Caso aconteça um erro com a conexão, uma exceção é lançada.
		}catch (Exception e) {
			e.printStackTrace();
		}
		return listaPessoas;
	}

}