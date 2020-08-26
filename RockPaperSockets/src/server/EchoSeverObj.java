package server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author dwatson, mariam, Talha Khamoor
 * @version Feb 07, 2020
 * 
 * 
 * This method is meant to be the server. It creates the server sockets and streams that help 
 * with the two clients communicate. This class also instantiates the server GUI. it also creates a thread to
 * the client handler and runs forever.
 */
public class EchoSeverObj
{
	
	public EchoSeverObj() {
	}
	
	@SuppressWarnings("resource")
	public void initialize()
	{
		// creating the server socket that will accept connections
		ServerSocket server = null;
		Socket socket = null;
		//array list of sockets to see if paris of two are made
		ArrayList <Socket> sockets = new ArrayList<>(2);
		
		//assigning port number to server socket
		try
		{
			server = new ServerSocket(3333);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Server up and running!!!!!!!!!!");
		// creating the server GUI
		ServerGUI window = new ServerGUI();
		window.frame1.setVisible(true);
		
		// while loop runs forever to keep connecting clients that try to connect to it
		while(true)
		{
			try
			{
				socket = server.accept();
				System.out.println("Accepted a client connection.");
				sockets.add(socket);
				if (sockets.size() == 2) {
					System.out.println("paired");
					new Thread(new ClientHandler(sockets.get(0), sockets.get(1))).start();
					sockets.clear();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
