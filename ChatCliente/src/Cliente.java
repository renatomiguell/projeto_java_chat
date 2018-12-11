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
	
	// Renomeia o nome das classes para organizar o c�digo e inicia as vari�veis.
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
	// M�todo Main
	public static void main(String [] args) {
		
		try {
			//
			String host = "localhost";
			// Imprime o inicio do c�digo
			System.out.println(" _____________________________________________________________________________ ");
			System.out.println("|                                                                             |");
			System.out.println("|                            BEM-VINDO AO CHAT                                |");
			System.out.println("|                                                                             |");
			System.out.println("|                    Comece digitando ENTRAR <nickname>                       |");
			System.out.println("|_____________________________________________________________________________|");

			// Obtendo refer�ncia do objeto remoto.
			Registry registry = LocateRegistry.getRegistry(host); 
			servidor = (ServidorServicos) registry.lookup("ChatServices");
			cliente = new ClienteServicosCode();
			cli= (ClienteServicos) UnicastRemoteObject.exportObject(cliente, 0);
			
			// Estou executando minha classe "Cliente" dentro de uma Thread.
			Cliente cliente = new Cliente();
			Thread t = new Thread(cliente);
			// Iniciando a Thread.
			t.start();
		
		// Caso houver uma exce��o ele imprime no terminal.
		}catch (Exception e)  { 
			System.err.println("Erro no cliente: " + e);
		}

	}

	// M�todo run do Runnable. 
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
				System.out.println("Por favor, digite um comando ou mensagem v�lidos.");
				break;
			}**/


			try{
				// 
				switch (op_digitada) {
					// Se a entrada do usu�rio for "ENTRAR Nome" corretamente, ele entra no case:
					case "ENTRAR":
						//
						if(split.length!=2){
							System.out.println("Por favor, para entrar no chat voc� deve digitar dessa forma: ENTRAR <nickname>");
						break;
						}
						
						// Atribui a vari�vel "nickname" o nickname informado pelo usu�rio.
						nickname = split[1];
						// System.out.println(nickname);
						
						// Chama a fun��o entrar do servidor.
						resposta=servidor.entrar(cli);
						
						// E atribui o valor 1 a vari�vel "status", ou seja, o usu�rio est� em uma se��o ativa do chat.
						status=1;
						
						// Seta a vari�vel "nickname".
						cliente.set_cliente_nickname(nickname);
						
						// Atribui junto ao nome do usu�rio o id.
						// String.valueOf foi usado para que fosse atribuido o valor NULL caso n�o tivesse id. Se us�ssemos String.toString() o programa iria lan�ar uma exce��o.
						nickname= nickname +"_" + String.valueOf(cliente.get_cliente_id());
						
						// Imprime a mensagem de cadastro realizado com sucesso.
						System.out.println("_______________________________________________________________________________\n");
						System.out.println("    Bem vindo(a), @"+ nickname);
						System.out.println("    " + resposta);
						System.out.println("_______________________________________________________________________________");

						// Imprime as conversas que est�o armazenadas no Banco de Dados.
						List<ClienteServicosCode> listaPessoas = new ArrayList<>();
 
						listaPessoas = ServidorServicosCode.listarTodos();
 				
						for (int i = 0; i < listaPessoas.size(); i ++) {
							System.out.println(listaPessoas.get(i));
						}
						break;
					
					// Se a entrada do usu�rio for "SAIR" corretamente, ele entra no case:
					case "SAIR":
						// Se o "status" for 1, quer dizer que o usu�rio estava utilizando o chat. Portanto ele entra no "if" e finaliza a se��o do usu�rio.
						if(status!=0){
							// Chama a fun��o do servidor para sair do chat.
							resposta = servidor.sair(cli);
							System.out.println(resposta);
							// A vari�vel "status" recebe o valor 0, quer dizer que o usu�rio n�o est� em se��o ativa no chat.
							status=0;
							// Caso o usu�rio n�o estiver em se��o ativa, ele entra no "else" que imprime a seguinte mensagem:
						}else{
							System.out.println("Voc� n�o est� logado.");
						}
						break;
					
					// Quando o programa resolve os "cases" ele entra no "default":
					default:
						// Se o "status" for 1, quer dizer que o usu�rio estava utilizando o chat e entra no "if":
						if(status!=0){
							
							formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT ).withLocale( Locale.UK ).withZone( ZoneId.systemDefault() );
							// 
							String mensagem = "@" + nickname + ": " + opcao;
							// Chama a fun��o do servidor que envia a mensagem do usu�rio.
							servidor.enviar_mensagem(mensagem, cliente);
							
							// Armazena todas as mensagens no Banco de Dados:
							ClienteServicosCode pessoaDTO = new ClienteServicosCode();
							ServidorServicosCode pessoaDAO = new ServidorServicosCode();
							
							// Seta todas as informa��es do usu�rio: id, nome, mensagem.
							pessoaDTO.set_cliente_id(cliente.get_cliente_id());
							pessoaDTO.set_cliente_nickname(cliente.get_cliente_nickname());
							pessoaDTO.set_data_hora(formatter.format( Instant.now() ));
							pessoaDTO.set_mensagem(opcao);
							
							// Chama a fun��o do servidor para inserir os dados no Banco.
							pessoaDAO.inserir(pessoaDTO);
 				
						}
						// Caso o usu�rio n�o estiver em se��o ativa no chat (status=0) ele entra no "else" e imprime a seguinte mensagem:
						else{
							System.out.println("Por favor, ENTRE para conversar");
						}
						break;

				}
			// Caso houver uma exce��o ele imprime no terminal.
			}catch (RemoteException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}