package server;


public class Compac {
	private int op_code = 0;
	private int player_turn = 0;
	private int data = 0;
	
//Constructor - Recv
	public Compac(String input){
		String parse; 
		
		while(input.indexOf(' ') == 0){
			input = input.substring(1);
		}
		
		parse = input.substring(0,input.indexOf(' '));
		op_code = Integer.parseInt(parse);
		
		input = input.substring(input.indexOf(' ')+1);
		parse = input.substring(0,input.indexOf(' '));
		player_turn = Integer.parseInt(parse);
		
		input = input.substring(input.indexOf(' ')+1);
		if(input.indexOf(' ') != -1){
		input = input.substring(0,input.indexOf(' '));
		}
		data = Integer.parseInt(input);
	}
	
//Constructor - Send
	public Compac(){
		
	}
	
	public int getOp(){
		return op_code;
	}
	
	public int getPlayer(){
		return player_turn;
	}
	
	public int getData(){
		return data;
	}
	
	//NOTE: OP CODE NOT WORKING CHECK VALUE IN FIRST IF
	public boolean setOp(int op){
		if(op_code < 10){
		op_code = op;
		return true;
	}
		else{
			return false;
		}
}

	public boolean setPlayer(int player){
		if(player == 0 || player == 7){
			player_turn = player;
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean setData(int dat){
		data = dat;
		return true;
	}

	//Makes string in correct data format
	public String makePacket(){
		String packet;
		
		packet = Integer.toString(op_code);
		packet = packet.concat(" ");
		packet = packet.concat(Integer.toString(player_turn));
		packet = packet.concat(" ");
		packet = packet.concat(Integer.toString(data));
		
		return packet;
	}
	
}
	
	

