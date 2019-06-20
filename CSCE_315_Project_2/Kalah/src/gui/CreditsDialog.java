package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreditsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CreditsDialog dialog = new CreditsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CreditsDialog() {
		String str = "\n\t    Credits\n\n";
		str += "\t  Developers:\n\tAustin Langley\n\tJoaquin Guerrero\n\tTravis Stewart\n";
		str += "\n\t  References:\n\t";
		str += "TCP Client/Server example -> https://systembash.com/a-simple-java-tcp-server-and-tcp-client/ \n\n";
		
		setBounds(100, 100, 1000, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(1, 0, 0, 0));
		{
			JTextArea txtrCreditstext = new JTextArea();
			txtrCreditstext.setText(str);
			txtrCreditstext.setEditable(false);
			contentPanel.add(txtrCreditstext);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton closeButton = new JButton("Close");
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				closeButton.setActionCommand("Close");
				buttonPane.add(closeButton);
				getRootPane().setDefaultButton(closeButton);
			}
		}
	}

}
