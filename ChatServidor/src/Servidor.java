import java.rmi.*; 
import java.rmi.server.*; 
import java.rmi.registry.*;

public class Servidor {

	public static void  main(String [] args) {
		try {
			// Criando um objeto remoto.
			ServidorServicosCode services = new ServidorServicosCode();
			ServidorServicos service = (ServidorServicos) UnicastRemoteObject.exportObject(services, 0);

			// Registrando o objeto remoto no registro RMI com um identificador fornecido.
			Registry registry= LocateRegistry.getRegistry(); 
			registry.bind("ChatServices", service);
			System.out.println(" _____________________________________________________________________________ ");
			System.out.println("|                                                                             |");
			System.out.println("|                   SERVIDOR DO CHAT INICIADO COM SUCESSO!                    |");
			System.out.println("|_____________________________________________________________________________|");
	    
			// Caso houver um erro no servidor ele imprime a exceção no terminal.
		} catch (Exception e) {
			System.err.println("Erro no servidor:" + e) ;
			e.printStackTrace();
		}
	}
}