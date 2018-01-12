import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class scoreTesting {

	@Test
	public void testScoreCounterIncrease() {
		Board snake = new Board();
		snake.initGame();
		
		snake.reset();
		
		snake.pellet_x = 30;
		snake.pellet_y = 30;
		snake.x[0] = 30;
		snake.y[0] = 30;
		
		snake.checkPellet();
		
		// Checks that current score increases by 10 from default score 100
		assertEquals(snake.current_score, 110);
		
		snake.reset();
		
		// Snake moves 7 times to decrement points by 1
		for (int i = 0; i < 7; i++) {
			snake.move();
		}
		
		snake.pellet_x = 30;
		snake.pellet_y = 30;
		snake.x[0] = 30;
		snake.y[0] = 30;
		
		snake.checkPellet();
		
		// Assertion tests
		assertEquals(snake.current_score, 109); // Snake's points decrement by 1, but increment by 10 after consuming pellet
		assertNotEquals(snake.current_score, 110);
		assertNotEquals(snake.current_score, 100);
		assertNotEquals(snake.current_score, 99);
	}
	
	@Test
	public void testScoreCounterDecrease() {
		Board snake = new Board();
		snake.initGame();

		snake.reset();
		
		// Snake moves 7 times to decrement points by 1
		for (int i = 0; i < 7; i++) {
			snake.move();
		}
		
		// Assertion tests
		assertEquals(snake.current_score, 99);
		assertNotEquals(snake.current_score, 100);
		assertNotEquals(snake.current_score, 101);
	}

}
