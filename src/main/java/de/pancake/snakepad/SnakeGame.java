package de.pancake.snakepad;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import com.harry1453.klaunchpad.api.Launchpad;
import com.harry1453.klaunchpad.impl.launchpads.mk2.LaunchpadMk2Pad;

import de.pancake.snakepad.utils.ColorUtils;
import kotlin.Pair;

public class SnakeGame extends TimerTask {
	public enum Difficulty { EASY, HARD }
	public enum Direction { UP, DOWN, LEFT, RIGHT }
	
	private Launchpad launchpad;
	
	private Difficulty difficulty;
	private Direction direction;
	private Pair<Integer, Integer> foodPos;
	private List<Pair<Integer, Integer>> playerPos;
	private Timer timer;
	
	
	/**
	 * Initialize snake game
	 */
	public SnakeGame(Launchpad launchpad) {
		this.playerPos = new LinkedList<Pair<Integer, Integer>>() {

			@Override
			public Pair<Integer, Integer> remove(int index) {
				var pos = this.get(index);
				launchpad.clearPadLight(launchpad.getPad(pos.component1(), pos.component2()));
				return super.remove(index);
			}
			
		};
		this.difficulty = Difficulty.EASY;
		this.launchpad = launchpad;
		this.resetGame();
	}
	
	/**
	 * Reset the game
	 */
	private void resetGame() {
		// cancel game
		if (this.timer != null) 
			this.timer.cancel();
		
		// clear lights
		if (this.foodPos != null)
			this.launchpad.clearPadLight(this.launchpad.getPad(this.foodPos.component1(), this.foodPos.component2()));
		
		for (var pos : this.playerPos)
			this.launchpad.clearPadLight(this.launchpad.getPad(pos.component1(), pos.component2()));
		
		// reset lights
		this.launchpad.pulsePadLight(LaunchpadMk2Pad.T1, ColorUtils.BLUE);
		this.launchpad.setPadLight(LaunchpadMk2Pad.T2, ColorUtils.BLUE);
		this.launchpad.setPadLight(LaunchpadMk2Pad.T3, ColorUtils.BLUE);
		this.launchpad.setPadLight(LaunchpadMk2Pad.T4, ColorUtils.BLUE);
		this.launchpad.pulsePadLight(LaunchpadMk2Pad.T6, ColorUtils.GREEN);
		this.launchpad.setPadLight(LaunchpadMk2Pad.T7, ColorUtils.RED);
		
		// reset game
		this.direction = Direction.UP;
		this.foodPos = new Pair<>(5, 5);
		this.launchpad.setPadLight(this.launchpad.getPad(5, 5), ColorUtils.RED);
		this.playerPos.clear();
		this.playerPos.add(new Pair<>(2, 2));
		this.launchpad.setPadLight(this.launchpad.getPad(2, 2), ColorUtils.GREEN);
		this.timer = new Timer("Snake Timer", true);
	}
	
	/**
	 * Restart the game
	 */
	public void restartGame() {
		this.resetGame();
		this.timer.scheduleAtFixedRate(this, 400L, this.difficulty == Difficulty.EASY ? 400 : 250);
	}

	/**
	 * Run game loop
	 */
	@Override
	public void run() {
		var head = this.playerPos.get(0);
		
		// add new head in moving direction
		int x = head.component1();
		int y = head.component2();
		switch (this.direction) {
			case UP:
				y++;
				break;
			case DOWN:
				y--;
				break;
			case LEFT:
				x--;
				break;
			case RIGHT:
				x++;
				break;
		}
		
		if (x > 7)
			x = 0;
		
		if (x < 0)
			x = 7;
		
		if (y > 7)
			y = 0;
		
		if (y < 0)
			y = 7;
		
		this.playerPos.add(0, new Pair<>(x, y));
		
		// check for food
		if (this.playerPos.get(0).equals(this.foodPos)) {
			do {
				var pair = new Pair<Integer, Integer>((int) (Math.random() * 8), (int) (Math.random() * 8));
				
				if (!this.playerPos.contains(pair)) {
					this.foodPos = pair;
					this.launchpad.setPadLight(this.launchpad.getPad(this.foodPos.component1(), this.foodPos.component2()), ColorUtils.RED);
					break;
				}
				
			} while (true);
		} else {
			// remove last elements
			this.playerPos.remove(this.playerPos.size() - 1);
		}
		
		// redraw player
		for (var pos : this.playerPos)
			this.launchpad.setPadLight(launchpad.getPad(pos.component1(), pos.component2()), ColorUtils.GREEN);
		
		// check for collisions
		var check = new HashSet<Pair<Integer, Integer>>();
		int duplicates = this.playerPos.stream().filter(n -> !check.add(n)).collect(Collectors.toSet()).size();
		if (duplicates > 0)
			this.timer.cancel();
		
	}

	/**
	 * Set difficulty of the game
	 * @param difficulty Difficulty
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
		
		if (this.timer != null)
			this.timer.cancel();
		
		this.resetGame();
		
		if (difficulty == Difficulty.EASY) {
			this.launchpad.pulsePadLight(LaunchpadMk2Pad.T6, ColorUtils.GREEN);
			this.launchpad.setPadLight(LaunchpadMk2Pad.T7, ColorUtils.RED);
		} else {
			this.launchpad.setPadLight(LaunchpadMk2Pad.T6, ColorUtils.GREEN);
			this.launchpad.pulsePadLight(LaunchpadMk2Pad.T7, ColorUtils.RED);
		}
	}

	/**
	 * Set direction of the player
	 * @param direction Direction
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
		
		switch (this.direction) {
			case UP:
				this.launchpad.pulsePadLight(LaunchpadMk2Pad.T1, ColorUtils.BLUE);
				this.launchpad.setPadLight(LaunchpadMk2Pad.T2, ColorUtils.BLUE);
				this.launchpad.setPadLight(LaunchpadMk2Pad.T3, ColorUtils.BLUE);
				this.launchpad.setPadLight(LaunchpadMk2Pad.T4, ColorUtils.BLUE);
				break;
			case DOWN:
				this.launchpad.setPadLight(LaunchpadMk2Pad.T1, ColorUtils.BLUE);
				this.launchpad.pulsePadLight(LaunchpadMk2Pad.T2, ColorUtils.BLUE);
				this.launchpad.setPadLight(LaunchpadMk2Pad.T3, ColorUtils.BLUE);
				this.launchpad.setPadLight(LaunchpadMk2Pad.T4, ColorUtils.BLUE);
				break;
			case LEFT:
				this.launchpad.setPadLight(LaunchpadMk2Pad.T1, ColorUtils.BLUE);
				this.launchpad.setPadLight(LaunchpadMk2Pad.T2, ColorUtils.BLUE);
				this.launchpad.pulsePadLight(LaunchpadMk2Pad.T3, ColorUtils.BLUE);
				this.launchpad.setPadLight(LaunchpadMk2Pad.T4, ColorUtils.BLUE);
				break;
			case RIGHT:
				this.launchpad.setPadLight(LaunchpadMk2Pad.T1, ColorUtils.BLUE);
				this.launchpad.setPadLight(LaunchpadMk2Pad.T2, ColorUtils.BLUE);
				this.launchpad.setPadLight(LaunchpadMk2Pad.T3, ColorUtils.BLUE);
				this.launchpad.pulsePadLight(LaunchpadMk2Pad.T4, ColorUtils.BLUE);
				break;
		}
	}
	
}
