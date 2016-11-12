package server.interfaces;
import javax.swing.JInternalFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import server.Server;
import server.basis.Contact;
import javax.swing.JOptionPane;

public class AddUser extends JInternalFrame implements ActionListener {
	private JTextField username;
	private JPasswordField password;
	private Server serverRef;
	public AddUser(Server serverRef) {
		super("Agregar usuario",false,true,false,true);		
		this.serverRef = serverRef;

		JLabel labelForUsername = new JLabel("Usuario: ");
		labelForUsername.setBounds(5,20,70,20);		
		this.username = new JTextField();
		this.username.setBounds(75,20,150,20);		
		this.add(labelForUsername);
		this.add(username);
		
		JLabel labelForPassword = new JLabel("Password: ");
		labelForPassword.setBounds(5,45,70,20);
		this.add(labelForPassword);		
		this.password = new JPasswordField();
		this.password.setBounds(75,45,150,20);		
		this.add(labelForPassword);
		this.add(password);

		JButton register = new JButton("Registrar");
		register.setBounds(10,80,100,20);
		register.addActionListener(this);
		this.add(register);

		JButton cancel = new JButton("Cancelar");
		cancel.setBounds(130,80,100,20);
		cancel.addActionListener(this);
		this.add(cancel);

		this.setLayout(null);		
		this.setSize(250,160);				
		this.requestFocusInWindow();				
	}

	public boolean validateData() {
		String username = this.username.getText();
		String password = this.password.getText();
		if (username.length() == 0 || password.length() == 0) {
			JOptionPane.showMessageDialog(null,"Datos ingresados incorrectamente.", "Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (this.serverRef.getServerManager().existsUser(new Contact(username,""))) {
			JOptionPane.showMessageDialog(null,"Este usuario ya existe.","Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	public void actionPerformed(ActionEvent e) {		
		if (e.getActionCommand().equals("Cancelar")) {
			this.setVisible(false);
			this.dispose();
		} else if (e.getActionCommand().equals("Registrar")) {
			if (validateData()) {
				this.serverRef.getServerManager().register(this.username.getText(),this.password.getText());
				JOptionPane.showMessageDialog(null,"Usuario registrado correctamente!");
				this.serverRef.printLog("server","Usuario: " + this.username.getText() + " registrado.");
				this.setVisible(false);
				this.dispose();
			} 
		}
	}	
}