import java.rmi.*;
import java.util.List; 

public interface ServidorServicos extends Remote{
	
	public String entrar(ClienteServicos cliente)throws RemoteException;
	public void enviar_mensagem(String mensagem, ClienteServicos cliente)throws RemoteException;
	public String sair(ClienteServicos cliente)throws RemoteException;
	public void inserir(ClienteServicosCode pessoaDTO)throws RemoteException;
	public static List<ClienteServicosCode> listarTodos()throws RemoteException {
		return null ;
	}
	
}