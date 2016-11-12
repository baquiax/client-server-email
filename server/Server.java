package server;
import server.Menu;
import javax.swing.JScrollPane;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.net.Socket;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import server.functionality.ServerManager;

public class Server extends JFrame {	
	private JDesktopPane desktop;
	private JInternalFrame logFrame;
	private JTextArea logViewer;
	private ServerManager server;
	public Server() {		
		this.setLayout(null);
		this.setTitle("Server Mail");
		this.setJMenuBar(new Menu(this));
		this.setSize(700,500);				
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(getRootPane());
		this.setResizable(true);
		
		this.desktop = new JDesktopPane();

		this.logViewer = new JTextArea();		
		this.logViewer.setBounds(8,255,680,190);
		this.logViewer.setEditable(false);

		this.logFrame = new JInternalFrame("Server Log",true,false,true,true);
		this.logFrame.add(logViewer);
		this.logFrame.setSize(400,200);
		this.logFrame.setVisible(true);

		this.desktop.add(logFrame);
		this.desktop.setAutoscrolls(true);
		this.desktop.setPreferredSize(new Dimension(200, 400));

		JScrollPane sp = new JScrollPane(this.desktop);				
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(sp,BorderLayout.CENTER);
		this.setVisible(true);			

		this.server = new ServerManager(this,"bqx.com");	
		this.server.start();
	}

	public static void main(String args[]) {
		new Server();
	}

	public void printLog (String sender, String logText) {
		this.logViewer.insert(sender.toUpperCase() + ": " + logText + "\n",0);
		System.out.println(sender.toUpperCase() + ": " + logText);
	}

	public void close() {
		this.setVisible(false);
		this.dispose();
	}

	public void addInternalFrame (JInternalFrame newFrame) {
		this.desktop.add(newFrame);
		newFrame.setVisible(true);		
	}

	public ServerManager getServerManager() {
		return this.server;
	}
}