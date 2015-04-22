import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class ClientTest {
	public static void main(String[] args) {
		String clientIP = JOptionPane.showInputDialog(null, "Enter Server IP Address. \n(Default Local IP Already Inputted Below)", "127.0.0.1");

			Client client = new Client(clientIP);
			client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			client.run();
	}
}
