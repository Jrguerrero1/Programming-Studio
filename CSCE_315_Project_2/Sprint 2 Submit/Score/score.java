 //Joaquin Guerrero




 /*
add these to the gameboard class

    private static int player1_score;
    private static int player2_score;


 */





 //Main function that is called to make move
    public boolean makeMove(int pos, int player){
    
        //TODO: check if valid
        player_call = player;
        if(pos > board.length ){
            return false;
        }
        
        // was going to use if the pos was at either player kalah
        // if so do not make the move
        // if(pos==0 &&board[board.length/2]){
        //     return false;
        // }

// Score for player when a house is empty and they move 1 token to it
 
        int nextTokens=getTokens(pos+1);// gets the next number of tokens
            
        int tokens = pickUp(pos);// current tokens
        int parallel=getTokens(board.length- (pos+1));// gets the parallel house tokens
        //**********TODO: CHECK FOR NO TOKENS, etc. cases**********
//        if(tokens==0){
//          
//        }
        if(tokens != -1){
            //Could pickup
            for(int i = tokens; i > 0; i--){
                if(tokens > 13){
                    i = i - 14;
                }
                addToken(pos+i);
                if((pos+i)==0 || (pos+i)==(board.length/2))// adds to score
                    score();
            }
       
        if(nextTokens==0 && tokens==1){// next house is empty and current house has 1 token
            //get house across tokens
            cross(parallel+1);// adds the tokens from the parralel side to the players Kalah
        }                     // +1 for the total of the house and the one token moved
            nextPlayer();
            return true;
        }
        else{
            //Failed to pickup
            System.out.println("Invalid Move!");
            return false;
        }
    }




 // Score system...
    // This is the Kalah the ones in the end for each player

    public void cross(int tokens){
        
        if(player_turn== 0)     {
            for(int i=0;i<tokens;i++){
                Gameboard.player1_score ++;
            }
        }                                       //When a player gets all the tokens to their Kalah
        else {
            for(int i=0;i<tokens;i++){
                Gameboard.player1_score ++;
            }
        }
         
}

public void score(){
        if(player_turn== 0) Gameboard.player1_score ++;
            
                                            //When a player gets a token into their Kalah
        else Gameboard.player1_score ++;
       

    }
//Getting an error about returning an int...
 public int getScore(int player){
    	if(player==0)  return Gameboard.player1_score;
    	if(player==0)  return Gameboard.player2_score;
		
    	
    }
