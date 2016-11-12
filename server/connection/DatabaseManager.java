package server.connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
 * Esta clase esta destianda a interactuar con la base de datos. 
 * @author Alexander Baquiax.
 * @vendor Universidad Galileo.
*/ 

public class DatabaseManager {
	private Connection connection;
	private Statement query;

	public static void main(String args[]) throws Exception {
		DatabaseManager db =  new DatabaseManager();							
	}

	/*
	 * Constructor.	 
	*/ 
	public DatabaseManager() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:server-live.db");
			this.query = connection.createStatement();
			checkModel();
		} catch(ClassNotFoundException e)  {
			printError(e);
		} catch(SQLException e) {
			printError(e);
		}
	}

	/*
	 * Verifica si las tablas de la base de datos existen.
	 * Sino las crea.
	*/ 
	public void checkModel() {
		ResultSet rs;
		String[] tableNames = {"user", "contact", "mail"};

		String[] tableDefinitions = new 	String[tableNames.length];
		tableDefinitions[0] = "CREATE TABLE IF NOT EXISTS user(" +					
					"username string,"+
					"password string)";		
		tableDefinitions[1] = "CREATE TABLE IF NOT EXISTS contact(" +
					"username string, " +
					"email string, " +
					"PRIMARY KEY(username,email), " +
					"FOREIGN KEY(username) REFERENCES user(username));";
		tableDefinitions[2] = "CREATE TABLE IF NOT EXISTS mail(" +
					"mail_to string, " +
					"mail_from string, " +
					"mail_subject string, " +
					"mail_body string, " +
					"mail_date timestamp default current_timestamp, " +
					"status string default 'N')";					

		for (int i = 0; i < tableNames.length ;i++) {
			try {
				rs = executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" +
					tableNames[i] + "';");					
				if(rs.getFetchSize() == 0) {
					if(tableDefinitions[i].length() > 0) 
						executeUpdate(tableDefinitions[i]);
				}
			} catch(SQLException e) {
				printError(e);
			}
		}				
	}

	/*
	 * Metodo destiando para realizar consultas a la base de datos.
	 * @param queryString El querya ejecutar.
	 * @return ResultSet con los datos obtenidos por la consulta.
	*/ 
	public ResultSet executeQuery (String queryString) {		
		try {
			return this.query.executeQuery(queryString);
		} catch (SQLException e) {
			printError(e);
			return null;
		}
	}

	/*
	 * Este metodo esta destianado para realizar consultas tipo DML como inserts, updates o deletes.
	 * @param queryString Query a ejecutar.
	 * @return Cabtidad de filas o registros modificadas.
	*/ 
	public int executeUpdate (String queryString) {		
		try {
			return this.query.executeUpdate(queryString);
		} catch (SQLException e) {
			printError(e);
			return -1;
		}
	}

	/*
	 * Cerrar la coneccion con la base de datos.
	 * @return El resultado de la accion.
	*/ 
	public boolean closeConnection() {
		try {
			this.connection.close();
			return true;
		} catch (SQLException e) {
			printError(e.getMessage());
			return false;
		}
	}

	/*	
	 * Imprime los erroes de la base de datos. Esto es por si en algun momento se desea 
	 * imprimir un mensaje personalizado.
	*/ 
	public void printError(Object message) {
		System.out.println("ERROR: " + message);
	}
}