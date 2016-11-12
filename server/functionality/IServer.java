package server.functionality;
import java.util.ArrayList;
import server.basis.Contact;
import server.basis.ServerInfo;
import java.net.Socket;
import java.io.DataOutputStream;

/*
 * Interfaz que debe implementar el servidor de correos.
 * @author Alexander Baquiax 
 * @vendor Universidad Galileo.
*/ 

public interface IServer {
	/*
	 * Al implementar este metodo, nuestra aplicacion deberia ser capaz
	 * de validar la autenticacion de un usuario.
	 * @param username Nombre de la cuenta.
	 * @param password Pass del usuario.
	 * @return Retorna el contacto si la autenticacion es correcta.
	*/ 
	public void login(Socket sock, DataOutputStream out, String username, String password) throws Exception;

	/*
	 * Hay ocaciones en las que necesitamos saber si un usuario existe,
	 * esta es la tarea de este metodo.
	 * @param user Cuneta a buscar.
	 * @return Estado de la busqueda.
	*/ 
	public boolean existsUser(Contact user);
	
	/*
	 * Retorna una lista con los contactos de usuario proporcionado.	 
	 * @param user El usuario del que se obtendran los contactos.
	 * @return Una lista de contactos si existen.
	*/
	public void getContactList(DataOutputStream out, String user) throws Exception;

	/*
	 * Envia un correo a la lista de emails indicados. Este metodo debe ser lo suficientemente 
	 * capaz de enviarselos a otro servidores.
	 * @param from Usuario que envia el correo.
	 * @param sendTo Lista de destinatarios para el correo.
	 * @param subject Asunto del correo.
	 * @param body El mensaje a enviar.
	 * @return Indica si el correo fue enviado correctamente.
	*/ 
	public boolean sendMail(Socket socket,Contact from, Contact[] sendTo, String subject, String body);

	/*
	 * Guarda un correo en el servidor.	 
	 * @param from Usuario que envia el correo.
	 * @param sendTo Lista de destinatarios para el correo.
	 * @param subject Asunto del correo.
	 * @param body El mensaje a enviar.	 
	*/ 
	public void receiveMail(DataOutputStream out, Contact to, Contact from, String subject, String body) throws Exception;

	/*
	 * Intenta registrar un nuevo contacto al usuario indicado.
	 * @param user Usuario a modificar.
	 * @param newContact El nuevo contacto.
	 * @return Indica si el proceso fue exitoso.
	*/
	public boolean newContact(Contact user, Contact newContact);

	/*
	 * Unicamente retorna true. Esto le servira al cliente saber que el servidor sigue vivo.
	 * @return Siempre retorna true.
 	*/
	public boolean noop () ;

	/*
	 * Registra un nuevo usuario en el sistema. Retorna un booleano como resultado.
	 * @param username El nuevo usuario
	 * @param password Pass para este usuario
	*/
	public boolean register(String username, String password);
}