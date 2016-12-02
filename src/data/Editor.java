package data;

import static util.Artist.HEIGHT;
import static util.Artist.TILE_SIZE;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import util.Artist;
import util.MapManager;

public class Editor {

	private int selectedTile;
	private TileGrid grid;
	private TileType[] tileEasel;
	
	public Editor() {
		this.grid = new TileGrid();
		this.selectedTile = 0;
		this.tileEasel = new TileType[4];		
		this.tileEasel[0] = TileType.Blue;
		this.tileEasel[1] = TileType.Gray;
		this.tileEasel[2] = TileType.Red;
		this.tileEasel[3] = TileType.Finish;
	}
	
	public void update() {
		grid.draw();
		highlightTile();
		if (Mouse.isButtonDown(0)) {
			paintTile();
		}
		
		while (Keyboard.next()) {
			int pressed_key = Keyboard.getEventKey();
			if (Keyboard.getEventKeyState()) {
				switch (pressed_key) {
				case Keyboard.KEY_RIGHT:
					selectedTile++;
					if (selectedTile > tileEasel.length - 1)
						selectedTile = 0;
					break;
				case Keyboard.KEY_LEFT:
					selectedTile--;
					if (selectedTile < 0)
						selectedTile = tileEasel.length - 1;
					break;
				case Keyboard.KEY_S:
					MapManager.saveMap("new", grid);
					break;
				}
			}
		}
	}
	
	public Tile getSelectedTile() {
		int x = (int) Math.floor(Mouse.getX() / TILE_SIZE);
		int y = (int) Math.floor((HEIGHT - Mouse.getY() - 1) / TILE_SIZE);
		return grid.getTile(x, y);
	}
	
	private void highlightTile() {
		Tile tile = getSelectedTile();
		Texture highlight = Artist.loadTexture("gui", "highlight_green");
		Artist.drawObject(highlight, tile.getX(), tile.getY());
	}
	
	private void paintTile() {
		Tile tile = getSelectedTile();
		grid.setTile((int) tile.getX() / TILE_SIZE, (int) tile.getY() / TILE_SIZE, tileEasel[selectedTile]);
	}
}
