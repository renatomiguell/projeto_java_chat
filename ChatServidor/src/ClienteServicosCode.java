import java.rmi.RemoteException;

public class ClienteServicosCode implements ClienteServicos {
	
	// Inicias as variáveis que recebem os ids, nicknames, e mensagens do cliente.
	private int cliente_id;
	private String cliente_nickname;
	private String mensagem;
	private String data_hora;
	
	// Criando os métodos Getters e Setters.
	public String get_data_hora() {
		return data_hora;
	}

	public void set_data_hora(String data_hora) {
		this.data_hora = data_hora;
	}
	
	public void set_mensagem(String mensagem) throws RemoteException{
		this.mensagem = mensagem;
	}

	public String get_mensagem() throws RemoteException{
		return mensagem;
	}

	public void set_cliente_id(int id) throws RemoteException{
		this.cliente_id = id;
	}
	public int get_cliente_id() throws RemoteException{
		return cliente_id;
	}

	public void set_cliente_nickname(String nickname) throws RemoteException{
		this.cliente_nickname = nickname;
	}
	public String get_cliente_nickname() throws RemoteException{
		return cliente_nickname;
	}
	
	// Função para receber as mensagens.
	public void receber_mensagem(String mensagem) throws RemoteException {
		System.out.println(mensagem);
	}
	
	// Criando o método "toString".
	public String toString() {
		try {
			return "(" + get_data_hora() + ") - " + "@" + get_cliente_nickname() + "_" + get_cliente_id() + ": " + get_mensagem();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cliente_nickname;
	}
	
}
