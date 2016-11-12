package server.basis;

/*
 * Esta clase representa la informacion de un servidor.
 * La informacion de como acceder a el.
 * @auhtor Alexander Baquiax.
 * @vendor Universidad Galileo.
*/ 
public class ServerInfo {

	private String ip;
	private String serverName;

	/*
	 * Constructor.
	 * @param ip Direccion ip obtenida por el DNS.
	 * @param serverName Nombre del servidor.
	*/ 
	public ServerInfo(String ip, String serverName) {
		this.ip = ip;
		this.serverName = serverName;
	}

	/*
	 * Obtener la ip de objecto actual
	 * @return Direccion IP.
	*/ 
	public String getIp(){
		return this.ip;
	}

 	/*
 	 * Obtener el nombre del servidor atual.
 	 * @param Nombre del servidor.
 	*/ 
	public String getServerName() {
		return this.serverName;
	}

}