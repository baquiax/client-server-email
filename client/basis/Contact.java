package client.basis;
/*
 * Esta clase representa la estrucutra de un contacto.
 * @author Alexander Baquiax 
 * @vendor Universidad Galileo. Seccion B. 12007988
*/ 

public class Contact {
	
	private String username;
	private String serverName;

	/*
	 * Constructor
	 * @param username Nombre de la cuenta
	 * @param serverName Nombre del servidor.
	*/
	public Contact (String username, String serverName) {		
		this.username = username;
		this.serverName = serverName;
	}

	/*
	 * Constructor en base a un mail.
	*/
	 public Contact (String mail) {	 	
	 	int atSymbol = mail.indexOf("@");	 	
	 	if (atSymbol > 0) {
	 		String[] data = mail.split("@");
	 		this.username = data[0];
	 		this.serverName = data[1];
	 	} else {
	 		this.username = mail;
	 		this.serverName = "";
	 	}		
	 }

	/*
	 * Retorna la parte del email que representa el nombre de la cuenta.
 	 * @return El nombre de cuenta.
 	*/ 
	public String getUsername () {
		return this.username;
	}

	/*
	 * Retorna la parte del email que representa el nombre del servidor.
	 * @return Nombre del servidor.
	*/ 
	public String getServerName () {
		return this.serverName;
	}

	/*
	 * Representacion en cadena del objeto.
	 * @return El contacto en formato username@serverName
	*/ 
	public String toString () {
		return this.username + "@" + this.serverName;
	}
}