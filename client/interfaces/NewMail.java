package client.interfaces;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import client.functionality.ClientManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NewMail extends JInternalFrame implements ActionListener {	
	private ClientManager clientRef;
	private JTextField mails;
	private JTextField subject;
	private JTextArea body;
	private JLabel errors;

	public NewMail(ClientManager clientRef) {		
		super("Escibir correo",false,true,false,true);
		this.setLayout(null);
		JLabel labelForMails = new JLabel("Para:");
		labelForMails.setBounds(10,10,70,20);
		this.add(labelForMails);
		mails = new JTextField();
		mails.setBounds(80,10,250,20);
		this.add(this.mails);
		JLabel labelForSubject = new JLabel("Asunto:");
		labelForSubject.setBounds(10,40,70,20);
		this.add(labelForSubject);
		this.subject = new JTextField();
		this.subject.setBounds(80,40,250,20);
		this.add(this.subject);
		JLabel labelForBody = new JLabel("Contenido:");
		labelForBody.setBounds(10,70,70,20);
		this.add(labelForBody);
		this.body = new JTextArea();
		JScrollPane bodySp = new JScrollPane(this.body);
		bodySp.setBounds(80,70,400,300);
		this.add(bodySp);
		this.errors = new JLabel("");
		this.errors.setForeground(java.awt.Color.RED);
		this.errors.setBounds(80,370,400,20);
		this.add(this.errors);
		JButton enviar = new JButton("Enviar",new ImageIcon(getClass().getResource("../images/send.png")));
		enviar.setBounds(80,390,120,35);
		enviar.addActionListener(this);
		this.add(enviar);
		JButton cancelar = new JButton("Cancelar");
		cancelar.setBounds(220,390,100,35);
		cancelar.addActionListener(this);
		this.add(cancelar);
		this.clientRef = clientRef;
		this.setSize(500,470);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Enviar")) {
			this.errors.setText("");							
			if(this.mails.getText().trim().length() > 0) {
				String[] emails = this.mails.getText().trim().split(",");
				if(this.subject.getText().trim().length() > 0) {
					if(this.body.getText().trim().length() > 0) {
						Object result = this.clientRef.sendMail(emails,this.subject.getText(),this.body.getText());			
						(new javax.swing.JOptionPane()).showMessageDialog(null,result);
						this.dispose();
					} else {
						this.errors.setText("Esta seguro de que enviara un correo.");		
					}
				} else {
					this.errors.setText("No ha definido un tema.");	
				}
			} else {
				this.errors.setText("No hay ningun destinatario.");
			}			
		} else {
			this.setVisible(false);
			this.dispose();
		}
	}
}