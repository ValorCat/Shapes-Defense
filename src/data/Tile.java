package data;

import static util.Artist.drawObject;
import static util.Artist.loadTexture;

import org.newdawn.slick.opengl.Texture;

public class Tile {

	private float x, y;
	private Texture texture;
	private TileType type;
	
	public Tile(float x, float y, TileType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.texture = loadTexture("tile", type.textureName);
	}

	public void draw() {
		drawObject(texture, x, y);
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}
	
	public boolean compareTo(Tile otherTile) {
		return x == otherTile.getX() && y == otherTile.getY();
	}
}
