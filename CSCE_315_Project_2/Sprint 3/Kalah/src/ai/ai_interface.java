package ai;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import server.Gameboard;

public class ai_interface {
	
	
	Socket client_socket;
	DataOutputStream server_out;
	BufferedReader server_in;
	String server_response;
	String[] str_buf = null;
	String server_ip = "localhost"; 	//Defaults to localhost aka 127.0.0.1
	int server_port = 23456;	//Defaults to an unused port
	
	Gameboard gb;
	String player_name = null;
	int player_id = 7;		//Player ID also corresponds to the player's home base (i.e. Player_1 ID = 0 and Player_2 ID = 7)
	int score = 0;
	int board_size = 14;
	int seeds_per_hole = 4;
	int move_time = 0;
	//Game data
	int[] board = new int[20];
	int[] board_side; //Holds side of board
	
	
	public static void main(String[] args) throws Exception{
		ai_interface ai = new ai_interface();
		ai.ai_run();
	}
	
	
	
	public void ai_run()throws Exception{
		connect();
		receive(); //WELCOME
		receive(); //INFO
		gb = new Gameboard(board_size);
		
		for(int i = 1; i < gb.getBoardSize(); i++){
			if(i != gb.getBoardSize()/2){
			gb.setTokens(i, board[i]);
			}
		}
		ai ai1 = new ai(gb, -1, player_id, player_id);
		int move;
		
		
		//receive(); //BEGIN FROM SERVER
		send("READY");
		
		while(true){ //Breaks by server sending win/loss condition
		
		if(player_id == gb.getBoardSize()/2){//PLAYER 1
		move = ai1.getMove(gb, 4);
		send("MOVE " + move + " " + player_id);
		System.out.println("AI Move: " + move);	
		gb.makeMove(move, player_id);
		receive(); //OK
		receive(); //MOVE
		}
		else{
			receive(); // MOVE player 2, auto updates board
			move = ai1.getMove(gb, 4);
			send("MOVE " + move + " " + player_id);
			System.out.println("AI Move: " + move);	
			gb.makeMove(move, player_id);
			receive(); //OK
		}
			
		}
		
	}
	
	public void send(String pkt) throws IOException {
		server_out.writeBytes(pkt + '\n');
			
	}
	
	public void connect() throws IOException {
		client_socket = new Socket(server_ip, server_port);
		server_out = new DataOutputStream(client_socket.getOutputStream());
		server_in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
	}
	
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
		
		
	} else if(server_response.contains("BEGIN")){
		//RECV BEGIN
	}else if(server_response.contains("MOVE")){
		String[] MOVE_parsed = server_response.split("\\s");
		int op_move = Integer.parseInt(MOVE_parsed[1]);
		int op_player = Integer.parseInt(MOVE_parsed[2]);
		gb.makeMove(op_move, op_player);
	} else if(server_response.contains("ILLEGAL")){
		//Illegal move
	} else if(server_response.contains("TIME")){
		//Current time
	} else if(server_response.contains("LOSER")){
		System.exit(0);
		//Display end screen
	} else if(server_response.contains("WINNER")){
		System.exit(0);
		//Display winner screen
	} else if(server_response.contains("TIE")){
		System.exit(0);
		//Display Tie screen
	} else if(server_response.contains("OK")){
		//OK
	}
	else{
		
	}
	
	
	}
	
	
	
	
	
	
	

}
