//Developer: Travis Stewart

//Root package
package client;

//Package imports
import server.Server;
import gui.ClientGUI;
import gui.MenuWindow;


//General imports
import java.util.*;

import java.awt.*;
import java.awt.image.*;


import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.JPanel;

import java.io.*;
import java.net.*;


public class Main {

	public static void main(String[] args) throws Exception {
		//Client c = new Client();
		ClientGUI cg = new ClientGUI();
		//System.out.printf("      %s\n" ,"Welcome to Kalah(6,4)");
		
		cg.menu();
		

	}

}
