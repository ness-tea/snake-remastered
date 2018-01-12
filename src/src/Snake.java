/**
 * Serves as the main method holder for the game Snake.
 * 
 * Written with heavy help from http://zetcode.com/tutorials/javagamestutorial/snake/
 *
 * @author Harrison Lau
 */
import java.awt.EventQueue;
import javax.swing.JFrame;

public class Snake extends JFrame {
	/**
	 * Constructor for Snake. Sets up board, title of window, etc.
	 */
	public Snake() {
		
		add(new Board());
		

		setResizable(false);
		pack();
		
		setTitle("Snake 2.0 (Remastered)");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Main method to run the game.
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame ex = new Snake();
				ex.setVisible(true);
			}
		});
	}
}
