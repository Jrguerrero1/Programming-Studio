//Developer: Travis Stewart

//References: Java GUI Tutorial -> http://cs.lmu.edu/~ray/notes/javagraphics/
//https://www.cs.utexas.edu/users/scottm/cs305j/javaCode/Assignment1/DrawingPanel.java
//https://www.cs.utexas.edu/~scottm/cs305j/handouts/slides/Topic11IntroToGraphics_4Up.pdf

package gui;

//Package imports
import client.Client;

//General imports
import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.EventQueue;


public class ClientGUI {

	//Player data
	String player_name = null;
	int player_id = 0;		//Player ID also corresponds to the player's home base (i.e. Player_1 ID = 0 and Player_2 ID = 7)
	int score = 0;
	int board_size = 14;
	int seeds_per_hole = 4;
	
	
	//Game data
	int[] board = new int[14];
	
	//Game Setup options
	boolean human_player_flag = false;
	boolean ai_player_flag = false;
	boolean single_player_flag = false;
	boolean online_multiplayer_flag = false;
	boolean local_multiplayer_flag = false;
	
	//Server Connection Settings
	Socket client_socket;
	DataOutputStream server_out;
	BufferedReader server_in;
	String server_response;
	String[] str_buf = null;
	String server_ip = "localhost"; 	//Defaults to localhost aka 127.0.0.1
	int server_port = 23456;	//Defaults to an unused port	
	
	
	//Utility Variables
	Scanner sc = new Scanner(System.in);	//User input
	int[] buf = new int[3];		//Packet buffer
	
	//Default constructor
	public ClientGUI() throws IOException{}
	
	public void menu() throws IOException {
//			int opt = -1;
//			boolean quit = false;
			
			MenuWindow menu_window = new MenuWindow();
			menu_window.frame.setVisible(true);
			
/*			while(!quit) {
				System.out.println("\nMenu Options");
				System.out.printf("%3d -- %s\n", 0, "New Game");
				System.out.printf("%3d -- %s\n", 1, "Rules");
				System.out.printf("%3d -- %s\n", 2, "Credits");
				System.out.printf("%3d -- %s\n", 3, "Quit");
		
				System.out.println();
				
				//Get user response
				System.out.print("Please enter an option: ");
				opt = sc.nextInt();
				
				switch(opt) {
					case 0: {
						quit = true;
						gameSetup();
						break;
					}
					case 1: {
						System.out.println("Display game rules...");
						break;
					}
					case 2: {
						credits();
						break;
					}
					case 3: {
						quit = true;
						System.out.println("\nQuitting application...Thank you for playing\n");
						break;
					}
					default: {
						System.out.println("Error: Invalid Option...");
						break;
					}
				}//END_SWITCH
			}//END_WHILE
		*/
	}
	
	public void supportedCommands() {
		System.out.println("\nSupported Commands");
		System.out.printf("%3d -- %s\n", 0, "Menu");
		System.out.printf("%3d -- %s\n", 1, "Move");
		System.out.printf("%3d -- %s\n", 2, "Validation");
		System.out.printf("%3d -- %s\n", 3, "Score");
		System.out.printf("%3d -- %s\n", 4, "State");
		System.out.printf("%3d -- %s\n", 5, "Config");
		System.out.printf("%3d -- %s\n", 6, "Reset");
		System.out.println();
			
	}
	
	public void credits() {
		System.out.printf("\n\t    %s\n\n" ,"Credits");
		System.out.printf("\t  %s\n\t%s\n\t%s\n\t%s\n", "Developers:", "Austin Langley", "Joaquin Guerrero", "Travis Stewart");
		System.out.printf("\n\t  %s\n\t%s \n\n", "References:", "TCP Client/Server example -> https://systembash.com/a-simple-java-tcp-server-and-tcp-client/");

	}
	
	
//--------------------Server Functions--------------------	
	
	//Connect to server
	public void connect() throws IOException {
		client_socket = new Socket(server_ip, server_port);
		server_out = new DataOutputStream(client_socket.getOutputStream());
		server_in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
	}
	
	public void askServerInfo() throws IOException {
		System.out.println("Enter server information: ");
		System.out.print("IP Address: ");
		server_ip = sc.nextLine();
		System.out.print("Port #: ");
		server_port = sc.nextInt();
		
		System.out.printf("Attempting to connect to server: %s:%d", server_ip, server_port);
		
		connect();
		
	}
	
	
	public void startServer() throws IOException {
		//TODO CREATE A SERVER THREAD FOR A LOCAL SERVER
	}
	
	public boolean checkConnection() throws IOException {
		
		return false;
	}

	
//--------------------Game Functions--------------------
	
	public void send(String pkt) throws IOException {
		connect();
		server_out.writeBytes(pkt + '\n');
			
	}
	
	//Get packet from server
	public void receive() throws IOException {
		server_response = null;
		server_response = server_in.readLine();
		str_buf = null;
		str_buf = server_response.split(" ");
		
		//System.out.println(server_response);
		if(str_buf.length <= 3) {
			for(int i = 0; i < str_buf.length; i++) {
				buf[i] = Integer.parseInt(str_buf[i]);
			}
			
		}
		else {		//Includes board data 
			board = new int[str_buf.length - 2];	//Derive board size from packet size
			
			buf[0] = Integer.parseInt(str_buf[0]);
			buf[1] = Integer.parseInt(str_buf[1]);
			buf[2] = Integer.parseInt(str_buf[2]);
			for(int i = 2; i < str_buf.length; i++) {
				board[i-2] = Integer.parseInt(str_buf[i]);
			}
		}
		client_socket.close();
	}
	
	public void gameSetup() throws IOException {	
		int opt = -1;
		boolean quit = false;
		while(!quit) {
			System.out.println("\nGame Setup");
			System.out.printf("%3d -- %s\n", 0, "Quick Match");	//Single player vs self
			System.out.printf("%3d -- %s\n", 1, "New Local Game");	//ADD SUB-MENU FOR VS. HUMAN OR AI
			System.out.printf("%3d -- %s\n", 2, "New Online Game");
			System.out.printf("%3d -- %s\n", 3, "Join Local Game");
			System.out.printf("%3d -- %s\n", 4, "Join Online Game");
			System.out.printf("%3d -- %s\n", 5, "Back");
	//		System.out.printf("%3d -- %s\n", 6, "Reset");
			System.out.println();
			
			//Get user response
			System.out.print("Please enter an option: ");
			opt = sc.nextInt();
		
			switch(opt) {
				case 0: {
					quit = true;
					quickMatch();	//Uses default options to start a single player game
					break;
				}
				case 1: {
					quit = true;
					newGame(opt);
					break;
				}
				case 2: {
					quit = true;
					newGame(opt);
					break;
				}
				case 3: {
					quit = true;
					joinGame(opt);
					break;
				}
				case 4: {
					quit = true;
					joinGame(opt);
					break;
				}
				case 5: {
					quit = true;
					menu();
					break;
				}
				case 6: {
					System.out.println("Opt 6 not configured...");
					break;
				}
				default: {
					System.out.println("Error: Invalid Option...");
					break;
				}
			}//END_SWITCH
		}//END_WHILE
		
	}
	
	public void consoleBoard() {
		System.out.println("\t\t\t\t   Game Board");
		System.out.printf("\t|···· %2d(%2d) ··· %2d(%2d) ··· %2d(%2d) ··· %2d(%2d) ··· %2d(%2d) ··· %2d(%2d) ····|\n", 13, board[13], 12, board[12], 11, board[11], 10, board[10], 9, board[9], 8, board[8] );
		System.out.printf(" %2d(%2d) ", 0, board[0]);
		System.out.print(String.format("| %69s |", "").replace(' ', '·'));
		System.out.printf(" %2d(%2d) \n", 7, board[7]);
		System.out.printf("\t|···· %2d(%2d) ··· %2d(%2d) ··· %2d(%2d) ··· %2d(%2d) ··· %2d(%2d) ··· %2d(%2d) ····|\n", 1, board[1], 2, board[2], 3, board[3], 4, board[4], 5, board[5], 6, board[6] );
		System.out.println();
		
		
	}
	
	public void quickMatch() throws IOException {
		//connect();	//Using default server settings 
		gameLoop();
		
	}
	
	public void newGame(int opt) throws IOException {
		switch(opt) {
			case 1: {	//Local
				connect();	//Using default server settings 
				gameLoop();
				break;
			}
			case 2: {	//Online
				//connect();	//Using default server settings 
				askServerInfo();
				gameLoop();
				break;
			}
			default: {
				System.out.println("Error: Invalid option...Returning to Game Setup");
				gameSetup();
				break;
			}
		}
		
	}
	
	public void joinGame(int opt) throws IOException {
		switch(opt) {
			case 3: {	//Local
				connect();	//Using default server settings 
				gameLoop();
				
				break;
			}
			case 4: {	//Online
				//connect();	//Using default server settings 
				askServerInfo();
				gameLoop();
				break;
			}
			default: {
				System.out.println("Error: Invalid option...Returning to Game Setup");
				gameSetup();
				break;
			}
		}
		
	}
	
	
	public void gameLoop() throws IOException {
		boolean gameOver = false;
		int opt = -1;
		send("4 "+ player_id +" 0");
		receive();
		
		while(!gameOver) {
//			supportedCommands();
//			consoleBoard();
//			//Get user response
//			System.out.print("Please enter an option: ");
//			opt = sc.nextInt();
			
			switch(opt) {
				case 0: {	//Menu
					break;
				}
				case 1: {	//Move
					System.out.print("Enter start point: ");
					int move = sc.nextInt();
					send("1 " + player_id + " " + move);
					receive();	//Update game state
					break;
				}
				case 2: {	//Player's turn?
					//0-Not players turn
					//1-Players turn
					
					break;
				}
				case 3: {	//Score
					
					break;
				}
				case 4: {	//State
					send("4 " + player_id + " 0"); 		//Send gameboard state request
					receive();
					break;
				}
				case 5: {	//Config
					
					break;
				}
				case 6: {	//Reset
					gameOver = true;
					System.out.println("\nGame Reset Called...\nGoing to main menu...\n");
					send("6 " + player_id + " 0");
					break;
				}
				default: {
					System.out.println("Error: Invalid Option...");
					break;
				}
			}//END_SWITCH
			send("4 " + player_id + " 0"); 		//Send gameboard state request
			receive();
		}//END_WHILE
		System.out.println("...Game Over...");
		client_socket.close();
		//menu();
		
	}

//================================= Window Classes =================================	
	
//-----------------MenuWindow-----------------
	public class MenuWindow {
		private JFrame frame;

		/**
		 * Launch the application.
		 */
//		public static void main(String[] args) {
//			EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						MenuWindow window = new MenuWindow();
//						window.frame.setVisible(true);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}

		/**
		 * Create the application.
		 */
		public MenuWindow() {
			initialize();
		}

		/**
		 * Initialize the contents of the frame.
		 */
		private void initialize() {
			frame = new JFrame();
			frame.setBounds(100, 100, 163, 140);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			JPanel panel = new JPanel();
			frame.getContentPane().add(panel, BorderLayout.CENTER);
			
			JButton btnNewGame = new JButton("New Game");
			btnNewGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GameMenu game_menu = new GameMenu();
					game_menu.frame.setVisible(true);
				}
			});
			panel.add(btnNewGame);
			
			JButton btnCredits = new JButton("Credits");
			btnCredits.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CreditsDialog dialog = new CreditsDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
			});
			panel.add(btnCredits);
			
			JButton btnQuit = new JButton("Quit");
			btnQuit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			panel.add(btnQuit);
		}

	}//MenuWindow END_OF_CLASS
	
	
	
	
	
//------------------GameMenu------------------	
	public class GameMenu {

		public JFrame frame;

		/**
		 * Launch the application.
		 */
//		public static void main(String[] args) {
//			EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						GameMenu window = new GameMenu();
//						window.frame.setVisible(true);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}

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
//			GameWindow game_window;
			
			JButton btnNewLocalGame = new JButton("New Local Game");
			btnNewLocalGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GameWindow game_window = new GameWindow();
					game_window.frame.setVisible(true);
					
//					EventQueue.invokeLater(new Runnable() {
//						public void run() {
//							try {
//								GameWindow game_window = new GameWindow();
//								game_window.frame.setVisible(true);
//								connect();
//								send("4 "+ player_id +" 0");
//								receive();
//								game_window.update();
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					});
					
					try{
						//connect();
						send("4 "+ player_id +" 0");
						receive();
					}catch(IOException e1) {
						e1.printStackTrace(System.out);
					}
					
					game_window.update();
					
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
	}//GameMenu END_OF_CLASS	
	
	
	
	
//-----------------GameWindow-----------------	
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
		
		
		// Buttons		
		private JButton btn0;
		private JButton btn1;
		private JButton btn2;
		private JButton btn3;
		private JButton btn4;
		private JButton btn5;
		private JButton btn6;
		private JButton btn7;
		private JButton btn8;
		private JButton btn9;
		private JButton btn10;
		private JButton btn11;
		private JButton btn12;
		private JButton btn13;
		private JButton btn14;
		private JButton btn15;
		private JButton btn16;
		private JButton btn17;
		private JButton btn18;
		private JButton btn19;
		
		JLabel lblP_0_Score;
		JLabel lblP_1_Score;
		//private JButton btn20;
		
	

		/**
		 * Create the application.
		 */
		public GameWindow() {
			btnSetup();
			initialize(board_size);
		}

		/**
		 * Initialize the contents of the frame.
		 */
		public void btnSetup() {
			btn0 = new JButton("0");
//			btn0.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					
//				}
//			});
			btn0.setFocusPainted(false);
		
			btn1 = new JButton("1");
			btn1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 1" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
					
				}
			});
			btn1.setFocusPainted(false);

			btn2 = new JButton("2");
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 2" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn2.setFocusPainted(false);
		
			btn3 = new JButton("3");
			btn3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 3" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn3.setFocusPainted(false);
		
			btn4 = new JButton("4");
			btn4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 4" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn4.setFocusPainted(false);
		
			btn5 = new JButton("5");
			btn5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 5" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn5.setFocusPainted(false);
			
			btn6 = new JButton("6");
			btn6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 6" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn6.setFocusPainted(false);

			btn7 = new JButton("7");
			btn7.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 7" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn7.setFocusPainted(false);
			
			btn8 = new JButton("8");
			btn8.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 8" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn8.setFocusPainted(false);
			
			btn9 = new JButton("9");
			btn9.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 9" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn9.setFocusPainted(false);
			
			btn10 = new JButton("10");
			btn10.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 10" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn10.setFocusPainted(false);
			
			btn11 = new JButton("11");
			btn11.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 11" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn11.setFocusPainted(false);
			
			btn12 = new JButton("12");
			btn12.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 12" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn12.setFocusPainted(false);

			btn13 = new JButton("13");
			btn13.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 13" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
					
				}
			});
			btn13.setFocusPainted(false);
			
			btn14 = new JButton("14");
			btn14.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 14" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn14.setFocusPainted(false);


			
			btn15 = new JButton("15");
			btn15.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 15" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn15.setFocusPainted(false);


			
			btn16 = new JButton("16");
			btn16.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 16" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn16.setFocusPainted(false);


			
			btn17 = new JButton("17");
			btn17.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 17" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn17.setFocusPainted(false);


			
			btn18 = new JButton("18");
			btn18.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 18" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn18.setFocusPainted(false);


			
			btn19 = new JButton("19");
			btn19.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("1 " + player_id + " 19" );
						receive();
						send("4 0 0");
						receive();
						update();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn19.setFocusPainted(false);
			
		}
		
		public void initialize(int size) {
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.getContentPane().setLayout(null);
			
			lblP_1_Score = new JLabel("Player 1: 0");
			lblP_1_Score.setBounds(6, 23, 100, 16);
			frame.getContentPane().add(lblP_1_Score);
			
			lblP_0_Score = new JLabel("Player 2: 0");
			lblP_0_Score.setBounds(6, 51, 100, 16);
			frame.getContentPane().add(lblP_0_Score);
			
			
			
			
			switch(size) {
				case 10: {	// 4 bins
					btn0.setBounds(x, y, score_width, score_height);
					frame.getContentPane().add(btn0);
					
					btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn1);
					
					btn9.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn9);
					
					btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn2);
					
					btn8.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn8);
					
					btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn3);
					
					btn7.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn7);
					
					btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn4);
					
					btn6.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn6);
					
					btn5.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
					frame.getContentPane().add(btn5);
				
					break;
				}
				case 12: {	// 5 bins
					btn0.setBounds(x, y, score_width, score_height);
					btn0.setEnabled(false);
					frame.getContentPane().add(btn0);
					
					btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn1);
					
					btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn11);
					
					btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn2);
					
					btn10.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn10);
					
					btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn3);
					
					btn9.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn9);
					
					btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn4);
					
					btn8.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn8);
					
					btn5.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn5);
					
					btn7.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn7);
					
					btn6.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
					btn6.setEnabled(false);
					frame.getContentPane().add(btn6);
					
					break;
				}
				case 14: {	// 6 bins
					btn0.setBounds(x, y, score_width, score_height);
					btn0.setEnabled(false);
					frame.getContentPane().add(btn0);
					
					btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn1);
					
					btn13.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn13);
					
					btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn2);
					
					btn12.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn12);
					
					btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn3);
					
					btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn11);
					
					btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn4);
					
					btn10.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn10);
					
					btn5.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn5);
					
					btn9.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn9);
					
					btn6.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn6);
					
					btn8.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn8);
					
					btn7.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
					btn7.setEnabled(false);
					frame.getContentPane().add(btn7);
								
					break;
				}
				case 16: {	// 7 bins
					btn0.setBounds(x, y, score_width, score_height);
					btn0.setEnabled(false);
					frame.getContentPane().add(btn0);
					
					btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn1);
					
					btn15.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn15);
					
					btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn2);
					
					btn14.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn14);
					
					btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn3);
					
					btn13.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn13);
					
					btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn4);
					
					btn12.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn12);
					
					btn5.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn5);
					
					btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn11);
					
					btn6.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn6);
					
					btn10.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn10);
					
					btn7.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn7);
					
					btn9.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn9);
					
					btn8.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
					btn8.setEnabled(false);
					frame.getContentPane().add(btn8);
					
					break;
				}
				case 18: {	// 8 bins
					btn0.setBounds(x, y, score_width, score_height);
					btn0.setEnabled(false);
					frame.getContentPane().add(btn0);
					
					btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn1);
					
					btn17.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn17);
					
					btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn2);
					
					btn16.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn16);
					
					btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn3);
					
					btn15.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn15);
					
					btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn4);
					
					btn14.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn14);
					
					btn5.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn5);
					
					btn13.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn13);
					
					btn6.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn6);
					
					btn12.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn12);
					
					btn7.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn7);
					
					btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn11);
					
					btn8.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn8);
					
					btn10.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn10);
					
					btn9.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
					btn9.setEnabled(false);
					frame.getContentPane().add(btn9);
					
					break;
				}
				case 20: {	// 9 bins
					btn0.setBounds(x, y, score_width, score_height);
					btn0.setEnabled(false);
					frame.getContentPane().add(btn0);
					
					btn1.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn1);
					
					btn19.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn19);
					
					btn2.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn2);
					
					btn18.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn18);
					
					btn3.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn3);
					
					btn17.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn17);
					
					btn4.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn4);
					
					btn16.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn16);
					
					btn5.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn5);
					
					btn15.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn15);
					
					btn6.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn6);
					
					btn14.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn14);
					
					btn7.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn7);
					
					btn13.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn13);
					
					btn8.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn8);
					
					btn12.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn12);
					
					btn9.setBounds((x + ( ++compensate * spacing)), (y + score_height - btn_height), btn_width, btn_height);
					frame.getContentPane().add(btn9);
					
					btn11.setBounds((x + ( compensate * spacing)), y, btn_width, btn_height);
					frame.getContentPane().add(btn11);
					
					btn10.setBounds((x + ( ++compensate * spacing)), y, score_width, score_height);
					btn10.setEnabled(false);
					frame.getContentPane().add(btn10);
					
					break;
				}
				default: {
					break;
				}
			}			
			frame.setBounds(100, 100, ((2 * x) + score_width +(compensate * spacing)), (y + score_height + 100));
			
			int w = frame.getWidth() / 2;
			
			JLabel lblCurrentPlayer = new JLabel("Current Player: " + player_id);
			lblCurrentPlayer.setBounds((w - 60), 20, 120, 30);
			frame.getContentPane().add(lblCurrentPlayer);
			
			JButton btnSwitchPlayer = new JButton("Switch Player");
			btnSwitchPlayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(player_id == 0) {
						player_id = 7;
						lblCurrentPlayer.setText("Current Player: " + player_id);
					}
					else {
						player_id = 0;
						lblCurrentPlayer.setText("Current Player: " + player_id);
					}
				}
			});
			btnSwitchPlayer.setBounds((w - 62), 50, 124, 30);
			frame.getContentPane().add(btnSwitchPlayer);
			
			
		}
		
		public void update() {
			
			switch(board_size) {
				case 10: {
					btn0.setText(Integer.toString(board[0]));
					btn1.setText(Integer.toString(board[1]));
					btn2.setText(Integer.toString(board[2]));
					btn3.setText(Integer.toString(board[3]));
					btn4.setText(Integer.toString(board[4]));
					btn5.setText(Integer.toString(board[5]));
					btn6.setText(Integer.toString(board[6]));
					btn7.setText(Integer.toString(board[7]));
					btn8.setText(Integer.toString(board[8]));
					btn9.setText(Integer.toString(board[9]));
					
					lblP_0_Score.setText("Player 2: " + Integer.toString(board[0]));
					lblP_1_Score.setText("Player 1: " + Integer.toString(board[5]));
					
					break;
				}
				case 12: {
					btn0.setText(Integer.toString(board[0]));
					btn1.setText(Integer.toString(board[1]));
					btn2.setText(Integer.toString(board[2]));
					btn3.setText(Integer.toString(board[3]));
					btn4.setText(Integer.toString(board[4]));
					btn5.setText(Integer.toString(board[5]));
					btn6.setText(Integer.toString(board[6]));
					btn7.setText(Integer.toString(board[7]));
					btn8.setText(Integer.toString(board[8]));
					btn9.setText(Integer.toString(board[9]));
					btn10.setText(Integer.toString(board[10]));
					btn11.setText(Integer.toString(board[11]));
					
					lblP_0_Score.setText("Player 2: " + Integer.toString(board[0]));
					lblP_1_Score.setText("Player 1: " + Integer.toString(board[6]));
					
					break;
				}
				case 14: {
					btn0.setText(Integer.toString(board[0]));
					btn1.setText(Integer.toString(board[1]));
					btn2.setText(Integer.toString(board[2]));
					btn3.setText(Integer.toString(board[3]));
					btn4.setText(Integer.toString(board[4]));
					btn5.setText(Integer.toString(board[5]));
					btn6.setText(Integer.toString(board[6]));
					btn7.setText(Integer.toString(board[7]));
					btn8.setText(Integer.toString(board[8]));
					btn9.setText(Integer.toString(board[9]));
					btn10.setText(Integer.toString(board[10]));
					btn11.setText(Integer.toString(board[11]));
					btn12.setText(Integer.toString(board[12]));
					btn13.setText(Integer.toString(board[13]));
					
					lblP_0_Score.setText("Player 2: " + Integer.toString(board[0]));
					lblP_1_Score.setText("Player 1: " + Integer.toString(board[7]));
					
					break;
				}
				case 16: {
					btn0.setText(Integer.toString(board[0]));
					btn1.setText(Integer.toString(board[1]));
					btn2.setText(Integer.toString(board[2]));
					btn3.setText(Integer.toString(board[3]));
					btn4.setText(Integer.toString(board[4]));
					btn5.setText(Integer.toString(board[5]));
					btn6.setText(Integer.toString(board[6]));
					btn7.setText(Integer.toString(board[7]));
					btn8.setText(Integer.toString(board[8]));
					btn9.setText(Integer.toString(board[9]));
					btn10.setText(Integer.toString(board[10]));
					btn11.setText(Integer.toString(board[11]));
					btn12.setText(Integer.toString(board[12]));
					btn13.setText(Integer.toString(board[13]));
					btn14.setText(Integer.toString(board[14]));
					btn15.setText(Integer.toString(board[15]));
					
					lblP_0_Score.setText("Player 2: " + Integer.toString(board[0]));
					lblP_1_Score.setText("Player 1: " + Integer.toString(board[8]));
					
					break;
				}
				case 18: {
					btn0.setText(Integer.toString(board[0]));
					btn1.setText(Integer.toString(board[1]));
					btn2.setText(Integer.toString(board[2]));
					btn3.setText(Integer.toString(board[3]));
					btn4.setText(Integer.toString(board[4]));
					btn5.setText(Integer.toString(board[5]));
					btn6.setText(Integer.toString(board[6]));
					btn7.setText(Integer.toString(board[7]));
					btn8.setText(Integer.toString(board[8]));
					btn9.setText(Integer.toString(board[9]));
					btn10.setText(Integer.toString(board[10]));
					btn11.setText(Integer.toString(board[11]));
					btn12.setText(Integer.toString(board[12]));
					btn13.setText(Integer.toString(board[13]));
					btn14.setText(Integer.toString(board[14]));
					btn15.setText(Integer.toString(board[15]));
					btn16.setText(Integer.toString(board[16]));
					btn17.setText(Integer.toString(board[17]));
					
					lblP_0_Score.setText("Player 2: " + Integer.toString(board[0]));
					lblP_1_Score.setText("Player 1: " + Integer.toString(board[9]));
					
					break;
				}
				case 20: {
					btn0.setText(Integer.toString(board[0]));
					btn1.setText(Integer.toString(board[1]));
					btn2.setText(Integer.toString(board[2]));
					btn3.setText(Integer.toString(board[3]));
					btn4.setText(Integer.toString(board[4]));
					btn5.setText(Integer.toString(board[5]));
					btn6.setText(Integer.toString(board[6]));
					btn7.setText(Integer.toString(board[7]));
					btn8.setText(Integer.toString(board[8]));
					btn9.setText(Integer.toString(board[9]));
					btn10.setText(Integer.toString(board[10]));
					btn11.setText(Integer.toString(board[11]));
					btn12.setText(Integer.toString(board[12]));
					btn13.setText(Integer.toString(board[13]));
					btn14.setText(Integer.toString(board[14]));
					btn15.setText(Integer.toString(board[15]));
					btn16.setText(Integer.toString(board[16]));
					btn17.setText(Integer.toString(board[17]));
					btn18.setText(Integer.toString(board[18]));
					btn19.setText(Integer.toString(board[19]));
					
					lblP_0_Score.setText("Player 2: " + Integer.toString(board[0]));
					lblP_1_Score.setText("Player 1: " + Integer.toString(board[10]));
					
					break;
				}
			
			}
			frame.repaint();
			
		}
		
	}//GameWindow END_OF_CLASS


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//ClientGUI END_OF_CLASS
