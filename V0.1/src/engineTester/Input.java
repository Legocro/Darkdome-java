package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import entities.Player;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Input {

	//Key Numbers can be found here -> https://minecraft.gamepedia.com/Key_codes
	private static void setFullScreen() {
		if (Keyboard.isKeyDown(28) && Keyboard.isKeyDown(56)) {
			if (Display.isActive()) {
				if (!Display.isFullscreen()) {
					DisplayManager.setDisplayMode(DisplayManager.getMaxWidth(), DisplayManager.getMaxHeight(), true);
				    Display.setVSyncEnabled(true);
				}
				else {
					DisplayManager.setDisplayMode(DisplayManager.getMaxWidth(), DisplayManager.getMaxHeight(), false);
				    Display.setVSyncEnabled(false);
				}
			}
		}
	}
	
	private static void closeApp() {
		if (Keyboard.isKeyDown(1))
			System.exit(1);
	}
	
	public static void checkForInputs(Player player, Terrain terrain) {
		player.move(terrain);
		setFullScreen();
		closeApp();
	}
	
	public static void takeScreenShot() {
		if (Keyboard.isKeyDown(210))
			DisplayManager.screenShotManager();
	}
}
