package data;

import static util.Artist.loadTexture;

import org.newdawn.slick.opengl.Texture;

public enum ProjectileType {

	Bullet("burst", 16, 16, 600, false, 5, 1),
	Laser("laser", 32, 32, 1200, true, 1, 10),
	PoweredLaser("laser_powered", 32, 32, 1200, true, 1, 2);
	
	public boolean hasCollision;
	public float purgeTime, speed;
	public int height, hitbox, width;
	public Texture texture;
	
	ProjectileType(String textureName, int width, int height, float speed, boolean piercing, float purgeTime, int hitbox) {
		this.texture = loadTexture("proj", textureName);
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.hasCollision = piercing;
		this.purgeTime = purgeTime;
		this.hitbox = hitbox;
	}
}
