package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import utility.InputListener;

/**
 * 
 * @author Talha Khamoor, Mariam
 * @version feb 07, 2020
 * 
 * This method handles two clients that connected to each other. Sends messages to
 * each other depending on which listener is sending it. All objects are also handed off
 * to the server from here 
 */
public class ClientHandler implements Runnable, PropertyChangeListener {

	// attributes
	private Socket socket1;
	private Socket socket2;
	private ObjectOutputStream oos1;
	private ObjectOutputStream oos2;
	private InputListener lis1;
	private InputListener lis2;	

	/**
	 * 
	 * @param socket1
	 * @param socket2
	 * 
	 * This method takes in two sockests from the server class and creates
	 * streams to pass messages forward. We also instantiate our listeners here
	 * to receive messages
	 */
	public ClientHandler(Socket socket1, Socket socket2) {
		this.socket1 = socket1;
		this.socket2 = socket2;
		//creating streams
		try {
			oos1 = new ObjectOutputStream(socket1.getOutputStream());
			oos2 = new ObjectOutputStream(socket2.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//creating listeners
		lis1 = new InputListener(1, this.socket1, this);
		lis2 = new InputListener(2, this.socket2, this);
		//as stated as the object its meant to make sure the client GUI is reset
		try {
			oos1.writeObject("resetEverything");
			oos2.writeObject("resetEverything");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * This is the method that comes with making this class runnable
	 * It creates the threads to the listeners 
	 */
	public void run() {
		new Thread(lis1).start();
		new Thread(lis2).start();
	}

	/**
	 * This method recieves the objects from both clients and forwards them to each other
	 */
	@Override
	public synchronized void propertyChange(PropertyChangeEvent evt) {
		Object object  = evt.getNewValue();
		// sends object to server
		ServerGUI.objectsTransfer(object);
		int number = ((InputListener) evt.getSource()).getNumber();
		
		// based of the source it sends to which ever client the object is meant for
		if (number == 1) {
			
			try {
				oos2.writeObject(object);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				oos1.writeObject(object);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
}
