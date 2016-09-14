import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TestClient {

	public static void main(String args[]) {
		String fn=JOptionPane.showInputDialog("Enter your IP Address");
		client o1;
		o1=new client(fn); //here we pass the SERVER'S IP
		o1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		o1.startRunning();
		
		
	}
	
}
