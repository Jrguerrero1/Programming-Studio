package server;

import java.net.Socket;

public class Server_data {
	private Gameboard gb;
	private int p1_move = 1; //USED to communicate two player moves between threads
	private int move_unsent = 0;
	String last_move;
	
	
	Server_data(Gameboard gb_in, int p1,String mov){
		gb = gb_in;
		p1_move = p1;
		last_move = mov;
	}
	
	public synchronized int playerMove(){
		if(p1_move == 1){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	public synchronized void switchPlayer(){
		if(p1_move == 1){
			p1_move = 0;
		}
		else{
			p1_move = 1;
		}
	}
	
	public synchronized int unsent(){
		if(move_unsent == 1){
			return 1;
		}
		return 0;
	}
	
	public synchronized void setUnsent(int set){
		move_unsent = set;
	}
	
	public synchronized void setLast(String in){
		last_move = in;
	}
	
	public synchronized String getLast(){
		return last_move;
	}
	
	
}
