/**
 * This class represents the game's board as (x, y) coordinates. The snake
 * occupies a subset of the board while pellets (either regular or power-up)
 * spawn randomly on the board.
 * 
 * Many variables and methods were changed to public for the benefit of the MIS.
 * 
 * Vanilla implementation written with heavy help from http://zetcode.com/tutorials/javagamestutorial/snake/
 * 
 * @file Board
 * @author Harrison Lau
 * */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {
	
	public static final int INITIAL_BOARD_WIDTH = 800; /**< Board width */
	public static final int INITIAL_BOARD_HEIGHT = 820; /**< Board height */
	public final int DOT_SIZE = 20; /**< Size of each dot or 'pixel' */
	public final int ALL_DOTS = 900; /**< Max possible length of snake */
	public final int RAND_POS = 38; /**< Used to calculate next pellet position */
	public final int DELAY = 140; /**< Determines speed of game */
	
	public final int x[] = new int[ALL_DOTS]; /**< x-coordinates of board */
	public final int y[] = new int[ALL_DOTS]; /**< y-coordinates of board */
	
	public int dots; /**< Number of dots occupied by snake*/
	public int pellet_x; /**< x-coordinate of pellet */
	public int pellet_y; /**< y-coordinate of pellet */
	public int current_board_width = INITIAL_BOARD_WIDTH;
	public int current_board_height = INITIAL_BOARD_HEIGHT;
	
	
	public int score_size = 20;
	public static int current_score = 100;
	public int score_decrease_counter = 0;
	public int decrease_rate = 5;
	
	public Random rand = new Random(); /**< Used for calculating random power-up */
	public boolean speedupPellet = false; /**< Determines if current pellet is speed up power-up */
	public boolean slowdownPellet = false; /**< Determines if current pellet is slow down power-up */
	public boolean collisionPellet = false; /**< Determines if current pellet is collision immunity power-up */
	public boolean collisionActive = false; /**< If collision immunity power-up is in effect */

	public long stepCount = 0; /**< Used to determine duration of speed up and slow down power-up */
	public long immunityCount = 0; /**< Used to count duration of collision immunity power-up */
	
	public boolean leftDirection = false; /**< Moves snake to the left */
	public boolean rightDirection = true; /**< Moves snake to the right */
	public boolean upDirection = false; /**< Moves snake upwards */
	public boolean downDirection = false; /**< Moves snake downwards */
	
	public boolean inGame = false; /**< If game is in process */
	public boolean inMenu = true; /**< If menu is in process */
	public boolean inHelp = false; /** If help page is in process */
	public boolean inSettings = false; /** If settings page is in process */
	public boolean gameOver = false; /**< If game over */
	private Menu menu;
	private Scoreboard scoreboard;
	
	public Timer timer; /**< Timer (java swing) object */
	public Image ball; /**< Image icon for snake body */
	public Image pellet; /**< Image icon for normal pellet */
	public Image head; /**< Image icon for snake head */
	public Image speedup; /**< Image icon for speed up power-up pellet */
	public Image slowdown; /**< Image icon for slow down power-up pellet */
	public Image collision; /**< Image icon for collision immunity power-up pellet */
	
	/**
	 * Constructor for Board.
	 * 
	 * out := self
	 * 
	 */
	public Board() { 
		menu = new Menu();
		scoreboard = new Scoreboard();
		
		addKeyListener(new TAdapter());
		addMouseListener(new MouseInput());
		setBackground(Color.white);
		setFocusable(true);
		
		setPreferredSize(new Dimension(current_board_width, current_board_height));
		loadImages();
	
		initGame();
	}
	
	/**
	 * Sets the delay of the game, effectively controlling how quickly the game progresses.
	 * 
	 * transition := timer.setDelay(delay);
	 * 
	 * @param delay Delay of the game. The higher the number, the slower the game.
	 */
	public void setDelay(int delay) {
		timer.setDelay(delay);
	}
	
	/**
	 * Loads up the images for each significant component (body, head and pellets (power-up or otherwise))
	 * 
	 * transition := forall t : Image : t = t's corresponding image
	 * 
	 * exception := FileNotFoundException
	 */
	public void loadImages() {
//		ImageIcon dotImageIcon = new ImageIcon("img/dot.png");
		URL url = Snake.class.getResource("/dotDemo.png");
		ImageIcon dotImageIcon = new ImageIcon(url);
		ball = dotImageIcon.getImage();
		
//		ImageIcon pelletImageIcon = new ImageIcon("img/pellet.png");
		url = Snake.class.getResource("/pelletDemo.png");
		ImageIcon pelletImageIcon = new ImageIcon(url);
		pellet = pelletImageIcon.getImage();
		
//		ImageIcon headImageIcon = new ImageIcon("img/head.png");
		url = Snake.class.getResource("/headDemo.png");
		ImageIcon headImageIcon = new ImageIcon(url);
		head = headImageIcon.getImage();
		
//		ImageIcon speedupImageIcon = new ImageIcon("img/speedup.png");
		url = Snake.class.getResource("/speedupDemo.png");
		ImageIcon speedupImageIcon = new ImageIcon(url);
		speedup = speedupImageIcon.getImage();
		
//		ImageIcon slowdownImageIcon = new ImageIcon("img/slowdown.png");
		url = Snake.class.getResource("/slowdownDemo.png");
		ImageIcon slowdownImageIcon = new ImageIcon(url);
		slowdown = slowdownImageIcon.getImage();
		
//		ImageIcon collisionImageIcon = new ImageIcon("img/collision.png");
		url = Snake.class.getResource("/collisionDemo.png");
		ImageIcon collisionImageIcon = new ImageIcon(url);
		collision = collisionImageIcon.getImage();
	}
	
	/**
	 * Initializes the game (initial snake length, initial snake position, find next pellet and start of timer).
	 * The timer controls the pace of the game.
	 */
	public void initGame() {
		dots = 3;
		
		for (int z = 0; z < dots; z++) {
			x[z] = 100 - z*DOT_SIZE; //initial position of snake
			y[z] = 100;
		}
		
		locatePellet();
		
		timer = new Timer(DELAY, this);
		timer.start();
		
	}
	
	/**
	 * Resets the game.
	 * 
	 * transition := this.dots = initialDotSize; forall z : int | z < this.dots : this.x[z] = initPosition - z * 10 and this.y[z] = initPosition;
	 * 				 this.rightDirection = true; this.leftDirection = false; this.upDirection = false; this.downDirection = false;
	 * 
	 * out := self
	 */
	public void reset() {
		dots = 3;
		current_score = 100;
		for (int z = 0; z < dots; z++) {
			x[z] = 100 - z*DOT_SIZE; //initial position of snake
			y[z] = 100;
		}
		
	
		locatePellet();
		
		rightDirection = true;
		leftDirection = false;
		upDirection = false;
		downDirection = false;
		
		new Board();
	}
	
	/**
	 * Public access to drawing to the window the specified Graphics object.
	 * 
	 * out := forall t : Image : draw t
	 * 
	 * @param g Graphics object
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	/**
	 * Private (behind the scenes) access to drawing to the window.  
	 * @param g Graphics object
	 */
	private void doDrawing(Graphics g) {
		if (inGame) {
			if (speedupPellet == true) {
				g.drawImage(speedup, pellet_x, pellet_y, this);
			} else if (slowdownPellet == true) { 
				g.drawImage(slowdown, pellet_x, pellet_y, this);
			} else if (collisionPellet == true) {
				g.drawImage(collision, pellet_x, pellet_y, this);
			} else {
				g.drawImage(pellet, pellet_x, pellet_y, this);
			}
			for(int z = 0; z < dots; z++) {
				if (z == 0) {
					g.drawImage(head, x[z], y[z], this);
				} else {
					g.drawImage(ball, x[z], y[z], this);
				}
			}
			
			Toolkit.getDefaultToolkit().sync();
			scoreboard.renderScoreInGame(g);
			
			g.drawLine(current_board_width+20, 0, current_board_width+20, current_board_height);
			g.drawLine(0, current_board_height, current_board_width+20, current_board_height);
			
		// menu stuff
		}else if (inMenu) {
			menu.render(g);
		}else if (inHelp) {
			menu.renderHelp(g);
		}else if (inSettings) {
			menu.renderSettings(g);
		}else {
			scoreboard.gameOver(g);
			gameOver = true;
		}
	}
	
	
	/**
	 * If the snake's head touches a pellet, add to snake body count and applies power-up if applicable.
	 * Afterwards, locate the next pellet.
	 * 
	 * transition := (this.x[0] == this.pellet_x and this.y[0] == this.pellet_y) and (this.dots += 1 and (this.speedupPellet and this.setDelay(110) and speedupPellet = false)
	 * 				 and (this.slowdownPellet and this.setDelay(170) and this.slowdownPellet = false) and (this.collisionPellet and this.collisionActive = true and this.collisionPellet = false)
	 * 
	 * 
	 */
	public void checkPellet() { 
		if ((x[0] == pellet_x) && (y[0] == pellet_y)) { 
			dots++;
			current_score += 10;
			
			
			if (speedupPellet == true) { 
				this.setDelay(110);
				speedupPellet = false;
			}
			else if (slowdownPellet == true) {
				this.setDelay(170);
				slowdownPellet = false;
				decrease_rate = 10;
			}
			else if (collisionPellet == true) {
				collisionActive = true;
				collisionPellet = false;
			}
			
			locatePellet();
		}
	}
	
	/**
	 * Moves the snake across the board. Also times out power-ups after an elapsed time.
	 * 
	 * transition := (this.timer.getDelay() != 140 and this.stepCount += 1); (this.stepCount > 35 and this.setDelay(140) and this.stepCount = 0);
	 *               (this.collisionActive and this.immunityCount += 1); (this.immunityCount > 35 and this.collisionActive = false and this.immunityCount = 0);
	 *               forall z : int | z > 0 : z -= 1 and this.x[z] = x[(z-1)] and this.y[z] = y[(z-1)]; (this.leftDirection and x[0] -= this.DOT_SIZE);
	 *               (this.rightDirection and x[0] += this.DOT_SIZE); (this.upDirection and y[0] -= DOT_SIZE); (this.downDirection and y[0] += this.DOT_SIZE)
	 */
	public void move() {
		
		
		if (this.timer.getDelay() != 140) {
			
			stepCount++;
		}
		
		if (score_decrease_counter <= decrease_rate) {
			score_decrease_counter ++;
		}
		else {
			score_decrease_counter = 0;
			current_score --;
		}
	
		
		if (stepCount > 35) { //after moving 25 times, reset the delay and powerup effects
			this.setDelay(140);
			stepCount = 0;
			decrease_rate =5;
		}
		
		if (collisionActive == true) immunityCount++;
		
		if (immunityCount > 35) {
			collisionActive = false;
			immunityCount = 0;
		}
		
		for (int z = dots; z > 0; z--) {
			x[z] = x[(z-1)];
			y[z] = y[(z-1)];
		}
		
		if (leftDirection) x[0] -= DOT_SIZE;
		if (rightDirection) x[0] += DOT_SIZE;
		if (upDirection) y[0] -= DOT_SIZE;
		if (downDirection) y[0] += DOT_SIZE;
	}
	
	/**
	 * Checks if snake collides with itself or the board's boundary. Typically ends game unless under
	 * collision immunity power-up.
	 * 
	 * transition := forall (z : int | z > 0 : z -= 1 and z > 4 and x[0] == x[z] and y[0] == y[z] and !this.collisionActive and this.inGame = false);
	 * 				 (this.y[0] >= this.BOARD_HEIGHT and this.inGame = false); (this.y[0] < 0 and this.inGame = false);
	 * 				 (this.x[0] >= this.BOARD_WIDTH and this.inGame = false); (this.x[0] < 0 and this.inGame = false);
	 * 			     (!this.inGame and this.timer.stop());				 
	 * 
	 * @throws InterruptedException If thread's sleep is interrupted.
	 */
	public void checkCollision() throws InterruptedException {
		for (int z = dots; z > 0; z--) { 
			if ((z>4) && (x[0] == x[z]) && (y[0] == y[z])) {
				if (collisionActive == true) {
					Thread.sleep(500); // to feel like it lagged a bit
					return;
				}
				inGame = false;
			}	
		}
		
		if (y[0] >= current_board_height-score_size) inGame = false;
		
		if (y[0] < 0) inGame = false;
		
		if (x[0] >= current_board_width) inGame = false;
		
		if (x[0] < 0) inGame = false;
		
		if (current_score <= 0) {
			inGame = false;
			gameOver = true;
		}
		
		if(!inGame) timer.stop();
	}
	
	/**
	 * Places next pellet randomly on the screen. (Either power-up or otherwise).
	 * 
	 * transition := (this.rand.nextInt(10) + 1 == 3 and this.speedupPellet = true) or (this.rand.nextInt(10) + 1 == 7 and this.slowdownPellet = true)
	 * 				 or (this.rand.nextInt(10) + 1 == 4 and this.collisionPellet = true);
	 * 				 this.pellet_x = Math.random() * this.RAND_POS * this.DOT_SIZE; this.pellet_y = Math.random() * this.RAND_POS * this.DOT_SIZE;
	 * 
	 */
	public void locatePellet() { 
		if (rand.nextInt(10) + 1 == 3) { // one in ten chance to spawn speed up
			speedupPellet = true;
		} else if (rand.nextInt(10) + 1 == 7) { // slow down
			slowdownPellet = true;
		} else if (rand.nextInt(10) + 1 == 4) { // collision immunity
			collisionPellet = true;
		} 
		else {
			speedupPellet = false;
			slowdownPellet = false;
			collisionPellet = false;
		}
		
		int r;
		
		if (this.current_board_width == 800) {
			r = (int) (Math.random() * RAND_POS);
			pellet_x = ((r*DOT_SIZE));
			
			r = (int) (Math.random() * RAND_POS);
			pellet_y = ((r*DOT_SIZE));
		} else if (this.current_board_width == 600) {
			r = (int) (Math.random() * 30);
			pellet_x = ((r*DOT_SIZE));
			
			r = (int) (Math.random() * 30);
			pellet_y = ((r*DOT_SIZE));
		} else {
			r = (int) (Math.random() * 15);
			pellet_x = ((r*DOT_SIZE));
			
			r = (int) (Math.random() * 15);
			pellet_y = ((r*DOT_SIZE));
		}
	}
	
	/**
	 * Called whenever an action occurs. If the game is currently running, checks if pellet was consumed 
	 * by the snake, checks if collision has occurred and finally moves the snake.
	 * Repaints the scene afterwards.
	 * 
	 * @param e ActionEvent object
	 */
	@Override
	public void actionPerformed(ActionEvent e) { 
		if (inGame) {
			checkPellet();
			
			try { checkCollision();
			} catch (InterruptedException z) { System.out.println("This shouldn't occur ever."); }
			move();
		}
		repaint();
	}
	
	/**
	 * Private class to handle keyboard input.
	 * @author Harrison Lau
	 * 
	 * Written with heavy help from http://zetcode.com/tutorials/javagamestutorial/snake/
	 *
	 */
	private class TAdapter extends KeyAdapter {
		 
		/**
		 * Upon key press, handle accordingly (arrow keys, r and quit).
		 * @param e KeyEvent object
		 */
		
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode(); /*!< The code of key pressed. */
			
			if (inGame) {
				if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
					leftDirection = true;
					upDirection = false;
					downDirection = false;
				}
				
				if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
					rightDirection = true;
					upDirection = false;
					downDirection = false;
				} 
				
				if ((key == KeyEvent.VK_UP) && (!downDirection)) {
					upDirection = true;
					leftDirection = false;
					rightDirection = false;
				} 	
				
				if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
					downDirection = true;
					leftDirection = false;
					rightDirection = false;
				} 
				
				if (key == KeyEvent.VK_R) reset();
				
				if (key == KeyEvent.VK_Q || key == KeyEvent.VK_ESCAPE) System.exit(0);
			}
		}
	}
	
	// For handling mouse clicks in Menu
	private class MouseInput implements MouseListener {

		/*
			public Rectangle playButton = new Rectangle(80, 120, 130, 30);
			public Rectangle helpButton = new Rectangle(80, 170, 130, 30);
			public Rectangle settingsButton = new Rectangle(80, 220, 130, 30);
		 
		 *  public Rectangle playInHelp = new Rectangle(150, 250, 120, 30);
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			
			if (inMenu) {
				// Play Button
				if (mouseX >= 180 && mouseX <= 630) {
					if (mouseY >= 400 && mouseY <= 480) {
						
						// Pressed Play button
						inGame = true;
						inMenu = false;
						inHelp = false;
						gameOver = false;
						inSettings = false;
					}
				}
				
				// Help button pressed
				if (mouseX >= 180 && mouseX <= 630) {
					if (mouseY >= 500 && mouseY <= 580) {
						inHelp = true;
						inMenu = false;
						inGame = false;
					}
				}
				// Settings button pressed
				if (mouseX >= 180 && mouseX <= 630) {
					if (mouseY >= 600 && mouseY <= 680) {
						inSettings = true;
						inMenu = false;
						inGame = false;
					}
				}
			}
			
			if (inHelp) {
				if (mouseX >= 490 && mouseX <= 720) {
					if (mouseY >= 680 && mouseY <= 760) {
						inGame = true;
						inHelp = false;
					}
				}
				if (mouseX >= 80 && mouseX <= 280) {
					if (mouseY >= 680 && mouseY <= 760) {
						inMenu = true;
						inHelp = false;
					}
				}
			}
			
			if (inSettings) {
				if (mouseX >= 490 && mouseX <= 720) {
					if (mouseY >= 680 && mouseY <= 760) {
						inGame = true;
						inSettings = false;
					}
				}
				if (mouseX >= 80 && mouseX <= 280) {
					if (mouseY >= 680 && mouseY <= 760) {
						inMenu = true;
						inSettings = false;
					}
				}

				if (mouseX >= 180 && mouseX <= 630) {
					if (mouseY >= 300 && mouseY <= 380) {
						// Pressed Small button
						current_board_width = 300;
						current_board_height = 320;
						inGame = true; 
						inSettings = false;
						
					}
				}
				
				// Medium button pressed
				if (mouseX >= 180 && mouseX <= 630) {
					if (mouseY >= 400 && mouseY <= 480) {
						current_board_width = 600;
						current_board_height = 620;
						inGame = true; 
						inSettings = false;
					
					}
				}
				// Large button pressed
				if (mouseX >= 180 && mouseX <= 630) {
					if (mouseY >= 500 && mouseY <= 580) {
						current_board_width = 800;
						current_board_height = 820;
						inGame = true; 
						inSettings = false;
					
					}
				}
			}
			
			if (gameOver) {
				if (mouseX >= 180 && mouseX <= 630) {
					if (mouseY >= 550 && mouseY <= 630) {
						
						// Pressed Play button
						inGame = true;
						gameOver = false;
					}
					reset();
					initGame();
				}
				if (mouseX >= 180 && mouseX <= 630) {
					if (mouseY >= 650 && mouseY <= 730) {
						inMenu = true;
						gameOver = false;
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}