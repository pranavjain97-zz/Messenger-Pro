
import java.io.*;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class server extends JFrame {

	private JTextField userText;
	private JTextArea chatwindow; // big area. conversation
	private ObjectOutputStream output; // STREAM, my computer--> friend's comp
	private ObjectInputStream input;
	private ServerSocket server; // ports etc
	private Socket connection; // socekt is the connection b/w me and other computer

	// socket-> connection b/w 2 comps

	public server() { // GUI---GUI---GUI---GUI
		super("Pranav's FRIENDS Messenger");

		userText = new JTextField();
		userText.setEditable(false);

		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) { // this method is called after hitting enter
				sendMessage(event.getActionCommand()); // will send the message. will define later
				userText.setText(""); // this line v imp. as soon as u send the message. the typebox needs to become
										// empty, so that we can type in another message.

			}

		});
		add(userText, BorderLayout.NORTH);
		chatwindow = new JTextArea();
		add(new JScrollPane(chatwindow));
		setSize(600, 300);
		setVisible(true);
	}

	// set up and run the server
	public void startRunning() {
		try { // 1st 2nd
			server = new ServerSocket(6789, 100); // 1)port number ( where this app is located on our machine) for admin
													// 2) number of ppl allowed on the server
			while (true) {
				try { // connection with other ppl and have conversations

					waitForConnection(); // waits for somene to connect.
					setupStreams(); // gets connected. connecting streams with others
					whileChatting(); // sending messages back n forth.

				} catch (EOFException eofException) { // end stream
					showMessage("\n Server Timeout");

				} finally {
					closeCrap(); // signal end of streams, close all sockets
				}
			}

		} catch (IOException ioException) {
			ioException.printStackTrace(); // tells us where we made the error
		}

	}

	// wait for connection, then confirm connection.

	private void waitForConnection() throws IOException {
		showMessage("Waiting for someone to connect... \n");
		connection = server.accept(); // accepts the connection to socket. waits till someone is connected( loop keeps
										// running prev)
										// startRunning() keeps running again and again. as soon as it connects,this
										// creates a socket.
		showMessage("Now connected to " + connection.getInetAddress().getHostName());
	}

	// get stream to send and recieve data
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream()); // pathway to send stuff, assigns the output
																		// stream for the socket to 'output'
		output.flush(); // flushes out the crap
		input = new ObjectInputStream(connection.getInputStream()); // pathway to recieve stuff
		// dont have to flush again. the other user will flush from their output code.

		showMessage("\n Streams are now ready.\n");

	}

	// whileChatting

	private void whileChatting() throws IOException {
		String message = " You can chat now!";
		sendMessage(message);
		abletoType(true); // allow users to write text. ( before editabale was set false when we werent
							// connected) //USER
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);

			} catch (ClassNotFoundException classNotFoundException) {
				showMessage("\n idk wtf u r hacked!");
			}

		} while (!message.equals("CLIENT- BYE")); // as soon as any user types 'BYE'. Chat ends. Connection closes.

	}

	private void closeCrap() {

		showMessage("\n Closing connections... \n");
		abletoType(false);
		try {
			output.close();
			input.close(); // closes that stream
			connection.close();

		} catch (IOException ioException) {
			ioException.printStackTrace();

		}

	}

	// send message to client
	private void sendMessage(String message) {
		try {
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER - " + message);
		} catch (IOException ioException) {
			chatwindow.append("\n ERROR: DUDE I CANT SEND THAT MESSAGE");
		}
	}

	// chat window is updated here
	private void showMessage(final String text) {

		SwingUtilities.invokeLater( // to be able to update the GUI
				new Runnable() {
					public void run() {
						chatwindow.append(text);
					}
				});
	}

	// to allow them to type or not
	private void abletoType(final boolean ans) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userText.setEditable(ans); // if its true, you CAN type stuff, else u CANT.
			}

		}

		);

	}

}