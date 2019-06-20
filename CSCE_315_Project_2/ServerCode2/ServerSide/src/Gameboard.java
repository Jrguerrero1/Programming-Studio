
public class Gameboard {
	private int [] board;
	private int player_turn;
	private int player_call;
	private static int player1_score;
	private static int player2_score;
	String sboardState;
	
	//Constructor
	public Gameboard(){
		//Geneartes default game board
		board = new int[14];
		for(int i = 0; i < board.length;i++){
			if(i == 0 || i == 7){
				board[i] = 0;
			}
			else{
			board[i] = 4;
			}
		}
		
		player_turn = 0; //Starts as player 1s turn
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
			pos = pos - 14;
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
			player_turn = 7;
		}
		else if(player_turn == 7){
			player_turn = 0;
		}
		else{
			player_turn = 0;
		}
	}
	
	//Attempts to pickup, checks for valid move (Players home/tokens in spot)
	public int pickUp(int pos){
		
		if(getTokens(pos) != 0){
			if(player_call == player_turn && player_turn == 0 && pos < 7 && pos > 0){
			int tokens = getTokens(pos);
			setTokens(pos,0);
			return tokens;
		}
			else if(player_call == player_turn && player_turn == 7 && pos < 7 && pos > 0){ 
			int tokens = getTokens(pos + 7);
			setTokens(pos + 7,0);
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
		//TODO: check if valid
		player_call = player;
		if(pos > board.length ){
			return false;
		}
		
		int tokens = pickUp(pos);
		
		if(tokens != -1){
			//Could pickup
			for(int i = tokens; i > 0; i--){
				if(tokens > 13){
					i = i - 14;
				}
				addToken(pos+i);
			}
			//**********TODO: CHECK FOR NO TOKENS, etc. cases**********
			
			nextPlayer();
			return true;
		}
		else{
			//Failed to pickup
			System.out.println("Invalid Move!");
			return false;
		}
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
	
}
