import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;
import javax.swing.JOptionPane;
import java.rmi.server.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Cliente implements Runnable {
	
	// Renomeia o nome das classes para organizar o código e inicia as variáveis.
	public static String nickname;
	public static ServidorServicos servidor;
	public static ClienteServicosCode cliente;
	public static ClienteServicos cli;
	public int status=0;
	private DateTimeFormatter formatter;
	
	/**
	// Cria um objeto "date".
	public Date date = new Date();
	// Converte a data em string:
	public String modifiedDateTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	**/
	// Método Main
	public static void main(String [] args) {
		
		try {
			//
			String host = "localhost";
			// Imprime o inicio do código
			System.out.println(" _____________________________________________________________________________ ");
			System.out.println("|                                                                             |");
			System.out.println("|                            BEM-VINDO AO CHAT                                |");
			System.out.println("|                                                                             |");
			System.out.println("|                    Comece digitando ENTRAR <nickname>                       |");
			System.out.println("|_____________________________________________________________________________|");

			// Obtendo referência do objeto remoto.
			Registry registry = LocateRegistry.getRegistry(host); 
			servidor = (ServidorServicos) registry.lookup("ChatServices");
			cliente = new ClienteServicosCode();
			cli= (ClienteServicos) UnicastRemoteObject.exportObject(cliente, 0);
			
			// Estou executando minha classe "Cliente" dentro de uma Thread.
			Cliente cliente = new Cliente();
			Thread t = new Thread(cliente);
			// Iniciando a Thread.
			t.start();
		
		// Caso houver uma exceção ele imprime no terminal.
		}catch (Exception e)  { 
			System.err.println("Erro no cliente: " + e);
		}

	}

	// Método run do Runnable. 
	public void run() {
		
		while(true){
			String opcao;
			String resposta="";
			
			opcao= JOptionPane.showInputDialog(null, "Digite: ");
			
			String split[] = opcao.split(" ");
			
			if(opcao=="" || opcao.equals("/sair")) {
				try {
					resposta = servidor.sair(cli);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				System.out.println(resposta);
				break;
			}
			
			
			String op_digitada=split[0];
		
			/**
			if(op_digitada==""|| op_digitada==null || split.length<1){
				System.out.println("Por favor, digite um comando ou mensagem válidos.");
				break;
			}**/


			try{
				// 
				switch (op_digitada) {
					// Se a entrada do usuário for "ENTRAR Nome" corretamente, ele entra no case:
					case "ENTRAR":
						//
						if(split.length!=2){
							System.out.println("Por favor, para entrar no chat você deve digitar dessa forma: ENTRAR <nickname>");
						break;
						}
						
						// Atribui a variável "nickname" o nickname informado pelo usuário.
						nickname = split[1];
						// System.out.println(nickname);
						
						// Chama a função entrar do servidor.
						resposta=servidor.entrar(cli);
						
						// E atribui o valor 1 a variável "status", ou seja, o usuário está em uma seção ativa do chat.
						status=1;
						
						// Seta a variável "nickname".
						cliente.set_cliente_nickname(nickname);
						
						// Atribui junto ao nome do usuário o id.
						// String.valueOf foi usado para que fosse atribuido o valor NULL caso não tivesse id. Se usássemos String.toString() o programa iria lançar uma exceção.
						nickname= nickname +"_" + String.valueOf(cliente.get_cliente_id());
						
						// Imprime a mensagem de cadastro realizado com sucesso.
						System.out.println("_______________________________________________________________________________\n");
						System.out.println("    Bem vindo(a), @"+ nickname);
						System.out.println("    " + resposta);
						System.out.println("_______________________________________________________________________________");

						// Imprime as conversas que estão armazenadas no Banco de Dados.
						List<ClienteServicosCode> listaPessoas = new ArrayList<>();
 
						listaPessoas = ServidorServicosCode.listarTodos();
 				
						for (int i = 0; i < listaPessoas.size(); i ++) {
							System.out.println(listaPessoas.get(i));
						}
						break;
					
					// Se a entrada do usuário for "SAIR" corretamente, ele entra no case:
					case "SAIR":
						// Se o "status" for 1, quer dizer que o usuário estava utilizando o chat. Portanto ele entra no "if" e finaliza a seção do usuário.
						if(status!=0){
							// Chama a função do servidor para sair do chat.
							resposta = servidor.sair(cli);
							System.out.println(resposta);
							// A variável "status" recebe o valor 0, quer dizer que o usuário não está em seção ativa no chat.
							status=0;
							// Caso o usuário não estiver em seção ativa, ele entra no "else" que imprime a seguinte mensagem:
						}else{
							System.out.println("Você não está logado.");
						}
						break;
					
					// Quando o programa resolve os "cases" ele entra no "default":
					default:
						// Se o "status" for 1, quer dizer que o usuário estava utilizando o chat e entra no "if":
						if(status!=0){
							
							formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT ).withLocale( Locale.UK ).withZone( ZoneId.systemDefault() );
							// 
							String mensagem = "@" + nickname + ": " + opcao;
							// Chama a função do servidor que envia a mensagem do usuário.
							servidor.enviar_mensagem(mensagem, cliente);
							
							// Armazena todas as mensagens no Banco de Dados:
							ClienteServicosCode pessoaDTO = new ClienteServicosCode();
							ServidorServicosCode pessoaDAO = new ServidorServicosCode();
							
							// Seta todas as informações do usuário: id, nome, mensagem.
							pessoaDTO.set_cliente_id(cliente.get_cliente_id());
							pessoaDTO.set_cliente_nickname(cliente.get_cliente_nickname());
							pessoaDTO.set_data_hora(formatter.format( Instant.now() ));
							pessoaDTO.set_mensagem(opcao);
							
							// Chama a função do servidor para inserir os dados no Banco.
							pessoaDAO.inserir(pessoaDTO);
 				
						}
						// Caso o usuário não estiver em seção ativa no chat (status=0) ele entra no "else" e imprime a seguinte mensagem:
						else{
							System.out.println("Por favor, ENTRE para conversar");
						}
						break;

				}
			// Caso houver uma exceção ele imprime no terminal.
			}catch (RemoteException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}