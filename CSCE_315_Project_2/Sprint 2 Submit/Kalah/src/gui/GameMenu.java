package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameMenu {

	public JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameMenu window = new GameMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 162, 202);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewLocalGame = new JButton("New Local Game");
		btnNewLocalGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewLocalGame.setBounds(6, 6, 147, 29);
		frame.getContentPane().add(btnNewLocalGame);
		
		JButton btnJoinLocalGame = new JButton("Join Local Game");
		btnJoinLocalGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnJoinLocalGame.setBounds(6, 88, 147, 29);
		frame.getContentPane().add(btnJoinLocalGame);
		
		JButton btnNewOnlineGame = new JButton("New Online Game");
		btnNewOnlineGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewOnlineGame.setBounds(6, 47, 147, 29);
		frame.getContentPane().add(btnNewOnlineGame);
		
		JButton btnJoinOnlineGame = new JButton("Join Online Game");
		btnJoinOnlineGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnJoinOnlineGame.setBounds(6, 129, 147, 29);
		frame.getContentPane().add(btnJoinOnlineGame);
	}
}
