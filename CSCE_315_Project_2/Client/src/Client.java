//Developer: Travis Stewart



//Resources: TCP Client/Server example -> https://systembash.com/a-simple-java-tcp-server-and-tcp-client/

import java.util.*;
import java.io.*;
import java.net.*;



public class Client {
	
	//Player data
	String player_name = null;
	int player_id = 0;		//Player ID also corresponds to the player's home base (i.e. Player_1 ID = 0 and Player_2 ID = 7)
	int score = 0;
	
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
	Client() throws IOException{}
	
	public void menu() throws IOException {
		//try {
			int opt = -1;
			boolean quit = false;
			while(!quit) {
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
						//quit = true;
						credits();
						//menu();
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
//		}
//		catch(IOException e) {
//			
//		}
		
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
		
		//Update board state
		send("4 0 0");
		receive();
	}
	
	public void startServer() throws IOException {
		//TODO CREATE A SERVER THREAD FOR A LOCAL SERVER
	}
	
	public boolean checkConnection() throws IOException {
		
		return false;
	}

	
//--------------------Game Functions--------------------
	
	public void send(String pkt) throws IOException {
		server_out.writeBytes(pkt + '\n');
			
	}
	
	//Get packet from server
	public void receive() throws IOException {
		server_response = null;
		server_response = server_in.readLine();
		str_buf = null;
		str_buf = server_response.split(" ");
		
		if(str_buf.length <= 3) {
			for(int i = 0; i < str_buf.length; i++) {
				buf[i] = Integer.parseInt(str_buf[i]);
			}
		}
		else {		//Includes board data 
			buf[0] = Integer.parseInt(str_buf[0]);
			buf[1] = Integer.parseInt(str_buf[1]);
			buf[2] = Integer.parseInt(str_buf[2]);
			for(int i = 2; i < str_buf.length; i++) {
				board[i-2] = Integer.parseInt(str_buf[i]);
			}
		}

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
		connect();	//Using default server settings 
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
				connect();	//Using default server settings 
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
				connect();	//Using default server settings 
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
		
		while(!gameOver) {
			supportedCommands();
			consoleBoard();
			//Get user response
			System.out.print("Please enter an option: ");
			opt = sc.nextInt();
			
			switch(opt) {
				case 0: {	//Menu
					break;
				}
				case 1: {	//Move
					System.out.print("Enter start point: ");
					int move = sc.nextInt();
					send("1 " + player_id + " " + move);
					//receive();	//Update game state
					System.out.println(move);
					break;
				}
				case 2: {	//Validation
					
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
				
		}//END_WHILE
		System.out.println("...Game Over...");
		client_socket.close();
		menu();
		
	}
}
