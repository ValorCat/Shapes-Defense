package data;

import static util.Artist.HEIGHT;
import static util.Artist.TILE_SIZE;
import static util.Artist.WIDTH;
import static util.Artist.drawObject;
import static util.Artist.loadTexture;

import org.newdawn.slick.opengl.Texture;

import ui.UserInterface;
import util.Clock;
import util.MapManager;

public class Game {

	public static Player player;
	public static TileGrid grid;
	public static WaveManager waves;
	public static UserInterface gui;
	
	private Texture pausedTexture = loadTexture("gui", "pause");
	private Texture waveCountFrameTexture = loadTexture("misc", "bar_frame");
	private Texture waveCountTexture = loadTexture("misc", "bar_blue");
	
	public Game() {
		// map
		grid = MapManager.loadMap("swirl");
		
		// enemies
		waves = new WaveManager(4);
		
		// towers and mouse input
		player = new Player();
		
		// game interface
		gui = new UserInterface();
		gui.addButton("select_circle", "icon_circle", WIDTH - TILE_SIZE, TILE_SIZE, true);
		gui.addButton("select_triangle", "icon_triangle", WIDTH - TILE_SIZE, TILE_SIZE * 2, true);
		gui.addButton("select_square", "icon_square", WIDTH - TILE_SIZE, TILE_SIZE * 3, true);
		gui.addButton("select_shredder", "icon_shredder", WIDTH - TILE_SIZE, TILE_SIZE * 4, true);
		gui.addImage("tower_select_bg", "tower_selector", WIDTH - TILE_SIZE, TILE_SIZE);
	}
	
	public void update() {
		grid.draw();
		gui.draw();
		player.update();
		waves.update();
		
		if (Clock.paused) {
			float percent = (float) waves.getWaveNumber() / (float) waves.getTotalWaveCount();
			float x = WIDTH/2 - 4.5f * TILE_SIZE;
			float y = HEIGHT/2 - 1.25f * TILE_SIZE;
			float w = 8.5f * TILE_SIZE;
			float h = 2.5f * TILE_SIZE;
			drawObject(pausedTexture, x, y, w, h);
			drawObject(waveCountTexture, x + 32, y + h - 36, (w - 64) * percent, 10);
			drawObject(waveCountFrameTexture, x + 32, y + h - 36, w - 64, 10);
		}
	}
}
