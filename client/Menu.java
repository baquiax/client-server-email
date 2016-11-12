package client;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import client.Client;
import client.interfaces.*;

public class Menu extends JToolBar implements ActionListener {
	private Client clientRef;	
	private JButton login;
	private JButton newMail;
	private JButton mails;
	private JButton contacts;
	private JButton addContact;
	private JButton configure;
	private Configure configureUI;	

	public Menu(Client client) {
		this.clientRef = client;
		this.login = new JButton("Acceder",new ImageIcon(getClass().getResource("images/sign_in.png")));
		this.login.addActionListener(this);					
		this.add(this.login);
		this.addSeparator(new Dimension(30,this.getHeight()));
		this.mails = new JButton("Mis correos", new ImageIcon(getClass().getResource("images/mails.png")));		
		this.mails.addActionListener(this);
		this.add(this.mails);
		this.newMail = new JButton("Enviar mail", new ImageIcon(getClass().getResource("images/send_mail.png")));
		this.newMail.addActionListener(this);
		this.add(this.newMail);
		this.addSeparator(new Dimension(30,this.getHeight()));
		this.contacts = new JButton("Contactos", new ImageIcon(getClass().getResource("images/contacts.png")));
		this.addContact = new JButton("Agregar contacto", new ImageIcon(getClass().getResource("images/add_contact.png")));
		this.add(this.contacts);
		this.contacts.addActionListener(this);
		this.add(this.addContact);
		this.addContact.addActionListener(this);
		this.addSeparator(new Dimension(20,this.getHeight()));
		this.configure = new JButton("", new ImageIcon(getClass().getResource("images/configure.png")));
		this.configure.addActionListener(this);
		this.add(this.configure);
		this.configureUI = new Configure(this.clientRef.getClientManager());		
		this.setFloatable(false);
	}	

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Acceder")) {
			new Login(this.clientRef);
		} else if(e.getActionCommand().equals("Salir")) {
			this.clientRef.changeStatus(false);
		} else if(e.getActionCommand().equals("Enviar mail")) {
			this.clientRef.addInternalFrame(new NewMail(this.clientRef.getClientManager()));
		} else if(e.getActionCommand().equals("Mis correos")) {
			this.clientRef.addInternalFrame(new MyMails(this.clientRef.getClientManager()));
		} else if(e.getActionCommand().equals("")) {
			this.clientRef.addInternalFrame(this.configureUI);
		}	
	}

	public void changeStatus(boolean isLogged) {
		if (isLogged) {
			this.newMail.setEnabled(true);
			this.mails.setEnabled(true);
			this.contacts.setEnabled(true);
			this.addContact.setEnabled(true);
			this.login.setIcon(new ImageIcon(getClass().getResource("images/sign_out.png")));
			this.login.setText("Salir");
		} else {
			this.newMail.setEnabled(false);
			this.mails.setEnabled(false);
			this.contacts.setEnabled(false);
			this.addContact.setEnabled(false);
			this.login.setIcon(new ImageIcon(getClass().getResource("images/sign_in.png")));
			this.login.setText("Acceder");
		}	
	}
}