package client.interfaces;
import client.functionality.ClientManager;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Configure extends JInternalFrame implements ActionListener {
	private ClientManager clientRef;
	private JLabel labelForDNSIp;
	private JTextField dnsIp;
	private JLabel labelForDNSPort;
	private JTextField dnsPort;

	public Configure(ClientManager clientRef) {		
		super("Configuraciones",false,true,false,false);
		this.setLayout(null);
		this.clientRef = clientRef;
		this.setTitle("Configuraciones.");		
		this.labelForDNSIp = new JLabel("Direccion del DNS:");
		this.labelForDNSIp.setBounds(10,30,120,20);
		this.add(this.labelForDNSIp);
		this.dnsIp = new JTextField(this.clientRef.getDNSIp());
		this.dnsIp.setBounds(130,30,120,20);
		this.add(this.dnsIp);
		this.labelForDNSPort = new JLabel("Puerto del DNS:");
		this.labelForDNSPort.setBounds(10,55,120,20);
		this.add(this.labelForDNSPort);
		this.dnsPort = new JTextField(this.clientRef.getDNSPort());
		this.dnsPort.setBounds(130,55,120,20);
		this.add(this.dnsPort);
		JButton aceptar = new JButton("Guardar");
		aceptar.setBounds(20,95,100,25);
		aceptar.addActionListener(this);
		this.add(aceptar);
		JButton cancelar = new JButton("Cancelar");		
		cancelar.setBounds(140,95,100,25);
		cancelar.addActionListener(this);
		JButton update = new JButton("Reflejar cambios");
		update.setBounds(10,5,140,20);
		update.addActionListener(this);
		this.add(update);

		this.add(cancelar);
		this.setSize(280,170);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Guardar")) {
			this.clientRef.setDNSIp(this.dnsIp.getText());
			try {
				this.clientRef.setDNSPort(Integer.parseInt(this.dnsPort.getText()));
			} catch (Exception err) {

			}
			this.dnsIp.setText(this.clientRef.getDNSIp());
			this.dnsPort.setText(String.valueOf(this.clientRef.getDNSPort()));
			this.setVisible(false);
		} if (e.getActionCommand().equals("Reflejar cambios")) {
			this.clientRef.poblateServers();
			this.setVisible(false);
		} else {
			this.setVisible(false);
		}
	}
}