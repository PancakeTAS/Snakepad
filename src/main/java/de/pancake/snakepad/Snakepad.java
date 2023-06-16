package de.pancake.snakepad;

import com.harry1453.klaunchpad.impl.launchpads.mk2.LaunchpadMk2Pad;

import de.pancake.snakepad.SnakeGame.Difficulty;
import de.pancake.snakepad.SnakeGame.Direction;
import de.pancake.snakepad.utils.ColorUtils;
import de.pancake.snakepad.utils.MidiUtils;

public class Snakepad {

	public static void main(String[] args) {
		// setup launchpad and game
		var launchpad = MidiUtils.setupLaunchpad();
		var game = new SnakeGame(launchpad);
		
		// configure game state lights
		launchpad.setPadLight(LaunchpadMk2Pad.R1, ColorUtils.PURPLE);
		launchpad.pulsePadLight(LaunchpadMk2Pad.T6, ColorUtils.GREEN);
		launchpad.setPadLight(LaunchpadMk2Pad.T7, ColorUtils.RED);
		
		// configure game control lights
		launchpad.pulsePadLight(LaunchpadMk2Pad.T1, ColorUtils.BLUE);
		launchpad.setPadLight(LaunchpadMk2Pad.T2, ColorUtils.BLUE);
		launchpad.setPadLight(LaunchpadMk2Pad.T3, ColorUtils.BLUE);
		launchpad.setPadLight(LaunchpadMk2Pad.T4, ColorUtils.BLUE);
		
		// configure button actions
		launchpad.setPadButtonListener((pad,state,b) -> {
			
			if (!state)
				return null;
			
			// game state
			if (pad == LaunchpadMk2Pad.R1)
				game.restartGame();
			else if (pad == LaunchpadMk2Pad.T6)
				game.setDifficulty(Difficulty.EASY);
			else if (pad == LaunchpadMk2Pad.T7)
				game.setDifficulty(Difficulty.HARD);
			// game control
			else if (pad == LaunchpadMk2Pad.T1)
				game.setDirection(Direction.UP);
			else if (pad == LaunchpadMk2Pad.T2)
				game.setDirection(Direction.DOWN);
			else if (pad == LaunchpadMk2Pad.T3)
				game.setDirection(Direction.LEFT);
			else if (pad == LaunchpadMk2Pad.T4)
				game.setDirection(Direction.RIGHT);
			
			return null;
		});
	}

}
