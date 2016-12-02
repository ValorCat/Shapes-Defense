package data;

import java.util.Random;

import org.newdawn.slick.opengl.Texture;

import util.Artist;
import util.Clock;
import util.Input;

public class LootTowerXP implements Entity {

	private boolean alive;
	private float angle, lifetime, x, y;
	private int h, w;
	private Texture texture;
	
	public LootTowerXP(float x, float y) {
		this.x = x;
		this.y = y;
		
		this.alive = true;
		this.angle = 0;
		this.h = 24;
		this.texture = Artist.loadTexture("misc", "tower_xp");
		this.w = 24;
		
		Random rand = new Random();
		this.lifetime = rand.nextInt(2) + 2.5f;
	}
	
	public void update() {
		boolean xInRange = Input.getMouseX() >= x && Input.getMouseX() <= x + w;
		boolean yInRange = Input.getMouseY() >= y && Input.getMouseY() <= y + h;
		if (xInRange && yInRange) {
			alive = false;
			Random rand = new Random();
			int index = rand.nextInt(Player.towers.size());
			Tower tower = Player.towers.get(index);
			tower.updateExp(tower.getExpGoal() - tower.getExp());
		}
		
		lifetime -= Clock.delta();
		if (lifetime <= 0)
			alive = false;
		
		draw();
		angle += 0.5f;
	}

	public void draw() {
		Artist.drawRotatedObject(texture, x, y, w, h, angle);
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
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
