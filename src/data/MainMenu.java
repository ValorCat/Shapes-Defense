package data;

import static util.Artist.HEIGHT;
import static util.Artist.WIDTH;
import static util.Artist.drawObject;
import static util.Artist.loadTexture;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import ui.UserInterface;
import util.StateManager;
import util.StateManager.GameState;

public class MainMenu {

	private Texture background, title;
	private UserInterface menu;
	
	public MainMenu() {
		this.background = loadTexture("gui", "bg_main_menu");
		this.title = loadTexture("gui", "txt_title");
		this.menu = new UserInterface();
		menu.addButton("play", "btn_play", WIDTH/2 - 128, (int) (HEIGHT * 0.4f), true);
		menu.addButton("edit", "btn_editor", WIDTH/2 - 128, (int) (HEIGHT * 0.5f), true);
		menu.addButton("exit", "btn_exit", WIDTH/2 - 128, (int) (HEIGHT * 0.6f), true);
	}
	
	public void update() {
		drawObject(background, 0, 0, 2048, 1024);
		drawObject(title, 0, 0, 2048, 1024);
		menu.draw();
		if (Mouse.isButtonDown(0)) {
			if (menu.isButtonHoveredOn("play")) {
				StateManager.gameState = GameState.GAME;
			} else if (menu.isButtonHoveredOn("edit")) {
				StateManager.gameState = GameState.EDITOR;
			} else if (menu.isButtonHoveredOn("exit")) {
				System.exit(0);
			}
		}
	}
}
