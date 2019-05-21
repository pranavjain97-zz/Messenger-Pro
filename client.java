//this program will be on my personal computer. my private info. 
//other file was the SERVER available to ALL.

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class client extends JFrame {

	private JTextField userText;
	private JTextArea chatwindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;

	public client(String host) { // IP address of the person's server we want to talk to

		super("Client's App");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());
				userText.setText("");
			}
		});
		add(userText, BorderLayout.NORTH);
		chatwindow = new JTextArea();
		add(new JScrollPane(chatwindow), BorderLayout.CENTER);
		setSize(600, 300);
		setVisible(true);
	}

	// connect to server
	public void startRunning() {

		try {
			connectToServer();
			settingStreams();
			whileChatting();

		} catch (EOFException eofException) {
			showMessage("\n Client went Offline");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			closeShit();
		}

	}

	// connect to server

	private void connectToServer() throws IOException {
		showMessage("Connecting...\n");

		// getting the IP address & port number of the server
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("Now connected to:" + connection.getInetAddress().getHostName());
	}

	// set up streams for exchanging messages
	private void settingStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage(
				"\n Congrats, you have now connected streams with" + connection.getInetAddress().getHostName() + "\n");

	}

	// while chatting
	private void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			}

			catch (ClassNotFoundException classNotfoundException) {
				showMessage("\n Idk the object type");

			}

		} while (!message.equals("SERVER-BYE"));

	}

	private void closeShit() {

		showMessage("\n Closing Connections...");
		ableToType(false);
		try {
			output.close();
			input.close(); // closes that stream
			connection.close();

		} catch (IOException ioException) {
			ioException.printStackTrace();

		}
	}

	public void sendMessage(String message) {

		try {
			output.writeObject("CLIENT- " + message);
			output.flush();
			showMessage("\n CLIENT- " + message);
			// in client's window EG: CLIENT- Hello
		} catch (IOException ioException) {
			chatwindow.append("\n Error ");

		}
	}

	// show text on chatwindow
	private void showMessage(final String msg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatwindow.append(msg);
			}
		}

		);
	}

	// permission to write text
	private void ableToType(final boolean yo) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userText.setEditable(yo);
			}
		}

		);

	}

}
