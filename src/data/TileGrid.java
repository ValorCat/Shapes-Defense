package data;

import static util.Artist.TILE_SIZE;
import static util.Artist.drawObject;

public class TileGrid {

	private int width, height;
	private Direction startDirection;
	private Tile startTile;
	public Tile[][] map;
	
	public TileGrid() {
		this.width = 20;
		this.height = 15;
		this.map = new Tile[width][height];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[j].length; j++) {
				map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TileType.Blue);
			}
		}
	}
	
	public void setTile(int x, int y, TileType type) {
		map[x][y] = new Tile (x * 64, y * 64, type);
	}
	
	public Tile getTile(int x, int y) {
		try {
			return map[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return new Tile(-1, -1, TileType.Blank);
		}
	}
	
	public void draw() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[j].length; j++) {
				Tile tile = map[i][j];
				drawObject(tile.getTexture(), tile.getX(), tile.getY());
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public Direction getStartDirection() {
		return startDirection;
	}

	public void setStartDirection(Direction startDirection) {
		this.startDirection = startDirection;
	}

	public Tile getStartTile() {
		return startTile;
	}

	public void setStartTile(Tile startTile) {
		this.startTile = startTile;
	}
}
