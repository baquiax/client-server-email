package server.functionality;
import server.functionality.IServer;
import server.basis.ServerInfo;
import server.basis.Contact;
import server.basis.Error;
import server.Server;
import server.connection.DatabaseManager;
import java.util.Hashtable;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;

/*
 * Esta clase representa las acciones que un servidor de correos puede realizar.
 * Es la implementacion de la interfaz IServer.
 * @author Alexander Baquiax (S:B-12007988).
 * @vendor Universidad Galileo.
*/ 

public class ServerManager extends Thread implements IServer  {
	private Hashtable<Socket,Contact> clients;
	private String serverName;
	private DatabaseManager dbm;
	final private Server serverRef;
	private ServerSocket myService;	
	private ServerSocket myServiceToServer;	

	public ServerManager(Server serverRef,String serverName) {
		this.serverRef = serverRef;
		this.serverName = serverName;
		this.clients = new Hashtable<Socket,Contact>();
		this.dbm = new DatabaseManager();
		try {
			this.myService = new ServerSocket(1400);
			this.serverRef.printLog("server", "Escuchando en puerto 1400");
			this.myServiceToServer = new ServerSocket(1500);
			this.serverRef.printLog("server", "Escuchando en puerto 1500");
		} catch (IOException e) {				
			System.out.println(e);
		}		
	}

	public void run() {
		while(true) {
			try {
				final Socket skCliente = myService.accept();
				new Thread() {
					public void run() {
						try {
							InetAddress inet = skCliente.getInetAddress();
							serverRef.printLog ("server", "Cliente conectado - IP = " + inet);							

							InputStream in = skCliente.getInputStream();
							DataInputStream flowIn = new DataInputStream(in);
							OutputStream out = skCliente.getOutputStream();
							DataOutputStream flowOut = new DataOutputStream(out);

							ArrayList<String> commands = new ArrayList<String>();
							String firstString = flowIn.readUTF();
							commands.add(firstString);
							serverRef.printLog(inet.toString(), firstString);
							if (firstString.toUpperCase().indexOf("SEND MAIL") >=0 ) {
								while(true) {
									String nextCommand = flowIn.readUTF();									
									commands.add(nextCommand);									
									if (nextCommand.trim().toUpperCase().equals("END SEND MAIL")) break;
								}
							}
							executeCommand(skCliente, flowOut, commands);
							skCliente.close();
						} catch(Exception e) {
							System.out.println(e.getMessage());
						}
					}
				}.start();
			} catch (Exception e) {}			
		}
	}

	public void executeCommand(Socket skCliente, DataOutputStream out, ArrayList<String> commands) throws Exception {
		String[] firstCommandsSeparated = commands.get(0).split("[ ]");
		String firstCommand = firstCommandsSeparated[0];		
		if (firstCommand.toUpperCase().equals("LOGIN")) {
			if(firstCommandsSeparated.length > 1)
			login(skCliente, out, firstCommandsSeparated[1],firstCommandsSeparated[2]);						
		} else if(firstCommand.toUpperCase().equals("CLIST")) {
			if(firstCommandsSeparated.length > 1)
			getContactList(out, firstCommandsSeparated[1]);
		} else if(firstCommand.toUpperCase().equals("SEND")) {
			Object[] mailCommands = commands.toArray();
			ArrayList<String> destinatarios =  new ArrayList<String>();
			String subject = "";
			String body = "";
			for(int i = 0; i < mailCommands.length ;i++) {
				String mailCommand = (String) mailCommands[i];				
				if (mailCommand.toUpperCase().indexOf("MAIL TO") >= 0) {
					String[] mailToArray = mailCommand.trim().split("[ ]");
					destinatarios.add(mailToArray[2]);					
				} else if(mailCommand.toUpperCase().indexOf("MAIL SUBJECT") >= 0) {
					String[] subjetArray = mailCommand.split("[ ]");
					subject = subjetArray[2];
				} else if(mailCommand.toUpperCase().indexOf("MAIL BODY") >= 0) { 
					String[] bodyArray = mailCommand.split("[ ]");
					body = bodyArray[2];
				}				
			}
			for (String destinatario : destinatarios) {				
				Contact currentContact = new Contact("alex@a.es");
				receiveMail(out,new Contact(destinatario), currentContact,subject,body);
			}
			out.writeUTF("OK SEND MAIL");
		} else if(firstCommand.toUpperCase().equals("GETNEWMAILS")) { 
			getMyMails(out,firstCommandsSeparated[1]);
		} else {
			out.writeUTF("INVALID COMMAND");
		}
	}

	public void login(Socket sock, DataOutputStream out, String username, String password) throws Exception {
		if (username.length() < 1 && password.length() < 1) return ;
		username = username.trim();
		password = password.trim();
		if(existsUser(new Contact(username,""))) {
			String query = "SELECT username FROM user WHERE username = '" + username + "' AND " +
						"password = '" + password + "'";
			try {
				ResultSet resultado = this.dbm.executeQuery(query);
				if (resultado != null) {
					while(resultado.next()) {					
						Contact contact = new Contact(resultado.getString("username"),this.serverName);
						this.clients.put(sock,contact);
						out.writeUTF("OK LOGIN");						
						return;				
					}
				}
			} catch (SQLException e) { } 
			out.writeUTF((new Error(102,"LOGIN")).toString());
		} else {
			out.writeUTF((new Error(101,"LOGIN").toString()));
		}				
	}

	public boolean existsUser(Contact user) {
		if (user == null) return false;
		String query = "SELECT username FROM user WHERE username = '" + user.getUsername() + "' limit 1";
		try {
			ResultSet resultado = this.dbm.executeQuery(query);			
			return resultado.next();
		} catch (SQLException e) { 
			System.out.println("ERROR: " + e);
		} 
		return false;				
	}

	public void getContactList(DataOutputStream out, String user) throws Exception {
		ArrayList<Contact> result = new ArrayList<Contact>();		
		if (user == null) return ;
		String queryString = "SELECT email FROM contact WHERE username = '" + user + "'";		
		try {
			ResultSet query = this.dbm.executeQuery(queryString);
			while(query.next()) {
				String mail = query.getString("email").trim();
				int atSymbol = mail.indexOf("@");
				String username = mail.substring(0,atSymbol);
				String serverName = mail.substring(atSymbol + 1);
				result.add(new Contact(username,serverName));
			}			
		} catch (SQLException e) { 
			System.out.println("ERROR: " + e);
		} 	
		Object[] contacts = result.toArray();		
		if (contacts.length > 0) {
			for(int i = 0; i < contacts.length ; i++) {
				if (i == (contacts.length - 1)) {
					out.writeUTF("OK CLIST " + contacts[i] + " *");
					break;
				}
				out.writeUTF("OK CLIST " + contacts[i]);
			}		
		} else {			
			out.writeUTF("CLIST ERROR 103");
		}
	}

	public void receiveMail(DataOutputStream out, Contact to, Contact from, String subject, String body) throws Exception {
		String queryString  = "INSERT INTO mail (mail_to,mail_from,mail_subject,mail_body) " +
			"values('" + to.getUsername()+ "'," + 
			"'" + from.getUsername() + "'," +
			"'" + subject + "'," +
			"'" + body + "');"; 		
		this.dbm.executeUpdate(queryString);
		out.writeUTF("OK SEND MAIL");	
		serverRef.printLog("server", "OK SEND MAIL");
	}

	public boolean sendMail(Socket serverMail,Contact from, Contact[] sendTo, String subject, String body) {
		
		return false;
	}

	public boolean newContact(Contact user, Contact newContact) {
		String queryString = "INSERT INTO contact(username,email) " +
			"values('" + user.getUsername() + "','" + newContact + "')";
		if(this.dbm.executeUpdate(queryString) > 0) {
			return true;
		}
		return false;
	}

	public boolean noop () {
		return true;
	}

	public boolean register(String username, String password) {
		if (!existsUser(new Contact(username.trim(),this.serverName))) {
			String queryString = "INSERT INTO user(username,password) values ('" + 
				username.trim() + "','" + password.trim() + "')";
			this.dbm.executeUpdate(queryString);
		}
		return false;
	}

	public void getMyMails(DataOutputStream out, String username) throws Exception {		
		String query = "SELECT mail_from,mail_subject,mail_body,mail_date FROM mail WHERE mail_to = '" + username + "'";
		System.out.println(">" + query);
		try {
			ResultSet resultado = this.dbm.executeQuery(query);
			ArrayList<String> commands = new ArrayList<String>();
			while(resultado.next()) {
				commands.add("OK GETNEWMAILS " + resultado.getString("mail_from") + " " +
					resultado.getString("mail_subject").replaceAll(" ","+") + " " + resultado.getString("mail_body").replaceAll(" ","+"));
			}
			Object[] result = commands.toArray();
			for(int i =0; i < result.length; i++) {
				if (i == (result.length - 1)) {
					out.writeUTF(String.valueOf(result[i]) + " *");
				} else {
					out.writeUTF(String.valueOf(result[i]));
				}
			}
		} catch (SQLException e) { 
			System.out.println("ERROR: " + e);
		} 		
	}
}