package server;

import javax.swing.JFrame;
import javax.swing.JPanel;
import utility.Message;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * @author Talha Khamoor
 * @version Feb 07, 2020
 * 
 * This class created the Server GUI It receives objects only and prints them to the 
 * text area accordingly
 *
 */
public class ServerGUI {

	//variables
	public JFrame frame1;
	public static JPanel contentPane = new JPanel();
	public static TextArea serverTextArea = new TextArea();
	public static String userString = "";
	public static ServerGUI frame;
	public static Object user = null;
	public static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	/**
	 * constructor for server GUI
	 */
	public ServerGUI() {
		//calls initialize method to create GUI
		initiallize();
	}

	/**
	 * This method actually creates the frame and add the components to the GUI
	 */
	private void initiallize() {
		frame1 = new JFrame();
		frame1.setResizable(false);
		frame1.setBounds(100, 100, 391, 526);
		frame1.getContentPane().setLayout(null);
		// closes the GUI upon hitting the x button
		frame1.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		serverTextArea.setEditable(false);
		serverTextArea.setBounds(10, 58, 365, 379);
		frame1.getContentPane().add(serverTextArea);

		JLabel serverLabel = new JLabel("Server");
		serverLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 15));
		serverLabel.setHorizontalAlignment(SwingConstants.CENTER);
		serverLabel.setBounds(137, 11, 104, 36);
		frame1.getContentPane().add(serverLabel);
		serverTextArea.append("Server up and running!" + "\n");
	}
	
	/**
	 * 
	 * @param object
	 * 
	 * This method recieves objects from the client handler. Which receives objects from
	 * the clients. The clients are strings separated by commas, so we split them to get the message
	 * and also who it was sent by.
	 * 
	 */
	public static void objectsTransfer(Object object) {
		// gets current time to be able to stamp time to texts
		Date timeStamp = new Date();
		// if else tree to see what the object equates to and based off that it prints a message
		if (object.toString().contains("has connected to the server!")) {
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[0] + " has connected to the server!" + "\n");
		}else if (object.toString().contains("paper797138")){
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[1] + ": has chosen paper \n");
		}else if (object.toString().contains("rock797138")){
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[1] + ": has chosen rock \n");
		}else if (object.toString().contains("scissor797138")){
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[1] + ": has chosen scissor \n");
		}else if (object.toString().contains("tieGame797138")) {
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[0] + "'s game ended in a tie" + "\n");
		}else if (object.toString().contains("reciprocateTie797138")) {
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[0] + "'s game ended in a tie" + "\n");
		}else if (object.toString().contains("opponentWon797138")) {
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[0] + ": has lost the game!" + "\n");
		}else if (object.toString().contains("opponentLost797138")) {
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[0] + ": has won the game!" + "\n");
		}else if (object.toString().contains("responseIWin797138")) {
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[0] + ": has won the game!" + "\n");
		}else if (object.toString().contains("reponseILost797138")) {
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[0] + ": has lost the game!" + "\n");
		}else if (object.toString().contains("playerDisconnecting")) {
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[0] + " has disconnected from server! \n");
		}else if (object.toString().contains("replay797138")) {
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[0] + ": requested to play again" + "\n");
		}else if (object.toString().contains("resetButtons797138")) {
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String stringArray[] = object.toString().split(",");
			serverTextArea.append(stringArray[0] + ": accepted replay request" + "\n");
		}else if (object.toString().contains("attemptConnect") ||
				object.toString().contains("clearConnectionLabel")||
				object.toString().contains("setRockImg")||
				object.toString().contains("setPaperImg")||
				object.toString().contains("setScissorImg")){
			
		}else {
			// if none of the objects equal to anything it is assumed that its a message so it reaches here
			serverTextArea.append(dateFormat.format(timeStamp) + "  ");
			String incomingMessage = ((Message) object).getMsg();
			String incomingUser = ((Message) object).getUser();
			serverTextArea.append(incomingUser + " : '" + incomingMessage + "' \n");
		}
		
		
			
		
		
	}

}
