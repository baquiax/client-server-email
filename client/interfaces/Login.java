package client.interfaces;
import client.Client;
import client.functionality.ClientManager;
import client.basis.Error;
import client.basis.Contact;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class Login extends JDialog  implements ActionListener {
	private Client clientRef;
	private JTextField username;
	private JPasswordField password;
	private JLabel labelForError;

	public Login (Client client) {		
		this.clientRef = client;

		labelForError = new JLabel("");
		labelForError.setForeground(Color.red);
		labelForError.setBounds(20,85,260,20);
		this.add(labelForError);

		JLabel labelForUsername = new JLabel("E-mail:");
		labelForUsername.setBounds(20,10,70,25);
		this.add(labelForUsername);
		this.username = new JTextField();
		this.username.setBounds(95,10,160,25);
		this.add(this.username);

		JLabel labelForPassword = new JLabel("Password:");
		labelForPassword.setBounds(20,45,70,25);
		this.add(labelForPassword);
		this.password = new JPasswordField();
		this.password.setBounds(95,45,160,25);				
		this.add(this.password);

		JButton aceptar = new JButton("Acceder");
		aceptar.addActionListener(this);
		aceptar.setBounds(40,110,90,30);
		this.add(aceptar);

		JButton cancelar = new JButton("Cancelar");		
		cancelar.addActionListener(this);
		cancelar.setBounds(140,110,90,30);

		this.add(cancelar);
		this.setTitle("Acceso al sistema.");
		this.setModal(true);
		this.setResizable(false);
		this.setSize(290,180);
		this.setLayout(null);
		this.setLocationRelativeTo(getRootPane());		
		this.setVisible(true);		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Acceder")) {
			if(validateForm()) {				
				Object loginResult = this.clientRef.getClientManager().login(this.username.getText().trim(),this.password.getText().trim());
				if (loginResult instanceof Contact) {
					this.setVisible(false);
					this.dispose();
				} else if (loginResult instanceof Error) {
					this.labelForError.setText(loginResult.toString());					
				}					
			}
		} else if(e.getActionCommand().equals("Cancelar")) {
			this.setVisible(false);
			this.dispose();
		}
	}	

	/*
	 * Valida los el formualio antes de crear la conexion con el server.	 
	*/ 
	public boolean validateForm() {
		String username = this.username.getText().trim();
		String password = this.password.getText().trim();
		this.labelForError.setText("");
		if(username.length() < 1 || password.length() < 1) {
			this.labelForError.setText("Algun campo esta vacio!");
			return false;
		} else {
			int atIndex = username.indexOf("@");
			if (atIndex > 0) {

			} else {
				this.labelForError.setText("El correo no tiene un formato valido");
				return false;
			}
		}
		return true	;
	}
	
}