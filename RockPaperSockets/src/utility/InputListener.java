package utility;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Talha Khamoor, Mariam
 * @version Feb 07/2020
 * 
 * This class recieves inputs from both clients. Though in separate instances. It then uses the
 * notify observers method to notify the client handler
 */
public class InputListener implements Runnable {

	//Variables 
	private Socket socket;
	private ObjectInputStream ois;
	@SuppressWarnings("unused")
	private PropertyChangeListener observer;
	private int number;
	private List<PropertyChangeListener> observers = new ArrayList<>();

	/**
	 * Constructor for the input listener. Sets the sockets, number to identify, and observer 
	 * @param number
	 * @param socket
	 * @param observer
	 */
	public InputListener(int number, Socket socket, PropertyChangeListener observer) {
		this.observer = observer;
		this.number = number;
		this.socket = socket;
		//adding observer to list to go through later. It is needed even though we have one observer
		observers.add(observer);
	}

	/**
	 * this method gets number inorder to figure out which client to send message to.
	 * 
	 * @return int number
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * run method as required by runnable thread
	 * This gets incoming objects and sends them to observer method
	 */
	@Override
	public void run() {

		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Seems like someone clicked the quit button, whilst the game was still going."
					+ " No worries, please continue on");
		}
		boolean connected = true;
		while (connected) {
			Object object;
			try {
				object = ois.readObject();
				notifyObservers(object);

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				connected = false;
				
			} 
			
		}

	}
	
	/**
	 * This method notifies the observer which is the client handler of the objects incoming.
	 * @param object
	 */
	private void notifyObservers(Object object) {
		
		//loops through the one observer we have
		for( PropertyChangeListener clientHandler : observers) {
			clientHandler.propertyChange(new PropertyChangeEvent(this, null, null, object));
		}
		
	}

}
