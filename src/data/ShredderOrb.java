package data;

import static util.Artist.drawRotatedObject;
import static util.Artist.isCollision;
import static util.Artist.loadTexture;

import org.newdawn.slick.opengl.Texture;

import util.Clock;

public class ShredderOrb implements Entity {

	private boolean alive;
	private float damage, rotation, timeRemaining, x, y;
	private int h, w;
	private Texture texture;
	
	public ShredderOrb(float x, float y) {
		this.x = x;
		this.y = y;
		
		this.alive = true;
		this.damage = 0.06f;
		this.h = 32;
		this.rotation = 0;
		this.texture = loadTexture("tower", "barrier");
		this.timeRemaining = 6;
		this.w = 32;
	}

	public void update() {
		for (Enemy e : WaveManager.getEnemies()) {
			if (isCollision(x, y, w, h, e.getX(), e.getY(), e.getWidth(), e.getHeight())) {
				e.takeDamage(damage);
			}
		}
		if (timeRemaining <= 0) {
			alive = false;
		}
		if (!Clock.paused)
			draw();
		timeRemaining -= Clock.delta();
		rotation += 6;
	}

	public void draw() {
		drawRotatedObject(texture, x, y, w, h, rotation);
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public float getTimeRemaining() {
		return timeRemaining;
	}

	public void setTimeRemaining(float timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
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

	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}
}
