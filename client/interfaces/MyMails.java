package client.interfaces;
import client.functionality.ClientManager;
import javax.swing.JInternalFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.util.ArrayList;

public class MyMails extends JInternalFrame {
	private DefaultTableModel tableModel;
	private JTable table;
	private ClientManager clientRef;

	public MyMails(ClientManager clientRef) {
		super("Correos recibidos",false,true,false,true);
		this.clientRef = clientRef;
		this.tableModel = new DefaultTableModel();
		this.table = new JTable(this.tableModel);
		this.tableModel.addColumn("From");
		this.tableModel.addColumn("Asunto");		
		this.tableModel.addColumn("Mensaje");		
		ArrayList<String> mails = clientRef.getMyMails();
		for (String mail : mails) {
			String[] data = mail.split("[ ]");
			Object[] row = {data[2],data[3],data[4]};
			tableModel.addRow(row);
		}		
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.table.doLayout();
		JScrollPane jp = new JScrollPane(this.table);		
		this.add(jp);
		this.setSize(300,300);
	}
}