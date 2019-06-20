package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JLabel;

public class GameWindow {

	private JFrame frame;
	private int x = 75;
	private int y = 125;
	private int score_height = 175;
	private int score_width = 60;
	private int btn_width = 60;
	private int btn_height = 60;
	private int spacing = 77;
	private int compensate = 0;
	
	
	public int[] board = new int[14];
	int board_size = 5;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameWindow window = new GameWindow();
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
	public GameWindow() {
		initialize(board_size);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize(int num_btns) {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSwitchPlayer = new JButton("Switch Player");
		btnSwitchPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSwitchPlayer.setBounds(87, 6, 123, 29);
		frame.getContentPane().add(btnSwitchPlayer);
		
		JLabel lblCurrentPlayer = new JLabel("Current Player");
		lblCurrentPlayer.setBounds(118, 47, 61, 16);
		frame.getContentPane().add(lblCurrentPlayer);
		
		switch(num_btns) {
			case 4: {
				JButton btn0 = new JButton("0");
				btn0.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn0.setFocusPainted(false);
				btn0.setBounds(x, y, score_width, score_height);
				frame.getContentPane().add(btn0);
				
				JButton btn1 = new JButton("1");
				btn1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						board[1]++;
						btn1.setText(Integer.toString(board[1]));
					}
				});
				btn1.setFocusPainted(false);
				btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn1);
				
				JButton btn13 = new JButton("13");
				btn13.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn13.setFocusPainted(false);
				btn13.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn13);
				
				JButton btn2 = new JButton("2");
				btn2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn2.setFocusPainted(false);
				btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn2);
				
				JButton btn12 = new JButton("12");
				btn12.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn12.setFocusPainted(false);
				btn12.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn12);
				
				JButton btn3 = new JButton("3");
				btn3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn3.setFocusPainted(false);
				btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn3);
				
				JButton btn11 = new JButton("11");
				btn11.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn11.setFocusPainted(false);
				btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn11);
				
				JButton btn4 = new JButton("4");
				btn4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn4.setFocusPainted(false);
				btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn4);
				
				JButton btn10 = new JButton("10");
				btn10.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn10.setFocusPainted(false);
				btn10.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn10);
				
				JButton btn5 = new JButton("5");
				btn5.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn5.setFocusPainted(false);
				btn5.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
				frame.getContentPane().add(btn5);
				
				JLabel lblP_0_Score = new JLabel("Player 1: ");
				lblP_0_Score.setBounds(6, 23, 61, 16);
				frame.getContentPane().add(lblP_0_Score);
				
				JLabel lblP_1_Score = new JLabel("Player 2: ");
				lblP_1_Score.setBounds(6, 51, 61, 16);
				frame.getContentPane().add(lblP_1_Score);
				
				break;
			}
			case 5: {
				JButton btn0 = new JButton("0");
				btn0.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn0.setFocusPainted(false);
				btn0.setBounds(x, y, score_width, score_height);
				frame.getContentPane().add(btn0);
				
				JButton btn1 = new JButton("1");
				btn1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						board[1]++;
						btn1.setText(Integer.toString(board[1]));
					}
				});
				btn1.setFocusPainted(false);
				btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn1);
				
				JButton btn13 = new JButton("13");
				btn13.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn13.setFocusPainted(false);
				btn13.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn13);
				
				JButton btn2 = new JButton("2");
				btn2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn2.setFocusPainted(false);
				btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn2);
				
				JButton btn12 = new JButton("12");
				btn12.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn12.setFocusPainted(false);
				btn12.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn12);
				
				JButton btn3 = new JButton("3");
				btn3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn3.setFocusPainted(false);
				btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn3);
				
				JButton btn11 = new JButton("11");
				btn11.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn11.setFocusPainted(false);
				btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn11);
				
				JButton btn4 = new JButton("4");
				btn4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn4.setFocusPainted(false);
				btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn4);
				
				JButton btn10 = new JButton("10");
				btn10.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn10.setFocusPainted(false);
				btn10.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn10);
				
				JButton btn5 = new JButton("5");
				btn5.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn5.setFocusPainted(false);
				btn5.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn5);
				
				JButton btn9 = new JButton("9");
				btn9.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn9.setFocusPainted(false);
				btn9.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn9);
				
				JButton btn6 = new JButton("6");
				btn6.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn6.setFocusPainted(false);
				btn6.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
				frame.getContentPane().add(btn6);
				
				
				JLabel lblP_0_Score = new JLabel("Player 1: ");
				lblP_0_Score.setBounds(6, 23, 61, 16);
				frame.getContentPane().add(lblP_0_Score);
				
				JLabel lblP_1_Score = new JLabel("Player 2: ");
				lblP_1_Score.setBounds(6, 51, 61, 16);
				frame.getContentPane().add(lblP_1_Score);
				
				break;
			}
			case 6: {
				JButton btn0 = new JButton("0");
				btn0.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn0.setFocusPainted(false);
				btn0.setBounds(x, y, score_width, score_height);
				frame.getContentPane().add(btn0);
				
				JButton btn1 = new JButton("1");
				btn1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						board[1]++;
						btn1.setText(Integer.toString(board[1]));
					}
				});
				btn1.setFocusPainted(false);
				btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn1);
				
				JButton btn13 = new JButton("13");
				btn13.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn13.setFocusPainted(false);
				btn13.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn13);
				
				JButton btn2 = new JButton("2");
				btn2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn2.setFocusPainted(false);
				btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn2);
				
				JButton btn12 = new JButton("12");
				btn12.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn12.setFocusPainted(false);
				btn12.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn12);
				
				JButton btn3 = new JButton("3");
				btn3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn3.setFocusPainted(false);
				btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn3);
				
				JButton btn11 = new JButton("11");
				btn11.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn11.setFocusPainted(false);
				btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn11);
				
				JButton btn4 = new JButton("4");
				btn4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn4.setFocusPainted(false);
				btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn4);
				
				JButton btn10 = new JButton("10");
				btn10.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn10.setFocusPainted(false);
				btn10.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn10);
				
				JButton btn5 = new JButton("5");
				btn5.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn5.setFocusPainted(false);
				btn5.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn5);
				
				JButton btn9 = new JButton("9");
				btn9.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn9.setFocusPainted(false);
				btn9.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn9);
				
				JButton btn6 = new JButton("6");
				btn6.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn6.setFocusPainted(false);
				btn6.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn6);
				
				JButton btn8 = new JButton("8");
				btn8.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn8.setFocusPainted(false);
				btn8.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn8);
				
				JButton btn7 = new JButton("7");
				btn7.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn7.setFocusPainted(false);
				btn7.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
				frame.getContentPane().add(btn7);
				
				JLabel lblP_0_Score = new JLabel("Player 1: ");
				lblP_0_Score.setBounds(6, 23, 61, 16);
				frame.getContentPane().add(lblP_0_Score);
				
				JLabel lblP_1_Score = new JLabel("Player 2: ");
				lblP_1_Score.setBounds(6, 51, 61, 16);
				frame.getContentPane().add(lblP_1_Score);
				
				break;
			}
			case 7: {
				JButton btn0 = new JButton("0");
				btn0.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn0.setFocusPainted(false);
				btn0.setBounds(x, y, score_width, score_height);
				frame.getContentPane().add(btn0);
				
				JButton btn1 = new JButton("1");
				btn1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						board[1]++;
						btn1.setText(Integer.toString(board[1]));
					}
				});
				btn1.setFocusPainted(false);
				btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn1);
				
				JButton btn13 = new JButton("13");
				btn13.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn13.setFocusPainted(false);
				btn13.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn13);
				
				JButton btn2 = new JButton("2");
				btn2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn2.setFocusPainted(false);
				btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn2);
				
				JButton btn12 = new JButton("12");
				btn12.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn12.setFocusPainted(false);
				btn12.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn12);
				
				JButton btn3 = new JButton("3");
				btn3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn3.setFocusPainted(false);
				btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn3);
				
				JButton btn11 = new JButton("11");
				btn11.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn11.setFocusPainted(false);
				btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn11);
				
				JButton btn4 = new JButton("4");
				btn4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn4.setFocusPainted(false);
				btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn4);
				
				JButton btn10 = new JButton("10");
				btn10.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn10.setFocusPainted(false);
				btn10.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn10);
				
				JButton btn5 = new JButton("5");
				btn5.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn5.setFocusPainted(false);
				btn5.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn5);
				
				JButton btn9 = new JButton("9");
				btn9.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn9.setFocusPainted(false);
				btn9.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn9);
				
				JButton btn6 = new JButton("6");
				btn6.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn6.setFocusPainted(false);
				btn6.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn6);
				
				JButton btn8 = new JButton("8");
				btn8.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn8.setFocusPainted(false);
				btn8.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn8);
				
				JButton btn14 = new JButton("14");
				btn14.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn14.setFocusPainted(false);
				btn14.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn14);
				
				JButton btn15 = new JButton("15");
				btn15.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn15.setFocusPainted(false);
				btn15.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn15);
				
				JButton btn7 = new JButton("7");
				btn7.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn7.setFocusPainted(false);
				btn7.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
				frame.getContentPane().add(btn7);
				
				JLabel lblP_0_Score = new JLabel("Player 1: ");
				lblP_0_Score.setBounds(6, 23, 61, 16);
				frame.getContentPane().add(lblP_0_Score);
				
				JLabel lblP_1_Score = new JLabel("Player 2: ");
				lblP_1_Score.setBounds(6, 51, 61, 16);
				frame.getContentPane().add(lblP_1_Score);
				
				break;
			}
			case 8: {
				JButton btn0 = new JButton("0");
				btn0.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn0.setFocusPainted(false);
				btn0.setBounds(x, y, score_width, score_height);
				frame.getContentPane().add(btn0);
				
				JButton btn1 = new JButton("1");
				btn1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						board[1]++;
						btn1.setText(Integer.toString(board[1]));
					}
				});
				btn1.setFocusPainted(false);
				btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn1);
				
				JButton btn13 = new JButton("13");
				btn13.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn13.setFocusPainted(false);
				btn13.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn13);
				
				JButton btn2 = new JButton("2");
				btn2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn2.setFocusPainted(false);
				btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn2);
				
				JButton btn12 = new JButton("12");
				btn12.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn12.setFocusPainted(false);
				btn12.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn12);
				
				JButton btn3 = new JButton("3");
				btn3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn3.setFocusPainted(false);
				btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn3);
				
				JButton btn11 = new JButton("11");
				btn11.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn11.setFocusPainted(false);
				btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn11);
				
				JButton btn4 = new JButton("4");
				btn4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn4.setFocusPainted(false);
				btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn4);
				
				JButton btn10 = new JButton("10");
				btn10.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn10.setFocusPainted(false);
				btn10.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn10);
				
				JButton btn5 = new JButton("5");
				btn5.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn5.setFocusPainted(false);
				btn5.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn5);
				
				JButton btn9 = new JButton("9");
				btn9.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn9.setFocusPainted(false);
				btn9.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn9);
				
				JButton btn6 = new JButton("6");
				btn6.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn6.setFocusPainted(false);
				btn6.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn6);
				
				JButton btn8 = new JButton("8");
				btn8.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn8.setFocusPainted(false);
				btn8.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn8);
				
				JButton btn14 = new JButton("14");
				btn14.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn14.setFocusPainted(false);
				btn14.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn14);
				
				JButton btn15 = new JButton("15");
				btn15.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn15.setFocusPainted(false);
				btn15.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn15);
				
				JButton btn16 = new JButton("16");
				btn16.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn16.setFocusPainted(false);
				btn16.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn16);
				
				JButton btn17 = new JButton("17");
				btn17.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn17.setFocusPainted(false);
				btn17.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn17);
				
				JButton btn7 = new JButton("7");
				btn7.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn7.setFocusPainted(false);
				btn7.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
				frame.getContentPane().add(btn7);
				
				JLabel lblP_0_Score = new JLabel("Player 1: ");
				lblP_0_Score.setBounds(6, 23, 61, 16);
				frame.getContentPane().add(lblP_0_Score);
				
				JLabel lblP_1_Score = new JLabel("Player 2: ");
				lblP_1_Score.setBounds(6, 51, 61, 16);
				frame.getContentPane().add(lblP_1_Score);
				
				break;
			}
			case 9: {
				JButton btn0 = new JButton("0");
				btn0.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn0.setFocusPainted(false);
				btn0.setBounds(x, y, score_width, score_height);
				frame.getContentPane().add(btn0);
				
				JButton btn1 = new JButton("1");
				btn1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						board[1]++;
						btn1.setText(Integer.toString(board[1]));
					}
				});
				btn1.setFocusPainted(false);
				btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn1);
				
				JButton btn13 = new JButton("13");
				btn13.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn13.setFocusPainted(false);
				btn13.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn13);
				
				JButton btn2 = new JButton("2");
				btn2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn2.setFocusPainted(false);
				btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn2);
				
				JButton btn12 = new JButton("12");
				btn12.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn12.setFocusPainted(false);
				btn12.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn12);
				
				JButton btn3 = new JButton("3");
				btn3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn3.setFocusPainted(false);
				btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn3);
				
				JButton btn11 = new JButton("11");
				btn11.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn11.setFocusPainted(false);
				btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn11);
				
				JButton btn4 = new JButton("4");
				btn4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn4.setFocusPainted(false);
				btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn4);
				
				JButton btn10 = new JButton("10");
				btn10.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn10.setFocusPainted(false);
				btn10.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn10);
				
				JButton btn5 = new JButton("5");
				btn5.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn5.setFocusPainted(false);
				btn5.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn5);
				
				JButton btn9 = new JButton("9");
				btn9.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn9.setFocusPainted(false);
				btn9.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn9);
				
				JButton btn6 = new JButton("6");
				btn6.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn6.setFocusPainted(false);
				btn6.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn6);
				
				JButton btn8 = new JButton("8");
				btn8.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn8.setFocusPainted(false);
				btn8.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn8);
				
				JButton btn14 = new JButton("14");
				btn14.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn14.setFocusPainted(false);
				btn14.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn14);
				
				JButton btn15 = new JButton("15");
				btn15.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn15.setFocusPainted(false);
				btn15.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn15);
				
				JButton btn16 = new JButton("16");
				btn16.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn16.setFocusPainted(false);
				btn16.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn16);
				
				JButton btn17 = new JButton("17");
				btn17.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn17.setFocusPainted(false);
				btn17.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn17);
				
				JButton btn18 = new JButton("18");
				btn18.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn18.setFocusPainted(false);
				btn18.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
				frame.getContentPane().add(btn18);
				
				JButton btn19 = new JButton("19");
				btn19.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn19.setFocusPainted(false);
				btn19.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
				frame.getContentPane().add(btn19);
				
				JButton btn7 = new JButton("7");
				btn7.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btn7.setFocusPainted(false);
				btn7.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
				frame.getContentPane().add(btn7);
				
				JLabel lblP_0_Score = new JLabel("Player 1: ");
				lblP_0_Score.setBounds(6, 23, 61, 16);
				frame.getContentPane().add(lblP_0_Score);
				
				JLabel lblP_1_Score = new JLabel("Player 2: ");
				lblP_1_Score.setBounds(6, 51, 61, 16);
				frame.getContentPane().add(lblP_1_Score);
				
				break;
			}
			default: {
				break;
			}
		}
		
		frame.setBounds(100, 100, ((2 * x) + score_width +(compensate * spacing)), (y + score_height + 100));
	}
}
