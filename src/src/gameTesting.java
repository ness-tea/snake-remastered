
import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class gameTesting {

	@Test
	public void testInitState() {
		Board snake = new Board();
		snake.initGame();
		
		// Checks that snake is moving in right direction
		assertEquals(snake.rightDirection, true);
		// Checks if snake starts with 3 dots in body
		assertEquals(snake.dots, 3);
		// Checks if current score is 100
		assertEquals(snake.current_score, 100);
	}
	
	@Test
	public void testReset() {
		Board snake = new Board();
		snake.initGame();
		
		snake.dots = 40;
		snake.current_score = 500;
		 
		// Sets snake dots and current score to non-default values
		assertEquals(snake.dots, 40);
		assertEquals(snake.current_score, 500);
		
		snake.reset();
		
		// Checks to ensure that dots and current score are reset to default values
		assertEquals(snake.dots, 3);
		assertEquals(snake.current_score, 100);
	}

}