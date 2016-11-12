package server;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import server.interfaces.*;

public class Menu extends JMenuBar implements ActionListener {	
	private Server serverRef;
	public Menu (Server serverRef) {
		this.serverRef = serverRef;		
		JMenu menuServer = new JMenu("Server");
		menuServer.setMnemonic(KeyEvent.VK_S);
		JMenuItem subMenuSalir = new JMenuItem("Salir",new ImageIcon(getClass().getResource("images/exit.png")));		
		subMenuSalir.addActionListener(this);
		menuServer.add(subMenuSalir);

		JMenu menuConfigurar = new JMenu("Configurar");
		menuConfigurar.setMnemonic(KeyEvent.VK_C);
		JMenuItem subMenuIps = new JMenuItem("Configurar DNS");
		subMenuIps.addActionListener(this);
		JMenuItem subMenuServerName = new JMenuItem("Nombre del servidor");		
		subMenuServerName.addActionListener(this);
		menuConfigurar.add(subMenuIps);
		menuConfigurar.add(subMenuServerName);

		JMenu menuUsuarios = new JMenu("Usuarios");
		menuUsuarios.setMnemonic(KeyEvent.VK_U);
		JMenuItem subMenuAgregarUsuario = new JMenuItem("Registrar un usuario");
		subMenuAgregarUsuario.addActionListener(this);
		JMenuItem subMenuVerUsuarios = new JMenuItem("Administrar usuarios");
		subMenuVerUsuarios.addActionListener(this);
		menuUsuarios.add(subMenuAgregarUsuario);
		menuUsuarios.add(subMenuVerUsuarios);		
		this.add(menuServer);
		this.add(menuUsuarios);
		this.add(menuConfigurar);
	}

	public void actionPerformed(ActionEvent e) {		
		if (e.getActionCommand().equals("Salir")) {
			if(JOptionPane.showConfirmDialog(this.serverRef,"Salir","¿Esta seguro de botar el servidor?",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)  {
				this.serverRef.close();
			}			
		} else if (e.getActionCommand().equals("Registrar un usuario")){
			this.serverRef.addInternalFrame(new AddUser(this.serverRef));
		}
	}
}