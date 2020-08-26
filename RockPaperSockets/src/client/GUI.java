package client;

import javax.swing.JFrame;
import utility.InputListener;
import utility.Message;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * 
 * @author Talha Khamoor
 * @version Feb 07, 2020 This huge class deals with creating the client login
 *          gui as well as the gui for the client. Aside from that it is also
 *          responsible for the game logic. The presence of action listeners and
 *          property change help with the functioning of this class.
 */
public class GUI implements PropertyChangeListener {

	// These are all the variables used in this class
	public JFrame frame1;
	public JFrame frame;
	private JTextField textField;
	private JButton sendMessageButton;
	private JButton paperButton;
	private JButton scissorButton;
	private JButton rockButton;
	private JButton replayButton;
	private JLabel playerSideLabel;
	private JLabel awaySideLabel;
	private JLabel resultLabel;
	private JLabel connectionLabel;
	boolean isConnected = true;
	private String text = "";
	private String user = "";
	private String ip = "";
	private String gameChoice;
	private String incomingGameChoice;
	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private ObjectOutputStream oos = null;
	private Socket s1;
	private TextArea textArea = new TextArea();
	private InputListener lis;
	private ActionListener buttonActionListener;
	private boolean attemptConnect = true;
	private boolean socketsClosed = true;
	private JLabel errorMessageLogin;

	/**
	 * @author Talha KHamoor
	 * @version Feb 07, 2019 This is the constructor of this class. It initialized
	 *          the client login
	 */
	public GUI() {

		initializeLogin();

	}

	/*
	 * This method creates a frame for the login, it takes in ip address and user
	 * name and passes those values to the client method. It also does some basic
	 * error checkin of null values.
	 */
	private void initializeLogin() {

		frame1 = new JFrame();
		frame1.setResizable(false);
		frame1.setBounds(new Rectangle(100, 100, 449, 243));
		frame1.getContentPane().setLayout(null);
		// Window listener. If someone clicks the x it closes the GUI
		frame1.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Setting up the gui
		JLabel lblTalhasRockPaper = new JLabel("Talha's Rock Paper Scissor Game");
		lblTalhasRockPaper.setFont(new Font("Harlow Solid Italic", Font.BOLD | Font.ITALIC, 16));
		lblTalhasRockPaper.setHorizontalAlignment(SwingConstants.CENTER);
		lblTalhasRockPaper.setBounds(10, 11, 414, 38);
		frame1.add(lblTalhasRockPaper);

		JTextField ipAddressTF = new JTextField();
		ipAddressTF.setBounds(172, 74, 86, 20);
		frame1.add(ipAddressTF);

		JTextField userNameTF = new JTextField();
		userNameTF.setBounds(172, 105, 86, 20);
		frame1.add(userNameTF);

		JLabel ipAddressLabel = new JLabel("Enter IP Address: ");
		ipAddressLabel.setBounds(71, 77, 280, 14);
		frame1.add(ipAddressLabel);

		JLabel userLabel = new JLabel("Enter a User Name: ");
		userLabel.setBounds(61, 108, 280, 14);
		frame1.add(userLabel);

		errorMessageLogin = new JLabel("Error Message");
		errorMessageLogin.setHorizontalAlignment(SwingConstants.CENTER);
		errorMessageLogin.setBounds(10, 170, 414, 14);
		frame1.add(errorMessageLogin);
		errorMessageLogin.setVisible(false);

		// only button where if pressed it initialized the client GUI
		JButton playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// Checking if values are empty
				if (ipAddressTF.getText().equals("")) {
					errorMessageLogin.setVisible(true);
					errorMessageLogin.setText("please enter a IP Address!");
				} else if (userNameTF.getText().equals("")) {
					errorMessageLogin.setVisible(true);
					errorMessageLogin.setText("Please enter a user name!");
				} else {
					ip = ipAddressTF.getText();
					user = userNameTF.getText();
					initialize();
					frame.setVisible(true);
					frame1.setVisible(false);
				}
			}
		});
		playButton.setBounds(172, 136, 89, 23);
		frame1.add(playButton);

	}

	/**
	 * @author Talha Khamoor
	 * @version Feb 07, 2019 This method is responsible for creating the gui for
	 *          each client that joins. It initially attempts to send an object to
	 *          the client handler, however if no connection is made it kind of just
	 *          ignores that line. However the second client that hits that line
	 *          actually is able to hit that line. Before any of that, it also does
	 *          create a socket, object output steam, etc. This method also has a
	 *          bunch of buttons text fields etc that are connected to action
	 *          listeners
	 */
	private void initialize() {

		// setting up the frame
		buttonActionListener = new ButtonActionListener();
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(new Rectangle(450, 200, 680, 495));
		frame.getContentPane().setLayout(null);
		// window listener to see if user is trying to exit out
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// if the sockets are closed it creates new socket to close
				// in order to avoid errors
				// if value holds true it will actually be able to run the oos line
				if (socketsClosed) {
					try {
						oos.writeObject(user + "," + "playerDisconnecting");
						oos.close();
						s1.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					try {
						s1 = new Socket(ip, 3333);
						OutputStream os = s1.getOutputStream();
						oos = new ObjectOutputStream(os);
						// new Thread(lis).start();
						oos.close();
						s1.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

				System.exit(0);
			}
		});

		// Creating the GUI
		textArea.setEditable(false);
		textArea.setBounds(10, 271, 463, 153);
		frame.getContentPane().add(textArea);

		textField = new JTextField();
		textField.setBounds(10, 432, 364, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		sendMessageButton = new JButton("Send");
		sendMessageButton.addActionListener(buttonActionListener);
		sendMessageButton.setBounds(384, 431, 89, 23);
		frame.getContentPane().add(sendMessageButton);
		sendMessageButton.setEnabled(false);

		paperButton = new JButton();

		paperButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("paper.png")));
		paperButton.addActionListener(buttonActionListener);
		paperButton.setBounds(479, 152, 184, 130);
		frame.getContentPane().add(paperButton);
		paperButton.setEnabled(false);

		scissorButton = new JButton();
		scissorButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("scissor.png")));
		scissorButton.addActionListener(buttonActionListener);
		scissorButton.setBounds(479, 294, 184, 130);
		frame.getContentPane().add(scissorButton);
		scissorButton.setEnabled(false);

		rockButton = new JButton();
		rockButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("rock.png")));
		rockButton.addActionListener(buttonActionListener);
		rockButton.setBounds(479, 11, 184, 130);
		frame.getContentPane().add(rockButton);
		rockButton.setEnabled(false);

		playerSideLabel = new JLabel();
		playerSideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("question.gif")));
		playerSideLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerSideLabel.setBounds(67, 41, 106, 106);
		frame.getContentPane().add(playerSideLabel);

		awaySideLabel = new JLabel();
		awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("question.gif")));
		awaySideLabel.setHorizontalAlignment(SwingConstants.CENTER);
		awaySideLabel.setBounds(315, 41, 106, 106);
		frame.getContentPane().add(awaySideLabel);

		resultLabel = new JLabel();
		resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("vs.gif")));
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resultLabel.setBounds(176, 152, 137, 81);
		frame.getContentPane().add(resultLabel);

		replayButton = new JButton("Replay");
		replayButton.addActionListener(buttonActionListener);
		replayButton.setBounds(479, 431, 184, 23);
		frame.getContentPane().add(replayButton);
		replayButton.setEnabled(false);

		connectionLabel = new JLabel();
		connectionLabel.setBounds(10, 252, 163, 14);
		frame.getContentPane().add(connectionLabel);

		// Creates the socket and streams.. If the input entered was incorrect in
		// previous gui, it will throw the exception
		try {

			s1 = new Socket(ip, 3333);
			OutputStream os = s1.getOutputStream();
			oos = new ObjectOutputStream(os);
			lis = new InputListener(0, s1, this);
			new Thread(lis).start();
		} catch (UnknownHostException e) {
			errorMessageLogin.setVisible(true);
			errorMessageLogin.setText("Failed to connect to server, please try again!");
			e.printStackTrace();
		} catch (IOException e) {
			errorMessageLogin.setVisible(true);
			errorMessageLogin.setText("Failed to connect to server, please try again!");
			e.printStackTrace();
		}
		try {
			oos.writeObject("attemptConnect");
			connectionLabel.setText("Waiting for an opponent!");

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}

	}

	/**
	 * 
	 * @author Talha Khamoor
	 * @version Feb 07, 2019
	 * 
	 *          This method is called whenever a button is pressed in the client
	 *          GUI. Depending on what the user clicks it takes the appropriate
	 *          action. Whether that be to set text and images in its own client or
	 *          object output those changes in the other client or server.
	 */
	private class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Action listeners which perform their action based of which one is clicked
			if (e.getSource() == sendMessageButton) {
				text = textField.getText();
				textField.setText("");
				Message msg = new Message(user, text, new Date());
				Date sendingDate = msg.getTimeStamp();

				textArea.append(dateFormat.format(sendingDate) + "  " + user + ": " + text + "\n");
				try {
					oos.writeObject(msg);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (e.getSource() == paperButton) {
				paperButton.setEnabled(false);
				scissorButton.setEnabled(false);
				rockButton.setEnabled(false);
				gameChoice = "paper797138";
				playerSideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("paper.png")));
				connectionLabel.setText("Waiting for opponents move!");
				try {

					oos.writeObject(gameChoice + "," + user);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (e.getSource() == scissorButton) {
				paperButton.setEnabled(false);
				scissorButton.setEnabled(false);
				rockButton.setEnabled(false);
				gameChoice = "scissor797138";
				playerSideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("scissor.png")));
				connectionLabel.setText("Waiting for opponents move!");
				try {
					oos.writeObject(gameChoice + "," + user);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (e.getSource() == rockButton) {
				paperButton.setEnabled(false);
				scissorButton.setEnabled(false);
				rockButton.setEnabled(false);
				gameChoice = "rock797138";
				playerSideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("rock.png")));
				connectionLabel.setText("Waiting for opponents move!");
				try {
					oos.writeObject(gameChoice + "," + user);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} else if (e.getSource() == replayButton) {
				playerSideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("question.gif")));
				awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("question.gif")));
				resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("vs.gif")));

				incomingGameChoice = null;
				gameChoice = null;
				try {
					oos.writeObject(user + "," + "replay797138");

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * @author Talha Khamoor
	 * @version Feb 07, 2019
	 * @param propertChange evt
	 * @return void
	 * 
	 *         This method receives the changes the other client sends forth. Based
	 *         on the parameter value it either sends message back through the
	 *         stream or implements changes in its own client. Like changing
	 *         pictures or displaying text.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object object = evt.getNewValue();
		// if else tree to see what the value of the incoming object is
		// If it doesn't match any of these it is assumed that is a message.
		// i added my student number to some of the variables inorder to avoid some
		// error and making my program more secure
		if (object.toString().contains("paper797138") || object.toString().contains("rock797138")
				|| object.toString().contains("scissor797138")) {
			connectionLabel.setText("Opponent is waiting on you!");
			incomingGameChoice = (String) object;
			GameDecision(incomingGameChoice);
		} else if (object.equals("setPaperImg")) {
			awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("paper.png")));
		} else if (object.equals("setRockImg")) {
			awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("rock.png")));
		} else if (object.equals("setScissorImg")) {
			awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("scissor.png")));
		} else if (object.toString().contains("opponentWon797138")) {
			resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("winner.png")));
			try {
				oos.writeObject(user + "," + "responseIWin797138");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (object.toString().contains("opponentLost797138")) {
			resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("loser.png")));
			try {
				oos.writeObject(user + "," + "reponseILost797138");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (object.toString().contains("tieGame797138")) {
			resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("draw.png")));

			try {
				oos.writeObject(user + "," + "reciprocateTie797138");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (object.toString().contains("replay797138")) {
			// pop up message to see if player wants to replay
			int yes_no = JOptionPane.showConfirmDialog(frame,
					"Your opponent wants a rematch, would you like to play again?", "Rematch Request!",
					JOptionPane.YES_NO_OPTION);
			incomingGameChoice = null;
			gameChoice = null;
			// if the user clicks yes it resets everything
			if (yes_no == JOptionPane.YES_OPTION) {
				playerSideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("question.gif")));
				awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("question.gif")));
				resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("vs.gif")));

				paperButton.setEnabled(true);
				scissorButton.setEnabled(true);
				rockButton.setEnabled(true);
				try {
					oos.writeObject(user + "," + "resetButtons797138");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// else it closes everything and exits out
			} else {
				try {
					oos.writeObject(user + "," + "playerDisconnecting");
					oos.close();
					s1.close();
					System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (object.toString().contains("resetButtons797138")) {
			paperButton.setEnabled(true);
			scissorButton.setEnabled(true);
			rockButton.setEnabled(true);
			gameChoice = null;
			incomingGameChoice = null;
		} else if (object.toString().contains("playerDisconnecting")) {
			try {
				// prompts the staying user if they also want to stay or quit the game
				int yes_no = JOptionPane.showConfirmDialog(frame,
						"Your opponent has left, Would you like to que up for another game?", "Opponent Left!",
						JOptionPane.YES_NO_OPTION);
				// if they answer yes to the popup it still closes the sockets and streams but
				// right away creates another one
				if (yes_no == JOptionPane.YES_OPTION) {
					oos.writeObject(user + "," + "playerDisconnecting");
					socketsClosed = false;
					oos.close();
					s1.close();
					isConnected = true;
					connectionLabel.setText("waiting for opponent!");

					s1 = new Socket(ip, 3333);
					OutputStream os = s1.getOutputStream();
					oos = new ObjectOutputStream(os);
					lis = new InputListener(0, s1, this);
					new Thread(lis).start();

					attemptConnect = true;
					gameChoice = null;
					incomingGameChoice = null;
					playerSideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("question.gif")));
					awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("question.gif")));
					resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("vs.gif")));
				} else {
					// else closes the streams and sockets and closes GUI
					oos.writeObject(user + "," + "playerDisconnecting");
					oos.close();
					s1.close();
					System.exit(0);

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (object.equals("attemptConnect")) {
			// if an attempt to connect reaches here is sets all the below variables to set
			// up for new game
			connectionLabel.setText("Connected to an opponent!");
			rockButton.setEnabled(true);
			paperButton.setEnabled(true);
			scissorButton.setEnabled(true);
			sendMessageButton.setEnabled(true);
			replayButton.setEnabled(true);
			socketsClosed = true;
			// if the flag is true is sends a message to dedicated to server as well makes
			// sure the connection is
			// reciprocated to other GUI. while loop is here to prevent infinite loop
			// between the two clients
			while (attemptConnect) {
				attemptConnect = false;
				try {
					oos.writeObject(user + "," + " has connected to the server!");
					oos.writeObject("attemptConnect");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else if (object.equals("clearConnectionLabel")) {
			connectionLabel.setText("");
		} else if (object.toString().contains("reciprocateAttemptConnect")) {
			// left empty because this stream meant for server
		} else if (object.toString().contains("has connected to the server!")) {
			// left empty because this stream meant for server
		} else if (object.toString().contains("reponseILost797138") || object.toString().contains("responseIWin797138")
				|| object.toString().contains("reciprocateTie797138")) {
			// left empty because this stream meant for server
		} else if (object.toString().equals("resetEverything")) {
			// this object is sent from client handler to make sure everything is reset when
			// two clients are connected
			connectionLabel.setText("Connected to an opponent!");
			rockButton.setEnabled(true);
			paperButton.setEnabled(true);
			scissorButton.setEnabled(true);
			sendMessageButton.setEnabled(true);
			replayButton.setEnabled(true);
			gameChoice = null;
			incomingGameChoice = null;
			socketsClosed = true;

		} else {
			// if object does not hit any if, it means that it is a message. So the message
			// is printed here
			String incomingMsg = ((Message) object).getMsg();
			String incomingUser = ((Message) object).getUser();
			Date incomingTime = ((Message) object).getTimeStamp();
			textArea.append(dateFormat.format(incomingTime) + "  " + incomingUser + ": " + incomingMsg + "\n");
		}

	}

	/**
	 * @author Talha Khamoor
	 * @version Feb 07, 2019
	 * @return void
	 * @param incomingGameChoice
	 * 
	 *                           This method is the game logic area. It takes in the
	 *                           input and output from both clients and decides the
	 *                           result of the game. Based off that it sets the
	 *                           change in its own client to present the results,
	 *                           and sends a stream to the other client of the
	 *                           results. Only when both players have made a move
	 *                           does this method truly execute.
	 */
	private void GameDecision(String incomingGameChoice) {
		// the following if statement to make sure the winner is decided when both
		// players submit their choice
		if (incomingGameChoice != null && gameChoice != null) {
			connectionLabel.setText("");
			try {
				oos.writeObject("clearConnectionLabel");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String gameChoiceArray[] = gameChoice.split(",");
			String incomingGameChoiceArray[] = incomingGameChoice.split(",");

			// this here is the heart of the game logic. It decides the by checking what the
			// opponent chose and client chose
			if (gameChoiceArray[0].equals(incomingGameChoiceArray[0])) {
				if (gameChoiceArray[0].equals("rock797138")) {
					try {
						oos.writeObject("setRockImg");
						oos.writeObject(user + "," + "tieGame797138");
						awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("rock.png")));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (gameChoiceArray[0].equals("paper797138")) {
					try {
						oos.writeObject("setPaperImg");
						oos.writeObject(user + "," + "tieGame797138");
						awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("paper.png")));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (gameChoiceArray[0].equals("scissor797138")) {
					try {
						oos.writeObject("setScissorImg");
						oos.writeObject(user + "," + "tieGame797138");
						awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("scissor.png")));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("draw.png")));
			} else if (gameChoiceArray[0].equals("rock797138") && incomingGameChoiceArray[0].equals("paper797138")) {
				awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("paper.png")));
				try {
					oos.writeObject("setRockImg");
					oos.writeObject(user + "," + "opponentWon797138");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("loser.png")));
			} else if (gameChoiceArray[0].equals("rock797138") && incomingGameChoiceArray[0].equals("scissor797138")) {
				awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("scissor.png")));
				try {
					oos.writeObject("setRockImg");
					oos.writeObject(user + "," + "opponentLost797138");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("winner.png")));
			} else if (gameChoiceArray[0].equals("paper797138") && incomingGameChoiceArray[0].equals("rock797138")) {
				awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("rock.png")));
				try {
					oos.writeObject("setPaperImg");
					oos.writeObject(user + "," + "opponentLost797138");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("winner.png")));
			} else if (gameChoiceArray[0].equals("paper797138") && incomingGameChoiceArray[0].equals("scissor797138")) {
				awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("scissor.png")));
				try {
					oos.writeObject("setPaperImg");
					oos.writeObject(user + "," + "opponentWon797138");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("loser.png")));
			} else if (gameChoiceArray[0].equals("scissor797138") && incomingGameChoiceArray[0].equals("rock797138")) {
				awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("rock.png")));
				try {
					oos.writeObject("setScissorImg");
					oos.writeObject(user + "," + "opponentWon797138");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("loser.png")));
			} else if (gameChoiceArray[0].equals("scissor797138") && incomingGameChoiceArray[0].equals("paper797138")) {
				awaySideLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("paper.png")));
				try {
					oos.writeObject("setScissorImg");
					oos.writeObject(user + "," + "opponentLost797138");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resultLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("winner.png")));
			}

		}

	}
}
