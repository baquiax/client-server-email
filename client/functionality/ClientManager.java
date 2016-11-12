package client.functionality;
import client.Client;
import client.basis.Contact;
import client.basis.Error;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Hashtable;
import java.util.ArrayList;

public class ClientManager {
	private String serverName;	
	private String serverHost;
	private String user;
	private int serverPort; 
	private Client client;
	private String ipDNS;
	private int portDNS;
	private Hashtable<String, String>  servers;	
	private Contact currentUser;

	public ClientManager(Client clientRef, String serverHost, int serverPort) {
		this.client = clientRef;
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.ipDNS = "";
		this.portDNS = 0;
		this.user = "";	
		this.servers = new Hashtable<String, String>();	
		this.currentUser = new Contact("");
	}

	public String getDNSIp() {
		return this.ipDNS;
	}

	public void setDNSIp(String ip) {
		this.ipDNS = ip;
	}

	public int getDNSPort() {
		return this.portDNS;
	}

	public void setDNSPort(int port) {
		this.portDNS = port;
	}

	public void poblateServers() {
		this.servers = new Hashtable<String, String>();
		try {
			Socket miSocket = new Socket(this.ipDNS,this.portDNS);
			OutputStream out = miSocket.getOutputStream();
			DataOutputStream flowOut = new DataOutputStream(out);
			InputStream in = miSocket.getInputStream();
			DataInputStream flowIn = new DataInputStream(in);
			flowOut.writeUTF("GETIPTABLE");
			while(true) {
				//String ip = flowIn.readUTF();
				String ip = "OK IP TABLE baquiax.com 10.0.2.194 *";
				String[] newIp = ip.split("[ ]");
				if (!newIp[1].equals("ERROR")) {
					this.servers.put(newIp[3], newIp[4]);
				}				
				if (ip.indexOf("*") > 0) break;
			}			
		} catch (Exception e) { 
			System.out.println("Poblate: " + e.getMessage());
		}
	}

	public String getIPServer(String fromMail) {
		String[] mailParts = fromMail.split("@");
		return (this.servers.get(mailParts[1]) != null) ?  this.servers.get(mailParts[1]) : "-1.0.0.0";
	} 

	public Object login(String email, String password) {
		try {
			Socket sockClient = new Socket(getIPServer(email),this.serverPort);			
			Contact mail = new Contact(email);
			OutputStream out = sockClient.getOutputStream();
			DataOutputStream flowOut = new DataOutputStream(out);
			flowOut.writeUTF("LOGIN " + mail.getUsername() + " " + password);
			InputStream in = sockClient.getInputStream();
			DataInputStream flowIn = new DataInputStream(in);
			String result = flowIn.readUTF();
			String splitResult[] = result.split("[ ]");
			if (splitResult.length < 3) {
				this.client.setEmail(mail.getUsername());
				this.client.changeStatus(true);
				this.serverHost = getIPServer(email);
				this.user = mail.getUsername();
				getContactList();
				this.currentUser = new Contact(email);
				return mail;
			} else if(splitResult[2].equals("101")){
				this.client.changeStatus(false);
				return new Error(101,"Usuario inexistente.");				
			} else if(splitResult[2].equals("102")) {
				this.client.changeStatus(false);
				return new Error(102,"Password invalido");
			}			
			sockClient.close();
		} catch (IOException e) { 
			System.out.println(e.getMessage());
			return new Error(404,"Imposible conectarse con el servidor");
		}
		return null;
	}

	public void getContactList() {		
		try {
			Socket sockClient = new Socket(this.serverHost,this.serverPort);
			OutputStream out = sockClient.getOutputStream();
			DataOutputStream flowOut = new DataOutputStream(out);
			flowOut.writeUTF("CLIST " + this.user );
			InputStream in = sockClient.getInputStream();
			DataInputStream flowIn = new DataInputStream(in);
			while(true) {				
				String clist = flowIn.readUTF();				
				if(clist.indexOf("*") > 0) break;
				if(clist.indexOf("ERROR") > 0) break;
			}
		} catch (IOException e) { 
			System.out.println(e.getMessage());			
		}
	}

	public Object sendMail(String[] mails, String subject, String body) {
		String result = null;
		try {
			Socket sockClient = new Socket(this.serverHost,this.serverPort);
			OutputStream out = sockClient.getOutputStream();
			DataOutputStream flowOut = new DataOutputStream(out);
			flowOut.writeUTF("SEND MAIL " + this.currentUser);
			for (int i = 0; i < mails.length ;i++) {
				System.out.println(mails[i]);
				if(i == (mails.length - 1)) 
					flowOut.writeUTF("MAIL TO " + mails[i] + " *");
				else
					flowOut.writeUTF("MAIL TO " + mails[i] );
			}
			flowOut.writeUTF("MAIL SUBJECT " + subject.replaceAll(" ","+"));
			flowOut.writeUTF("MAIL BODY " + body.replaceAll(" ","+"));
			flowOut.writeUTF("END SEND MAIL");
			InputStream in = sockClient.getInputStream();
			DataInputStream flowIn = new DataInputStream(in);
			result = flowIn.readUTF();			
			sockClient.close();
		} catch (Exception e) { 
			System.out.println(e);
		}
		return result;
	}

	public ArrayList<String> getMyMails() {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Socket sockClient = new Socket(this.serverHost,this.serverPort);
			OutputStream out = sockClient.getOutputStream();
			DataOutputStream flowOut = new DataOutputStream(out);
			flowOut.writeUTF("GETNEWMAILS " + this.currentUser.getUsername());			
			InputStream in = sockClient.getInputStream();
			DataInputStream flowIn = new DataInputStream(in);
			while(true) {
				String r = flowIn.readUTF();
				result.add(r);
				if(r.indexOf("*") >= 0)	break;
			}			
			sockClient.close();
		} catch (Exception e) { 
			System.out.println(e);
		}
		return result;
	}
} 