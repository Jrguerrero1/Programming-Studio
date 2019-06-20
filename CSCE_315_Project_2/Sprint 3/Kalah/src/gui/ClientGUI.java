//Developer: Travis Stewart

//References: Java GUI Tutorial -> http://cs.lmu.edu/~ray/notes/javagraphics/

package gui;

//Package imports
import client.Client;
import server.Gameboard;

//General imports
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class ClientGUI {

	//Player data
	String player_name = null;
	int player_id = 7;		//Player ID also corresponds to the player's home base (i.e. Player_1 ID = 0 and Player_2 ID = 7)
	int score = 0;
	int board_size = 14;
	int seeds_per_hole = 4;
	int move_time = 0;
	
	JLabel lblCurrentPlayer;
	boolean pie_rule = false;
	
	
	//Game data
	Gameboard gb;
	int[] board;// = new int[14];
	int[] board_side; //Holds side of board
	
	
	//Server Connection Settings
	Socket client_socket;
	DataOutputStream server_out;
	BufferedReader server_in;
	String server_response;
	String[] str_buf = null;
	String server_ip = "localhost"; 	//Defaults to localhost aka 127.0.0.1
	int server_port = 23456;	//Defaults to an unused port	
	String r_str = null;	//Contains the server's last message to this client

	
	
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
	
//--------------------Game Functions--------------------
	
	public void send(String pkt) throws IOException {
		server_out.writeBytes(pkt + '\n');
			
	}
	
	//Get packet from server
	public void receive() throws IOException {
		server_response = server_in.readLine();
		System.out.println("Server sent: " +server_response);
		
	if(server_response.contains("WELCOME")){
			//HANDLE WELCOME
	} else if(server_response.contains("INFO")){
			//INFO message parsed below
			String[] INFO_parsed = server_response.split("\\s");
			board_size = 2*Integer.parseInt(INFO_parsed[1])+ 2; 
			seeds_per_hole = Integer.parseInt(INFO_parsed[2]);
			board = new int[board_size];
			
			//Fills holes on board
			for(int i =1; i < board_size; i++){
				if(i != board_size/2){
					board[i] = seeds_per_hole;
				}
			}
			move_time = Integer.parseInt(INFO_parsed[3]);
			player_id = 0;
			if(INFO_parsed[4].contains("F")){
				player_id = board_size/2;
			}
			board_side = new int[Integer.parseInt(INFO_parsed[1])];
			/*Create an array for one side of board */
		if(INFO_parsed.length > 5){
			for(int i = 1; i < board_size; i++){
				if(i < board_size/2){
				board[i] = Integer.parseInt(INFO_parsed[5+i]);
				}
				if(i > board_size/2){
				board[i] = Integer.parseInt(INFO_parsed[5+i-(board_size/2)]);
				}
			}	
		}
		
		gb = new Gameboard(board_size);
		gb.board = board;
		
		
	} else if(server_response.contains("BEGIN")){
		//RECV BEGIN
		r_str = "BEGIN";
	}else if(server_response.contains("MOVE")){
		//UPDATE LOCAL BOARD WITH MOVE/Clients Turn
		r_str = Integer.toString(player_id);
		String[] MOVE_parsed = server_response.split("\\s");
		int op_move = Integer.parseInt(MOVE_parsed[1]);
		//int op_player = Integer.parseInt(MOVE_parsed[2]);
		
		if(player_id == 0) {	//Received player 1's move
			gb.makeMove(op_move, board_size/2);
		}
		else{	//Received player 2's move
			gb.makeMove(op_move, 0);
		}
	} else if(server_response.contains("ILLEGAL")){
		//Illegal move
		r_str = "ILLEGAL";
		System.exit(0);
		
	} else if(server_response.contains("TIME")){
		//Current time
		r_str = "TIME";
	} else if(server_response.contains("LOSER")){
		//Display end screen
		r_str = "LOSER";
		ErrorDialog errDiag = new ErrorDialog("Game Over... Loser...");
		errDiag.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		errDiag.setAlwaysOnTop(true);
		errDiag.setVisible(true);
		System.exit(0);

		
	} else if(server_response.contains("WINNER")){
		//Display winner screen
		r_str = "WINNER";
		ErrorDialog errDiag = new ErrorDialog("Game Over... Winner...");
		errDiag.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		errDiag.setAlwaysOnTop(true);
		errDiag.setVisible(true);
		System.exit(0);

	} else if(server_response.contains("TIE")){
		//Display Tie screen
		r_str = "TIE";
		ErrorDialog errDiag = new ErrorDialog("Game Over... Tie...");
		errDiag.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		errDiag.setAlwaysOnTop(true);
		errDiag.setVisible(true);
		System.exit(0);

	} else if(server_response.contains("OK")){
		//OK
		r_str = "OK";
	} else if(server_response.contains("PIERULE")){
//			pie_rule = true;
//			if(player_id == 0) {
//				player_id = board_size / 2;
//			}
//			else {
//				player_id = 0;
//			}
		
	}
	
	
	}
	

//================================= Window Classes =================================	
	
//-----------------MenuWindow-----------------
	public class MenuWindow {
		private JFrame frame;

		public MenuWindow() {
			initialize();
		}

		//Initialize the contents of the frame.
		private void initialize() {
			frame = new JFrame();
			frame.setBounds(100, 100, 240, 140);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			JPanel panel = new JPanel();
			frame.getContentPane().add(panel, BorderLayout.CENTER);
			panel.setLayout(null);
			
			JButton btnNewGame = new JButton("New Game");
			btnNewGame.setBounds(65, 5, 110, 29);
			btnNewGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GameMenu game_menu = new GameMenu();
					game_menu.frame.setVisible(true);
				}
			});
			panel.add(btnNewGame);
			
			JButton btnCredits = new JButton("Credits");
			btnCredits.setBounds(20, 42, 90, 29);
			btnCredits.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CreditsDialog dialog = new CreditsDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
			});
			panel.add(btnCredits);
			
			JButton btnQuit = new JButton("Quit");
			btnQuit.setBounds(75, 80, 90, 29);
			btnQuit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			panel.add(btnQuit);
			
			
			JButton btnRules = new JButton("Rules");
			btnRules.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//TODO: OPEN DIALOG DISPLAYING THE GAME RULES
				}
			});
			btnRules.setBounds(130, 42, 90, 29);
			panel.add(btnRules);
			
		}

	}//MenuWindow END_OF_CLASS
	
	
	
//------------------GameMenu------------------	
	public class GameMenu {
		public JFrame frame;

		public GameMenu() {
			initialize();
		}

		private void initialize() {
			frame = new JFrame();
			frame.setBounds(100, 100, 324, 155);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.getContentPane().setLayout(null);
//			GameWindow game_window;
			
			JButton btnNewLocalGame = new JButton("New Local Game");
			btnNewLocalGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						connect();
						receive();	
						receive();//INFO struct		
//						
						send("READY");
						if(player_id == 0) receive();
						
					}catch(IOException e1) {
						e1.printStackTrace(System.out);
					}
					
					GameWindow game_window = new GameWindow();
					game_window.frame.setVisible(true);
					game_window.update();
					
					
				}
			});
			btnNewLocalGame.setBounds(6, 6, 147, 29);
			frame.getContentPane().add(btnNewLocalGame);
			
			JButton btnJoinLocalGame = new JButton("Join Local Game");
			btnJoinLocalGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						connect();
						receive();	
						receive();//INFO struct		
//						
						send("READY");
						if(player_id == 0) receive();
						
					}catch(IOException e1) {
						e1.printStackTrace(System.out);
					}
					
					GameWindow game_window = new GameWindow();
					game_window.frame.setVisible(true);
					game_window.update();
					
				}
			});
			btnJoinLocalGame.setBounds(6, 47, 147, 29);
			frame.getContentPane().add(btnJoinLocalGame);
			
			JButton btnNewOnlineGame = new JButton("New Online Game");
			btnNewOnlineGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SetupConnection dialog = new SetupConnection();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
				}
			});
			btnNewOnlineGame.setBounds(165, 6, 147, 29);
			frame.getContentPane().add(btnNewOnlineGame);
			
			JButton btnJoinOnlineGame = new JButton("Join Online Game");
			btnJoinOnlineGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SetupConnection dialog = new SetupConnection();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);		
				}
			});
			btnJoinOnlineGame.setBounds(165, 47, 147, 29);
			frame.getContentPane().add(btnJoinOnlineGame);
			
			
			JButton btnBack = new JButton("Back");
			btnBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
				}
			});
			btnBack.setBounds(112, 88, 90, 29);
			frame.getContentPane().add(btnBack);
		}
	}//GameMenu END_OF_CLASS	
	
	
	public class SetupConnection extends JDialog {

		private final JPanel contentPanel = new JPanel();
		private JTextField txtAddress;
		private JTextField txtPort;

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
						server_ip = txtAddress.getText();
						server_port =  Integer.parseInt(txtPort.getText());
						
						try{
							connect();
							receive();	
							receive();//INFO struct		
//							
							send("READY");
							if(player_id == 0) receive();
							
						}catch(IOException e1) {
							e1.printStackTrace(System.out);
							ErrorDialog errDiag = new ErrorDialog("Error when trying to connect...");
							errDiag.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							errDiag.setVisible(true);
						}
						
						GameWindow game_window = new GameWindow();
						game_window.frame.setVisible(true);
						game_window.update();
						
						dispose();
					}
				});
				btnConnect.setBounds(98, 67, 117, 29);
				contentPanel.add(btnConnect);
			}
		}

	}

	
	
	
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
		
		JLabel lblServerMessage;
		JLabel lblMsg;
		
	

		/**
		 * Create the application.
		 */
		public GameWindow() {
			btnSetup();
			initialize(board_size);
			
//			
//			if(player_id == 0){
//				try {
//					receive();
//				} catch (IOException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}
//				
//			}
			
			
			
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
					try { //TODO: Change to new format, calc new board for all buttons
						send("MOVE 1 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(1, player_id);
							update();
						}
						receive();	//Wait for other player's move
						update();	//update player game board
						////playerTurn();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
					
				}
			});
			btn1.setFocusPainted(false);

			btn2 = new JButton("2");
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try { //TODO: Change to new format
						send("MOVE 2 " + player_id );
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(2, player_id);
							update();
						}
						receive();
						update();
						////playerTurn();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn2.setFocusPainted(false);
		
			btn3 = new JButton("3");
			btn3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try { //TODO: Change to new format
						send("MOVE 3 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(3, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn3.setFocusPainted(false);
		
			btn4 = new JButton("4");
			btn4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try { //TODO: Change to new format
						send("MOVE 4 " + player_id );
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(4, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
				}
			});
			btn4.setFocusPainted(false);
		
			btn5 = new JButton("5");
			btn5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try { //TODO: Change to new format
						send("MOVE 5 " + player_id );
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(5, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 6 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(6, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 7 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(7, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 8 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(8, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 9 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(9, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 10 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(10, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 11 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(11, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 12 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(12, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 13 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(13, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 14 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(14, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 15 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(15, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 16 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(16, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 17 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(17, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 18 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(18, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
						send("MOVE 19 " + player_id);
						receive(); //OK
						if(r_str == "OK"){
							gb.makeMove(19, player_id);
							update();
						}
						receive();
						update();
						//playerTurn();
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
			frame.setAlwaysOnTop(true);
			frame.getContentPane().setLayout(null);
			
			lblP_0_Score = new JLabel("Player 1: 0");
			lblP_0_Score.setBounds(6, 23, 100, 16);
			frame.getContentPane().add(lblP_0_Score);
			
			lblP_1_Score = new JLabel("Player 2: 0");
			lblP_1_Score.setBounds(6, 51, 100, 16);
			frame.getContentPane().add(lblP_1_Score);
			
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
			
			String tmp_str;
			if(player_id == 0){
				tmp_str = "Player 2";
			}
			else{
				tmp_str = "Player 1";
			}
			
			lblCurrentPlayer = new JLabel("Current Player: " + tmp_str);
			lblCurrentPlayer.setBounds((w - 60), 20, 200, 30);
			frame.getContentPane().add(lblCurrentPlayer);
			
			JButton btnSwitchPlayer = new JButton("Pie Rule");
			btnSwitchPlayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					try {
//						send("PIERULE");
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					if(player_id == 0) {
//						player_id = board_size / 2;
//						lblCurrentPlayer.setText("Current Player: Player 1" );
//					}
//					else {
//						player_id = 0;
//						lblCurrentPlayer.setText("Current Player: Player 2");
//					}
//					pie_rule = false;
//					frame.remove(btnSwitchPlayer);
				}
			});
			btnSwitchPlayer.setBounds((w - 60), 50, 124, 30);
			//if(player_id == 0)
			//frame.getContentPane().add(btnSwitchPlayer);
			
			JButton btnGiveUp = new JButton("Give Up");
			btnGiveUp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						send("NEW");
					}catch(IOException e2) {
						e2.printStackTrace(System.out);
					}
					frame.dispose();
					//TODO: DISPLAY A GAME OVER SCREEN
					
				}
			});
			btnGiveUp.setBounds((frame.getWidth() - 120), 6, 110, 29);
			frame.getContentPane().add(btnGiveUp);
			
			
			lblServerMessage = new JLabel("Server Message:");
			lblServerMessage.setBounds(10, frame.getHeight() - 60, 105, 16);
			frame.getContentPane().add(lblServerMessage);
			
			lblMsg = new JLabel("");
			lblMsg.setBounds(120, frame.getHeight() - 60, 200, 16);
			frame.getContentPane().add(lblMsg);
			
			
		}
		
		public void swapPlayer() {
			if(player_id == 0) {
				lblCurrentPlayer.setText("Current Player: Player 1" );
			}
			else {
				lblCurrentPlayer.setText("Current Player: Player 2");
			}
			pie_rule = false;
			
		}
		
		public void update() {
			//Get updated board state from game board
			board = gb.returnboard();
			if(pie_rule) swapPlayer();

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
					
					lblP_0_Score.setText("Player 1: " + Integer.toString(board[5]));
					lblP_1_Score.setText("Player 2: " + Integer.toString(board[0]));
					
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
					
					lblP_0_Score.setText("Player 1: " + Integer.toString(board[6]));
					lblP_1_Score.setText("Player 2: " + Integer.toString(board[0]));
					
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
					
					lblP_0_Score.setText("Player 1: " + Integer.toString(board[7]));
					lblP_1_Score.setText("Player 2: " + Integer.toString(board[0]));
					
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
					
					lblP_0_Score.setText("Player 1: " + Integer.toString(board[8]));
					lblP_1_Score.setText("Player 2: " + Integer.toString(board[0]));
					
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
					
					lblP_0_Score.setText("Player 1: " + Integer.toString(board[9]));
					lblP_1_Score.setText("Player 2: " + Integer.toString(board[0]));
					
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
					
					lblP_0_Score.setText("Player 1: " + Integer.toString(board[10]));
					lblP_1_Score.setText("Player 2: " + Integer.toString(board[0]));
					
					break;
				}
			
			}
			lblMsg.setText(r_str);
			frame.repaint();	
		}
		
	}//GameWindow END_OF_CLASS


	
	
	
	
	
	public static void main(String[] args) {
		try {
			ClientGUI cg = new ClientGUI();
			cg.menu();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}//ClientGUI END_OF_CLASS
