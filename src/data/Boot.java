package data;

import static util.Artist.beginSession;

import org.lwjgl.opengl.Display;

import util.Clock;
import util.Input;
import util.MusicPlayer;
import util.StateManager;

public class Boot {

	public Boot() {
		beginSession();
				
		while (!Display.isCloseRequested()) {
			Clock.update();
			StateManager.update();
			Input.update();
			MusicPlayer.update();
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		System.out.println((int) (Clock.totalTime / 60) + " minutes played. Goodbye!");
	}
	
	public static void main(String[] args) {
		new Boot();
	}
}
