package ai;

import java.io.IOException;
import java.util.Vector;

import server.Gameboard;	

public class ai_simple {
	Vector<ai> ai_vec = new Vector<ai>();
	Gameboard rootboard = new Gameboard();
	private int max_points = -1;
	private int max_move = -1;
	private int ai_player = 0;
	
	public static void main(String[] args)throws IOException {
		Gameboard gb = new Gameboard();
		ai_simple ai1 = new ai_simple(0);
		ai_simple ai2 = new ai_simple(7);
		int move;
		
		for(int i = 0; i < 30; i++){
			move = ai1.getMove(gb);
			System.out.println("Player 1: " + move);
			gb.makeMove(move,0);
			move = ai2.getMove(gb);
			System.out.println("Player 2: " + move);
			gb.makeMove(move,7);
			System.out.println("Board: " + gb.getBoard());
		}
	}


	public ai_simple(int player_in){
		ai_player = player_in;
	}
	
	public int getMove(Gameboard gb){
		ai_vec.clear();
		max_points = -1;
		max_move = -1;
		
		for(int i = 1; i < gb.getHome(); i++){
			rootboard.copyGameboard(gb);
			int pts = rootboard.calcScoredSimple(i, ai_player);
			if( pts > max_points){
				max_points = pts;
				max_move = i;
			}
	}
		return max_move;
	}	
	
	
	}