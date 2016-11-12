package client;
import client.Menu;
import client.functionality.ClientManager;
import client.basis.Contact;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionListener;
import java.awt.Color;

public class Client extends JFrame {
	private ClientManager clientManager;
	private JDesktopPane desktop;
	private JLabel status;
	private JLabel username;
	private Menu menuBar;

	public Client() {		
		this.setSize(710,600);
		this.setTitle("Cliente de correo");
		this.setLocationRelativeTo(getRootPane());		
		this.clientManager = new ClientManager(this,"",1400);							
		this.desktop = new JDesktopPane();
		this.menuBar = new Menu(this);
		this.getContentPane().add(this.menuBar, java.awt.BorderLayout.NORTH);
		this.getContentPane().add(this.getStatusBar(), java.awt.BorderLayout.SOUTH);
		this.getContentPane().add(desktop);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
		this.setVisible(true);
	}

	public static void main (String args[]) {		
		new Client();
	}

	public JToolBar getStatusBar() {
		JToolBar statusBar = new JToolBar();
		statusBar.setFloatable(false);
		this.status = new JLabel("Desconectado");
		this.username = new JLabel("");
		statusBar.add(new javax.swing.JLabel("Estado: "));
		statusBar.add(this.status);
		statusBar.addSeparator();
		statusBar.add(this.username);
		this.changeStatus(false);
		return statusBar;
	}

	public void addInternalFrame(JInternalFrame newFrame) {
		JInternalFrame[] frames = this.desktop.getAllFrames();
		for (int i = 0 ; i < frames.length ; i++) {
			if (frames[i] == newFrame) {
				newFrame.setVisible(true);
				return;
			}
		}
		this.desktop.add(newFrame);
		newFrame.setVisible(true);
	}

	public ClientManager getClientManager() {
		return this.clientManager;
	}

	public void setEmail(String mail) {
		this.username.setForeground(new Color(45,138,70));
		this.username.setText("" + mail);
	}
	
	public void changeStatus(boolean newStatus) {
		this.menuBar.changeStatus(newStatus);
		if (newStatus) {
			this.status.setText("Conectado");
			this.status.setForeground(new Color(65,158,90));
		} else {
			this.status.setText("Desconectado");
			this.status.setForeground(new Color(209,67,48));
			this.username.setText("");
			JInternalFrame[] frames = this.desktop.getAllFrames();
			for (int i = 0 ; i < frames.length ; i++) {
				frames[i].setVisible(false);
			}
		}
	}
}