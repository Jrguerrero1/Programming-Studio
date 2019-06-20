package server;


public class Gameboard {
	public int [] board;
	private int player_turn;
	private int temp_turn;
	private int player_call;
	String sboardState;
	private static int player1_score;
	private static int player2_score;
	
	int board_size = 14;
	int houses_per_side = 0;
	int seeds_per_house = 0;
	int time_per_turn = 0;
	private int player_1_id = 7;
	private int player_2_id = 0;
	
	
	//Constructor
	public Gameboard(int size){
		//Geneartes default game board
		board_size = size;
		board = new int[board_size];
		for(int i = 0; i < board.length;i++){
			if(i == 0 || i == 7){
				board[i] = 0;
			}
			else{
			board[i] = 4;
			}
		}
		
		player_1_id = board_size / 2;
		
		player_turn = player_1_id; //Starts as player 1s turn
	}
	
	//Make calling gameboard same as passed
		public void copyGameboard(Gameboard gb_in){
			int [] board_in = gb_in.returnboard();
				for(int i = 0; i < gb_in.board_size; i++){
					board[i] = board_in[i];
				}
				 player_turn = gb_in.getTurn();
				player_call = gb_in.getCall();
		}
		
	//Number of tokens at certain pos of the board
	public int getTokens(int pos){
		return board[pos];
	}
	
	// Takes a position and sets tokens in that pos to passed value
	public void setTokens(int pos, int num){
		board[pos] = num;
	}
	
	//Increaes tokens in pos by 1 (used in makeMove()))
	public void addToken(int pos){
		if(pos > board.length){
			System.out.println("Error here! (ERROR 3423)");
			pos = pos - board_size;
		}
		board[pos] += 1;
	}
	
	//Returns which players turn it is
	public int getTurn(){
		return player_turn;
	}
	
	//Sets game to next players turn
	public void nextPlayer(){
		if(player_turn == 0){
			player_turn = player_1_id;
		}
		else{
			player_turn = player_2_id;
		}
	}
	
	//Attempts to pickup, checks for valid move (Players home/tokens in spot)
	public int pickUp(int pos){
		
		if(getTokens(pos) != 0){
			if(player_call == player_turn && player_turn == player_1_id && pos < player_1_id && pos > 0){
			int tokens = getTokens(pos);
			setTokens(pos,0);
			return tokens;
		}
			else if(player_call == player_turn && player_turn == player_2_id && pos < board_size && pos > player_1_id){ //TODO: switch 0 to getHome()
			int tokens = getTokens(pos);
			setTokens(pos,0);
			return tokens;
				
		}
			else{
				return -1;
			}
		}
		else{
			return -1;
		}
	}
	
	//Main function that is called to make move
	public boolean makeMove(int pos, int player){
		player_call = player;
		if(pos > board_size ){
			return false;
		}
		
		int tokens = pickUp(pos);
		
		if(tokens != -1){
			//Could pickup
			//Travis' Version
			int dropAt = pos;
			for(int t = tokens; t > 0; t--){
				dropAt++;
				if(player_turn == 0) {	//Player 2 turn 
					if(dropAt == board_size){
						dropAt = 0;
						addToken(dropAt);
					}
					else if(dropAt == player_1_id){
						dropAt++;
						addToken(dropAt);
					}
					else{
						addToken(dropAt);
					}		
				}
				else{	//Player 1 turn
					if(dropAt == board_size){
						dropAt = 1;
						addToken(dropAt);
					}
					else{
						addToken(dropAt);
					}			
				}
				
			}//END_OF_FOR
			
			
			//Check 'Land on Zero' rule
			if(getTokens(dropAt) == 1 && dropAt != player_1_id && dropAt != player_2_id) {	//Result after landing on zero 
				int sum = 1;
				if(player_turn == 0 && dropAt > player_1_id){		//Player 2 turn
					sum += getTokens(board_size - dropAt);
					setTokens(board_size - dropAt, 0);
					board[player_2_id] += sum;
					setTokens(dropAt, 0);
				}
				else if(player_turn == player_1_id && dropAt < player_1_id){	//Player 1 turn
					sum += getTokens(board_size - dropAt);
					setTokens(board_size - dropAt, 0);
					board[player_1_id] += sum;
					setTokens(dropAt, 0);
				}
			}
		

			nextPlayer();
			return true;
		}
		else{
			//Failed to pickup
			//System.out.println("Invalid Move!");
			return false;
		}
	}
	// Score system...
	// This is the Kalah the ones in the end for each player
	// Need rule implementation
	public void score(String player,int tokens){
		if(player== "1"){
			for(int i=0;i<tokens;i++)
			{
				Gameboard.player1_score ++;
			}
		}
		else {
			for(int i=0;i<tokens;i++)
			{
				Gameboard.player1_score ++;
			}
		}
		 
}
	
	//need to add gameover message on gameboard gui
	public boolean checkGameOver(){

		int sum1=0;
		int sum2=0;
		
		for(int i= board_size -1;i>board_size/2;i--){//grabs the houses 
			sum2+=getTokens(i);
		}
		
		for(int i=0;i<board_size/2;i++){
			sum1+=getTokens(i);
		}
		if(sum2==0|| sum1==0) {
			if(sum1==0){
				board[player_1_id]+=sum2;
				for(int i= board_size -1;i>board_size/2;i--){//grabs the houses 
					setTokens(i,0);
				}
				return true;
			}
			else {
				board[player_2_id]+=sum1;
				for(int i=0;i<board_size/2;i++){
					setTokens(i,0);
				}
				return true;
			}
		}
		else {
			return false;
		}
			
		
	}
	
	public void pieRule(){//changes the player to other player after first move
		
		if(player_turn==player_1_id){
			player_turn=player_2_id;
			temp_turn=player_2_id;
			player_2_id=player_1_id;
			player_1_id=temp_turn;
		}
		else{
			player_turn=player_1_id;
			temp_turn=player_1_id;
			player_1_id=player_2_id;
			player_1_id=temp_turn;
			
		}
	}
	
	public int getCall(){
		return player_call;
	}
	
	public String getBoard(){
		sboardState = "4";
		sboardState = sboardState.concat(" 255"); // -1 Means server response
		
		for(int q = 0; q < board.length; q++){
			sboardState = sboardState.concat(" " + Integer.toString(board[q]));
		}
		System.out.println(sboardState);
		return sboardState;
	}
	
	//USED BY AI
	
	public int getBoardSize(){
		return board_size;
	}
	
	public int[] returnboard(){
		return board;
	}
	
	public int getHomeTokens(int player){
		int tokens = 0;
		if(player == 0){
			//Player 2
			for(int i = (board.length/2)+1; i < board.length; i++){
				tokens = tokens + board[i];
			}
		}
		else{
			for(int i = 1; i < (board.length/2); i++){
				tokens = tokens + board[i];
			}
		}
		return tokens;
	}
	
	public int getKalahTokens(int player){
		return board[player];
	}
	
}

