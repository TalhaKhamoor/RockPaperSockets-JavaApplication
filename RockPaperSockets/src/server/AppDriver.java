package server;

/**
 * 
 * @author Talha Khamoor
 * 
 * This class contains the main method for the server side application. It initializes the gui,
 *
 */
public class AppDriver {

	public static void main(String[] args) {
		EchoSeverObj server = new EchoSeverObj();
		server.initialize();
	}

}
