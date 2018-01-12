import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;

public class Menu {

	public Rectangle playButton = new Rectangle(180, 400, 450, 80);
	public Rectangle helpButton = new Rectangle(180, 500, 450, 80);
	public Rectangle settingsButton = new Rectangle(180, 600, 450, 80);
	
	public Rectangle playInHelp = new Rectangle(490, 680, 230, 80);
	public Rectangle backHelp = new Rectangle(80, 680, 200, 80);
	
	public Rectangle smallButton = new Rectangle(180, 300, 450, 80);
	public Rectangle mediumButton = new Rectangle(180, 400, 450, 80);
	public Rectangle largeButton = new Rectangle(180, 500, 450, 80);
	
	public Rectangle playInSettings = new Rectangle(490, 680, 230, 80);
	public Rectangle backSettings = new Rectangle(80, 680, 200, 80);
	

	URL url = Snake.class.getResource("/gameMenu.png");
	Image menuBG = Toolkit.getDefaultToolkit().createImage(url);
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g.drawImage(menuBG, 0, 0, null);
		
		Font font0 = new Font("century gothic", Font.CENTER_BASELINE, 100);
		Font font1 = new Font("century gothic", Font.BOLD, 100);
		Font font2 = new Font("century gothic", Font.BOLD, 50);
		
		g.setFont(font0);
		g.setColor(Color.BLACK);
		g.drawString("S N A K E  ", 100, 200);
		
		g.setFont(font1);
		g.setColor(Color.RED);
		g.drawString("2.0", 570, 200);
		
		g.setFont(font2);
		g.setColor(Color.black);
		g.drawString("PLAY", playButton.x + 170 , playButton.y + 60);
		g.setColor(Color.RED);
		g2d.draw(playButton);
		
		g.setColor(Color.black);
		g.drawString("HELP", helpButton.x + 170 , helpButton.y + 60);
		g.setColor(Color.RED);
		g2d.draw(helpButton);
		
		g.setColor(Color.black);
		g.drawString("SETTINGS", settingsButton.x + 120 , settingsButton.y + 60);
		g.setColor(Color.RED);
		g2d.draw(settingsButton);
	}
	
	public void renderHelp(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		Font fontH0 = new Font("century gothic", Font.BOLD, 40);
		String msgH0 = "PLAY NOW";
		String msgH1 = "BACK";
		
		Image background1;
		url = Snake.class.getResource("/howToPlay.png");
		ImageIcon helpBG = new ImageIcon(url);
		background1 = helpBG.getImage();
		g.drawImage(background1, 0, 0, null);
		
		g.setColor(Color.BLACK);
		g.setFont(fontH0);
		
		g.drawString(msgH1, backHelp.x + 50, backHelp.y + 55);
		g.drawString(msgH0, playInHelp.x + 12, playInHelp.y + 55);
		
		g.setColor(Color.red);
		g2d.draw(playInHelp);
		g2d.draw(backHelp);
		
	}
	
	public void renderSettings(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		Font fontH0 = new Font("century gothic", Font.BOLD, 40);
		String msgH0 = "PLAY NOW";
		String msgH1 = "BACK";
		String msgH2 = "SMALL";
		String msgH3 = "MEDIUM";
		String msgH4 = "LARGE";
		
		Image background1;
		url = Snake.class.getResource("/Settings.png");
		ImageIcon helpBG = new ImageIcon(url);
		background1 = helpBG.getImage();
		g.drawImage(background1, 0, 0, null);
		
		g.setColor(Color.BLACK);
		g.setFont(fontH0);
		
		g.drawString(msgH2, smallButton.x + 36, smallButton.y + 55);
		g.drawString(msgH3, mediumButton.x + 30, mediumButton.y + 55);
		g.drawString(msgH4, largeButton.x + 36, largeButton.y + 55);
		g.drawString(msgH1, backSettings.x + 10, backSettings.y + 54);
		g.drawString(msgH0, playInSettings.x + 7, playInSettings.y + 54);
		
		g.setColor(Color.red);
		g2d.draw(playInSettings);
		g2d.draw(backSettings);
		g2d.draw(smallButton);
		g2d.draw(mediumButton);
		g2d.draw(largeButton);
	}
}
