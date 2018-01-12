import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;

public class Scoreboard {
	public Rectangle inGameScore = new Rectangle(0, 800, 800, 20);

	public Rectangle gameOverScore = new Rectangle(250, 450, 150, 35);
	

	/**
	 * Draws the game over screen.
	 * @param g Graphics object
	 */
	public void gameOver(Graphics g) {
	    Board board = new Board();
		int score = board.current_score;
		Graphics2D g2d = (Graphics2D) g;
		Font font1 = new Font("century gothic", Font.BOLD, 50);
		
		String msgH0 = "FINAL SCORE: ";
		
		
		Image background;
		URL url = Snake.class.getResource("/gameOver.png");
		ImageIcon gameOverBG = new ImageIcon(url);
		background = gameOverBG.getImage();
		g.drawImage(background, 0, 0, null);

		
		String msg1 = "PLAY AGAIN";
		String msg2 = "MAIN MENU";
		g.setColor(Color.BLACK);
		g.setFont(font1);
		
		g.drawString(msgH0, gameOverScore.x + 0, gameOverScore.y + 15);
		g.drawString(Integer.toString(score), gameOverScore.x + 0, gameOverScore.y + 60);
		
		
		Rectangle playAgain = new Rectangle(180, 550, 450, 80);
		Rectangle menuButton = new Rectangle(180, 650, 450, 80);
		
		g.setColor(Color.BLACK);
		g.setFont(font1);
		g.drawString(msg1, playAgain.x + 80, playAgain.y + 60);
		g.drawString(msg2, menuButton.x + 80, menuButton.y + 60);
		g.setColor(Color.RED);
		g2d.draw(playAgain);
		g2d.draw(menuButton);
		
	}
	
	public void renderScoreInGame(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Board board = new Board();
		int score = board.current_score;
		Font fontH0 = new Font("century gothic", Font.BOLD, 40);
		String msgH0 = "SCORE: ";
		g.setFont(fontH0);
		
		
		g.drawString(msgH0, inGameScore.x + 7, inGameScore.y + 15);
		g.drawString(Integer.toString(score), inGameScore.x + 180, inGameScore.y + 15);
		
		g.setColor(Color.black);

		//g2d.draw(inGameScore);
		

		
		
	}
	
}
