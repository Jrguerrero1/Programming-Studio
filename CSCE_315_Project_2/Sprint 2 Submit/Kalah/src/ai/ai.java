package ai;

import java.io.IOException;
import java.util.Vector;

import server.Gameboard;

public class ai {
	
	Gameboard ai_board = new Gameboard();
	Vector<ai> submoves = new Vector<ai>();
	private int move = 1; //Stores best found move
	private int player = 0; //Stores which player calls
	private int points = 0; //Stores points from this node
	private int filled = 0; //Are all sub moves filled?
	private int max_node = 0; //Is this node a max node?
	private int minmax = 0; //If possible solution to max/min of childern
	
	
	public ai(Gameboard gb, int move_todo, int player_num){
		ai_board.copyGameboard(gb);
		player = player_num;
		move = move_todo;
		points = ai_board.calcScored(move, player);
	}
	
	public void ai_fill(Gameboard gb, int player_num){
		if(filled == 0){ //Bottom node not Fill
		for(int i = 1; i < gb.getHome(); i++){
			ai sub_ai = new ai(ai_board, i, nextPlayer(gb,player_num));
		if(max_node == 0){//Make next node Max node
			sub_ai.setNode(1);
			}
			sub_ai.setPoints(sub_ai.ai_board.calcScored(i, nextPlayer(gb,player_num)));
		if(max_node == 0){
			//Parent is Min
			if(sub_ai.getPoints() <= minmax){
				move = i;
				minmax = sub_ai.getPoints();
			}
			else{
				if(sub_ai.getPoints() >= minmax){
					move = i;
					minmax = sub_ai.getPoints();
				}
			}
		}	
			submoves.add(sub_ai);
		}
		filled = 1;
		}
		else{ // Filled call childern
			for(int q = 0; q <gb.getHome()-1; q++){
			submoves.elementAt(q).ai_fill(ai_board, nextPlayer(gb, player_num));
			}
			
		}
	}
	
	public int nextPlayer(Gameboard gb, int player_num){
		if(player_num == 0){
			return gb.getHome();
		}
		else{
			return 0;
		}
	}
	
	public int getPoints(){
		return points;
	}
	
	public void setNode(int max){
		max_node = max;
	}
	
	public void setPoints(int p_in){
		points = p_in;
	}
		
	}
	