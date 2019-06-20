package ai;

import java.io.IOException;

import java.util.*;  
import ai.ai;
import server.Gameboard;

public class ai_manager {
	Vector<ai> ai_vec = new Vector<ai>();
	Gameboard rootboard = new Gameboard();
	int ai_player = 0;
	int depth = 0;
	int max_move = 0;
	int max_points = 0;
	int difficulty = 0;
	
	
	public static void main(String[] args)throws IOException {
		Gameboard gb_test = new Gameboard();
		ai_manager aim = new ai_manager(gb_test,4, 0);
		ai_simple aim2 = new ai_simple(7);
		
		
		int ai_move;
		//Move 1
		for(int i = 0; i < 40; i++){
		ai_move = aim.getMove(gb_test);
		System.out.println("AI move is: " + ai_move);
		if(ai_move == 3){
			System.out.println("Test");
		}
		gb_test.makeMove(ai_move,0);
		aim.refresh(gb_test);
		
		
		ai_move = aim2.getMove(gb_test);
		System.out.println("AI 2 move is: " + ai_move);
		gb_test.makeMove(ai_move,7);

		}
		
		
		
		
		//aim.createDepth(gb_test, 0);
		//System.out.println("HI");
		/*
		//Move 2
		ai_move = game_ai.ai_move(gb_test, 7);
		System.out.println("AI2 move is: " + ai_move);
		gb_test.makeMove(ai_move,7);
		
		//Move 3
		ai_move = game_ai.ai_move(gb_test, 0);
		System.out.println("AI move is: " + ai_move);
		gb_test.makeMove(ai_move,0);
		*/
		}
	
	public ai_manager(Gameboard gb, int difficulty_in, int player_num){
		//Create root node
		rootboard.copyGameboard(gb);
		ai_player = player_num;
		
		for(int i = 1; i < gb.getHome(); i++){
			ai add = new ai(gb, i, player_num);
			if(add.getPoints() > max_points){
				max_points = add.getPoints();
				max_move = i;
			}
			ai_vec.add(add);
		}
		depth = 1;
		difficulty = difficulty_in;
		//ai_player = player_num;
	}
	
	public int getMove(Gameboard gb){
		max_points = 0;
		max_move = 0;
		this.refresh(gb);
		for(int i = 0; i < difficulty; i++){
			this.createDepth(gb, ai_player);
		}
		return max_move;
	}
	
	
	public void refresh(Gameboard gb){
		rootboard.copyGameboard(gb);
		ai_vec.clear();
		
		max_move = 0;
		max_points = 0;
		
		for(int i = 1; i < gb.getHome(); i++){
			ai add = new ai(gb, i, ai_player);
			if(add.getPoints() >= max_points){
				max_points = add.getPoints();
				max_move = i;
			}
			ai_vec.add(add);
		}
		depth = 1;
	}
	
	//Creates next depth
	public void createDepth(Gameboard gb, int ai_player)
	{
		for(int i = 0; i < rootboard.getHome()-1; i++){
			ai_vec.elementAt(i).ai_fill(gb, ai_player);
		}
		depth++;
	}
	
	
	public void alpha(){}
	
	public void beta(){}
	
	
	//Switches AI to other player to compute next depth
	public void switch_player(Gameboard gb){
		if(ai_player == 0){
			ai_player = gb.getHome();
		}
		else{
			ai_player = 0;
		}
	}

}
