package ai;

import java.io.IOException;
import java.util.Vector;

import server.Gameboard;

public class ai {
	Gameboard ai_board;
	Vector<ai> submoves = new Vector<ai>();
	private int root_player = 0; //Actual player
	private int turn_player = 0; //Player to perform move
	private int move = 0; //Move done by node
	private int root = 0;
	private int min_node = 0;
	private int value = 100; //Switches for Min nodes on creation
	int filled = 0;
	private int offset = 0; //Offset of home from zero (AKA for player 2, is + board.length/2)
	
	
	public ai(Gameboard gb, int move_todo, int root_p, int turn_p){
		ai_board = new Gameboard(gb.getBoardSize());
		ai_board.copyGameboard(gb);
		if(move_todo == -1){
			//ROOT NODE
		root_player = root_p; // Actual player
		turn_player = nextPlayer(gb,turn_p);
		move = move_todo;		
		value = -100;
		}
		else{
		if(turn_p == 0){//Player 2
			offset = gb.getBoardSize()/2;
		}
		root_player = root_p; // Actual player
		if(root_p == turn_p){ //Must be min node
			min_node = 1;
			value = -100;
		}
		turn_player = turn_p;
		move = move_todo;
		if(ai_board.makeMove(move_todo + offset, turn_p)){
		value = this.calcScore();
		}
	}
	}
	
	public int getMove(Gameboard gb, int diff){
		submoves.clear();
		filled = 0;
		value = -100; //root negative
		ai_board.copyGameboard(gb);
		for(int i = 0; i < diff; i++){
			this.ai_fill(ai_board, root_player, turn_player);
		}
		int best_move = this.evaluateTree(gb);
		
		if(best_move == -1){
			System.out.println("Game Over");
		}
		return best_move;
	}
	
	
	
	public void ai_fill(Gameboard gb, int root_p, int turn_p){
		if(filled == 0){ //Bottom node not Fill
			for(int i = 1; i < (gb.getBoardSize()/2); i++){
				ai sub_ai = new ai(ai_board, i, root_p, nextPlayer(gb,turn_p));
			submoves.add(sub_ai);
			}	
			filled = 1;
		}
			else{ // Filled call childern
				for(int q = 0; q < (gb.getBoardSize()/2)-1; q++){
				submoves.elementAt(q).ai_fill(ai_board, root_player, nextPlayer(ai_board, turn_player));
				}	
			}
		}
		
	
	public int evaluateTree(Gameboard gb){
		
		for(int i = 0; i < (gb.getBoardSize()/2)-1; i++){
			if(submoves.elementAt(0).filled == 0){
				//Evaluate at this level, next deeper is leafs	
			}
			else{ //Go next level down
				for(int q = 0; q < (gb.getBoardSize()/2)-1; q++){
					submoves.elementAt(q).evaluateTree(gb);
				}
			}
			for(int w = 0; w < (gb.getBoardSize()/2) -1;w ++){
				if(min_node == 0){
					//MAX NODE
					if(submoves.elementAt(w).value > value){
						value = submoves.elementAt(w).value;
					}
				}
				else{
					//MIN NODE
					if(submoves.elementAt(w).value < value){
						value = submoves.elementAt(w).value;
					}
				}
			}
		}
		
		//Reslove root node, returning move todo
		if(move == -1){
			for(int i = 0; i < (gb.getBoardSize()/2)-1; i++){
				if(root_player == 0){
				if(submoves.elementAt(i).value == value && ai_board.getTokens(i+1+(gb.getBoardSize()/2)) != 0){
					return i+1+gb.getBoardSize()/2;
				}
				}
		else{
				if(submoves.elementAt(i).value == value && ai_board.getTokens(i+1) != 0){
					return i+1;
				}
		    }		
		}	
		}
		//No optimal move found return furthest left (Mostly a safety measure)
		if(move == -1){
			for(int i = 0; i < (gb.getBoardSize()/2)-1; i++){
				if(root_player == 0){
				if(ai_board.getTokens(i+1+(gb.getBoardSize()/2)) != 0){
					return i+1+gb.getBoardSize()/2;
				}
				}
		else{
				if(ai_board.getTokens(i+1) != 0){
					return i+1;
				}
		    }
			}
		}
		
		return -1; //Move not found
}
	
		public int nextPlayer(Gameboard gb, int player_num){
			if(player_num == 0){
				return gb.getBoardSize()/2;
			}
			else{
				return 0;
			}
		}
		
		public int calcScore(){
			int score = 0; 
			//Heuristic algorithm used was developed by Neal Wu
			score = ai_board.getKalahTokens(root_player);
			score = score - ai_board.getKalahTokens(nextPlayer(ai_board, root_player));
			score = score + ai_board.getHomeTokens(root_player);
			score = score - ai_board.getHomeTokens(nextPlayer(ai_board, root_player));
			return score;
		}
		
		public int getValue(){
			return value;
		}
		
		public void setNode(int max){
			min_node = max;
		}
		
		public void setPoints(int p_in){
			value = p_in;
		}
	

}
