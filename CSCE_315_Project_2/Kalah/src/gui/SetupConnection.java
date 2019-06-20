package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SetupConnection extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtAddress;
	private JTextField txtPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SetupConnection dialog = new SetupConnection();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SetupConnection() {
		setBounds(100, 100, 307, 142);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblServerIp = new JLabel("Server IP:");
			lblServerIp.setBounds(25, 6, 61, 16);
			contentPanel.add(lblServerIp);
		}
		{
			JLabel lblPort = new JLabel("    Port #:");
			lblPort.setBounds(25, 34, 61, 16);
			contentPanel.add(lblPort);
		}
		{
			txtAddress = new JTextField();
			txtAddress.setText("127.0.0.1");
			txtAddress.setBounds(98, 1, 170, 26);
			contentPanel.add(txtAddress);
			txtAddress.setColumns(10);
		}
		{
			txtPort = new JTextField();
			txtPort.setText("23456");
			txtPort.setBounds(98, 29, 170, 26);
			contentPanel.add(txtPort);
			txtPort.setColumns(10);
		}
		{
			JButton btnConnect = new JButton("Connect");
			btnConnect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				
				}
			});
			btnConnect.setBounds(98, 67, 117, 29);
			contentPanel.add(btnConnect);
		}
	}

}
