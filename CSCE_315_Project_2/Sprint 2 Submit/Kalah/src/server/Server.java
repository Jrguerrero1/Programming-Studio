package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.*;
import java.net.*;


public class Server {
	
	
	
	public static void main(String[] args)throws IOException {
        ServerSocket listener = new ServerSocket(23456);
		System.out.println("Server Running...");
		Gameboard gb = new Gameboard();
		
	
		
      //  try {  	
            while (true) {
            	Socket socket = listener.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream outtoClient = new DataOutputStream(socket.getOutputStream());
	        	String request = input.readLine();
                
                Compac com = new Compac(request);
                
                switch(com.getOp()){
                
                case 0:
                	//Menu
                	break;
                	
                case 1:
                	//Move
                	if(gb.makeMove(com.getData(),com.getPlayer())){
                		String reply = "1 255 1"; //ACK
                		outtoClient.writeBytes(reply + '\n');
                	}
                	else{
                		String reply = "1 255 0"; //Failed
                		outtoClient.writeBytes(reply + '\n');
                	}
                	break;
                	
                case 2:
                	if(com.getPlayer() == gb.getTurn()){
                		String reply ="2 255 1";
                		outtoClient.writeBytes(reply + '\n');
                	}
                	else{
                		String reply = "2 255 0";
                		outtoClient.writeBytes(reply + '\n');
                	}
                	break;
                	
                case 3:
                	//Score
                	break;
                	
                case 4:
                	//State
                	outtoClient.writeBytes(gb.getBoard() + '\n'); //Returns as op player data data ....
                	break;
                	
                case 5:
                	//Config
                	break;
                	
                case 6:
                	//New Game
                	gb = new Gameboard();
                	break;
                	
                case 7:
                	//Exit
                	listener.close();
            		socket.close();
            		System.exit(0);
                	break;
                }
                	//END OF SWTICH
                 
                }
        //	}   
		}
	
}
   
	








