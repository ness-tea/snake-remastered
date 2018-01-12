import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */

/**
 * @author Vanessa
 *
 */
public class pelletTesting {

	@Test
	public void testPelletGrowth() throws InterruptedException {
		Board snake = new Board();
		snake.initGame();
		
		// Snake's head reaches pellet location
		snake.pellet_x = 30;
		snake.pellet_y = 30;
		snake.x[0] = 30;
		snake.y[0] = 30;
		
		// checks if snake has consumed pellet
		snake.checkPellet();
		
		// Checks that snake's body grows by 1
		assertEquals(snake.dots, 4);
		assertNotEquals(snake.dots, 3);
		
		// pellet at different location
		snake.pellet_x = 30;
		snake.pellet_y = 50;
		snake.x[0] = 30;
		snake.y[0] = 30;
		
		snake.checkPellet();
		
		// Checks that snake's body has not changed
		assertEquals(snake.dots, 4);
		assertNotEquals(snake.dots, 5);
	}
	
	@Test
	public void testSpeedUpPellet() {
		Board snake = new Board();
		snake.initGame();

		assertEquals(snake.stepCount, 0);
		
		snake.speedupPellet = true;
		
		snake.pellet_x = 30;
		snake.pellet_y = 30;
		snake.x[0] = 30;
		snake.y[0] = 30;
		
		snake.checkPellet();
		
		// snake moves 10 times for step counter to increase to 10
		for (int i = 0; i < 10; i++) {
			snake.move();
		}
		
		// stepCount is activated when speedUp pellet consumed
		assertEquals(snake.stepCount, 10);
		
		// Snake moves total of 36 times
		for (int i = 0; i < 26; i++) {
			snake.move();
		}
		
		// stepCount is resetted 
		assertEquals(snake.stepCount, 0);
		assertNotEquals(snake.stepCount, 36);
		assertEquals(snake.decrease_rate, 5);	
	}

	@Test
	public void testSlowDownPellet() {
		Board snake = new Board();
		snake.initGame();
		
		assertEquals(snake.stepCount, 0);
		assertEquals(snake.decrease_rate, 5);
		
		snake.slowdownPellet = true;
		
		snake.pellet_x = 30;
		snake.pellet_y = 30;
		snake.x[0] = 30;
		snake.y[0] = 30;
		
		snake.checkPellet();
		
		// snake moves 10 times for step counter to increase to 10
		for (int i = 0; i < 10; i++) {
			snake.move();
		}
		
		assertEquals(snake.stepCount, 10);
		assertEquals(snake.decrease_rate, 10);
		
		for (int i = 0; i < 36; i++) {
			snake.move();
		}
		
		assertEquals(snake.stepCount, 0);
		assertEquals(snake.decrease_rate, 5);
		assertNotEquals(snake.stepCount, 36);
		assertNotEquals(snake.slowdownPellet, true);
	}
	
	@Test
	public void testCollisionPellet() {
		Board snake = new Board();
		snake.initGame();
		
		assertEquals(snake.collisionActive, false);
		
		// Set active pellet to be collision pellet
		snake.collisionPellet = true;
		
		snake.pellet_x = 30;
		snake.pellet_y = 30;
		snake.x[0] = 30;
		snake.y[0] = 30;
		
		snake.checkPellet();
		
		for (int i = 0; i < 10; i++) {
			snake.move();
		}
		
		assertEquals(snake.collisionActive, true);
		assertEquals(snake.immunityCount, 10);
		
		for (int i = 0; i < 36; i++) {
			snake.move();
		}
		
		assertEquals(snake.immunityCount, 0);
		assertNotEquals(snake.immunityCount, 36);
		assertEquals(snake.collisionActive, false);
		assertEquals(snake.collisionPellet, false);
 	}
	
}
