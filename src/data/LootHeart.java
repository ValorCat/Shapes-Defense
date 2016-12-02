package data;

import java.util.Random;

import org.newdawn.slick.opengl.Texture;

import util.Artist;
import util.Clock;
import util.Input;

public class LootHeart implements Entity {

	private boolean alive;
	private float lifetime, value, x, y;
	private int h, w;
	private Texture texture;
	
	public LootHeart(float x, float y) {
		this.x = x;
		this.y = y;
		
		this.alive = true;
		this.h = 24;
		this.texture = Artist.loadTexture("misc", "heart");
		this.w = 24;
		
		Random rand = new Random();
		this.lifetime = rand.nextInt(2) + 2.5f;
		this.value = rand.nextInt(5) + 3;
	}

	public void update() {
		boolean xInRange = Input.getMouseX() >= x && Input.getMouseX() <= x + w;
		boolean yInRange = Input.getMouseY() >= y && Input.getMouseY() <= y + h;
		if (xInRange && yInRange) {
			alive = false;
			Player.updateEnergy(value);
		}
		
		lifetime -= Clock.delta();
		if (lifetime <= 0)
			alive = false;
		
		draw();
	}
	
	public void draw() {
		Artist.drawObject(texture, x, y, w, h);
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
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

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
