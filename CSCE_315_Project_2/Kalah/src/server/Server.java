package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.*;
import java.net.*;


public class Server extends Thread{
		private Gameboard gb;
		private Socket socket;
		private int thread_num;
		private int p1_move = 1; //USED to communicate two player moves between threads
		private int p2_move = 0;
		private int move_unsent = 0;
		int thread_player = 0;
		Server_data data;
		
		public Server(Socket sock, Gameboard gb_in, int thread, Server_data sd){
			gb = gb_in;
			socket = sock;
			thread_num = new Integer(thread);
			data = sd;
		}
		
		public void run(){
			try{
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		          DataOutputStream outtoClient = new DataOutputStream(socket.getOutputStream());
		          String request;
		          outtoClient.writeBytes("WELCOME" + '\n');
		          outtoClient.flush();
		          System.out.println("Client Connected on Thread" + thread_num);
		          gb.getBoard();
		          outtoClient.writeBytes(constructINFO(gb, thread_num) + '\n');
		          System.out.println("Server Sends:" +constructINFO(gb, thread_num));
		          outtoClient.flush();
		          
				 while(true){ //Take intput and reply to client
					 if(thread_num == 0 && data.playerMove() == 1 && data.unsent() == 1){
						 outtoClient.writeBytes(data.getLast() + '\n');
						data.setUnsent(0);
					 }else if(thread_num != 0 && data.playerMove() == 0 && data.unsent() == 1){
						 outtoClient.writeBytes(data.getLast() + '\n');
						 data.setUnsent(0);
					 }
					 
					if(input.ready()){
	            	 request = input.readLine();
	            	 System.out.println("Client sent: "+ request);
	            	 switch(request){
	                 case "READY":
	                
	                 	break;
	                 	
	                 case "OK":
	                	 
	                	 break;
	                	 
	                 case "NEW":
	                	 //RESTART GAME
	                	 break;
	                	 
	                 case "PIERULE":
	                	 //outtoClient.writeBytes("PIERULE" + '\n');
	                	 break;	 
	                	 
	                 default:
	                	 if(request.contains("MOVE")){
	                		 //MOVE
	                		 //Make move
	                		 if((getPlayer(gb, thread_num) == 0 && data.playerMove() == 0) || (getPlayer(gb, thread_num) != 0 && data.playerMove() == 1)){ //Is requesting players turn
	                		 String[] s_request = request.split(" ");
	                		 if(gb.makeMove(Integer.parseInt(s_request[1]),getPlayer(gb, thread_num))){ 
	                		 outtoClient.writeBytes("OK" + '\n');
	                		 gb.getBoard();
	                		 
	                		 data.setLast(request);
	                		 if(data.playerMove() == 1){ //Let other thread know
	                			data.switchPlayer();
	                			data.setUnsent(1);
	                		 }
	                		 else{
	                			 data.switchPlayer();
	                			 data.setUnsent(1);
	                		 }
	                		
	                		 
	                		 }
	                		 else{
	                			 outtoClient.writeBytes("ILLEGAL" + '\n');
	                		 }
	                		 }
	                	 }
	                	 else{
	                		 System.out.println("INVALID CLIENT REQUEST");
	                		 System.out.println("Request: " + request);
	                	 }
	                 }
	            	 //END OF SWITCH
	            
					}
					//END OF INPUT READY
	            }
			}
		catch(Exception e)
			{
			System.out.println("Error: Server Thread " + thread_num);
			}
		}
		
		public static void main(String[] args)throws IOException {
			Scanner line_in = new Scanner(System.in);
			int[] board_in = new int[10];//Larger than max board size
	        
			System.out.println("Server Running...");
			System.out.print("Houses Per Side: " );
			int houses_in = line_in.nextInt();
			System.out.print("Seeds Per House (0 if specified seeds per house is desired): ");
			int seeds_in = line_in.nextInt();
			
			if(seeds_in == 0){
				System.out.print("Seeds per house (1 2 3... format): ");
				for(int i = 0; i < houses_in; i++){
					board_in[i] = Integer.parseInt(line_in.next());
				}
				line_in.nextLine();
			}
			else{
				for(int i = 0; i < houses_in; i++){
					board_in[i] = seeds_in;
				}
			}
			System.out.print("Desired Turn Time: ");
			int time_turn = line_in.nextInt();
			
			
			ServerSocket listener = new ServerSocket(23456);
			int board_size = 2*houses_in+2;
			Gameboard gb = new Gameboard(2*houses_in+2);
			
			for(int i = 1; i < board_size; i++){
				if(i < board_size/2){
				gb.board[i] = board_in[i-1];
				}
				if(i > board_size/2){
				gb.board[i] = board_in[i-(board_size/2)-1];
				}
			}
			gb.board[0] = 0;
			gb.board[board_size/2] = 0;
			gb.houses_per_side = houses_in;
			gb.seeds_per_house = seeds_in;
			gb.time_per_turn = time_turn;
			
			System.out.println("Gameboard Setup Complete");
			gb.getBoard();
	
			int p1_move = 1;
			int p2_move = 0;
			int unsent = 0;
			String last_move;
			Server_data sd_in = new Server_data(gb, p1_move, " ");
			
			boolean initialize = true;
			int thread_num = 0;
			
            while (true) {
            	Socket socket = listener.accept();
            	
            	if(thread_num == 0){
                Server comThread = new Server(socket, gb, thread_num, sd_in);
                thread_num++;
                comThread.start();
            	}
            	else{
            		Server comThread2 = new Server(socket, gb, thread_num, sd_in);
                    thread_num++;
                    comThread2.start();
            	}
                } 
		}
		
		
		public int getPlayer(Gameboard gb, int thread){ //Need to add getBoardSize to Gameboard class
			if(thread == 0){
				return (gb.board_size)/2;
			}
			else{
				return 0;
			}
		}
		
		public String constructINFO(Gameboard gb, int thread_num){
			String info = "INFO ";
			info = info.concat(Integer.toString(gb.houses_per_side));
			info = info.concat(" ");
			info = info.concat(Integer.toString(gb.seeds_per_house));
			info = info.concat(" ");
			info = info.concat(Integer.toString(gb.time_per_turn));
			info = info.concat(" ");
			if(thread_num == 0){
				info = info.concat("F");
			}
			else{
				info = info.concat("S");
			}
			if(gb.seeds_per_house == 0){
				info = info.concat(" R");
				for(int i = 1; i <= gb.houses_per_side; i ++){
					info = info.concat(" ");
					info = info.concat(Integer.toString(gb.board[i]));
				}
			}
			return info;
		}
	
}
   
	








