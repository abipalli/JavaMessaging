import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** 
 * @author Abinesh Sarvepalli
 */

public class Server extends JFrame {
	private boolean end = false;
	
	private JTextField txtFldMssg;
	private JTextArea txtAreaConvo;
	
	private ObjectOutputStream outp;
	private ObjectInputStream inp;
	private ServerSocket server;	
	private Socket connection;
	
	
	//constructor
	Server() {
		super("SERVER");
		
		txtFldMssg = new JTextField();
			txtFldMssg.setEditable(false); //cant type without connection			
			txtFldMssg.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evnt) {
					sendMessage(evnt.getActionCommand());
					txtFldMssg.setText("");
				}
			});
		add(txtFldMssg, BorderLayout.SOUTH);
		
		txtAreaConvo = new JTextArea();
			add(new JScrollPane(txtAreaConvo), BorderLayout.CENTER);
			
		setSize(450, 450);
		setVisible(true);
		
	}


	private void sendMessage(String actionCommand) {
		// TODO Auto-generated method stub
		try {
			outp.writeObject(actionCommand);
			outp.flush();
			txtAreaConvo.append("\n@YOU: " + actionCommand);
		} catch(IOException ioe) {
			txtAreaConvo.append("ERROR: UNABLE TO SEND MESSAGE");
		}
	}
	private void showMessage(String message) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						txtAreaConvo.append("\n" + message);
					}
				}
		);
	}
	
	
	public void run() {
		try {
			server = new ServerSocket(6789, 100);
			
			while(!end) {
				try {
					//connect and have conversation
					waitForConnection();
					setupStreams();
					whileChatting();
				} catch(EOFException eofe) {
					showMessage("\nDisconnected From Server");
				} finally {
					close();
				}
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}


	private void close() {
		// TODO Auto-generated method stub
		ableToType(false);
		try {
			outp.close();
			inp.close();
			connection.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}


	private void ableToType(boolean b) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				txtFldMssg.setEditable(b);
			}
		});
	}


	private void whileChatting() throws IOException {
		// TODO Auto-generated method stub
		String message = "\nYou are now connected!";
		showMessage(message);
		ableToType(true);
		do {
			try {
				message = (String) inp.readObject();
				showMessage("@USER: " + message);
			} catch(ClassNotFoundException cnfe) {
				showMessage("Unable to Display Message");
			}
		} while(!message.equals("END"));
		end = true;
	}


	private void setupStreams() throws IOException {
		// TODO Auto-generated method stub
		outp = new ObjectOutputStream(connection.getOutputStream());
		outp.flush();
		inp = new ObjectInputStream(connection.getInputStream());
	}


	private void waitForConnection() throws IOException {
		// TODO Auto-generated method stub
		showMessage("Waiting for Connection...");
		connection = server.accept();
		showMessage("Connection Established: " + connection.getInetAddress().getHostName());
	}
}
