import java.rmi.*; 

public interface ClienteServicos extends Remote{
	
	public void receber_mensagem(String mensagem) throws RemoteException;
	public void set_cliente_id(int id) throws RemoteException;
	public int get_cliente_id() throws RemoteException;
	public void set_cliente_nickname(String nickname) throws RemoteException;
	public String get_cliente_nickname() throws RemoteException;
	public void set_mensagem(String mensagem) throws RemoteException;
	public String get_mensagem() throws RemoteException;
	
}