package data;

import org.newdawn.slick.opengl.Texture;

import util.Artist;
import util.Clock;

public abstract class Projectile implements Entity {

	private boolean alive, hasCollision;
	private float angle, exp, lifetime, purgeTime, speed, x, xVelocity, y, yVelocity;
	private int h, hitbox, w;
	private Enemy target;
	public ProjectileType type;
	private Texture texture;
	
	public Projectile(float x, float y, float angle, Enemy target) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.target = target;
		
		this.alive = true;
		this.exp = 0;
		this.h = 0;
		this.hasCollision = false;
		this.hitbox = 0;
		this.lifetime = 0;
		this.purgeTime = -1;
		this.speed = 0;
		this.texture = null;
		this.w = 0;
		this.xVelocity = 0;
		this.yVelocity = 0;
		
		calculateVelocity();
	}
	
	public void update() {
		// autokilling
		lifetime += Clock.delta();
		if (lifetime >= purgeTime && purgeTime != -1) {
			alive = false;
			return;
		}
		
		// direction and movement
		float mod = speed * Clock.delta();
		x += xVelocity * mod;
		y += yVelocity * mod;
		draw();
		
		// enemy collision
		for (Enemy e : WaveManager.getEnemies()) {
			float enemyHitbox = (e.getWidth() + e.getHeight()) / 4;
			if (Artist.isCollision(x + hitbox, y + hitbox, w - hitbox, h - hitbox,
					e.getX() + enemyHitbox, e.getY() + enemyHitbox, e.getWidth() - enemyHitbox, e.getHeight() - enemyHitbox)) {
				onCollision(e);
				if (!hasCollision) {
					alive = false;
					break;
				}
			}
		}
	}

	public void draw() {
		Artist.drawRotatedObject(texture, x, y, 32, 32, angle);
	}
	
	public abstract void onCollision(Enemy enemy);
	
	private void calculateVelocity() {
		float xDistance = Math.abs(target.getX() + target.getWidth()/2 - (x + w/2));
		float yDistance = Math.abs(target.getY() + target.getHeight()/2 - (y + h/2));
		xVelocity = xDistance / (xDistance + yDistance);
		yVelocity = 1 - xVelocity;
		
		// moving left/down
		if (target.getX() < x)
			xVelocity *= -1;
		if (target.getY() < y)
			yVelocity *= -1;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean hasCollision() {
		return hasCollision;
	}

	public void setHasCollision(boolean hasCollision) {
		this.hasCollision = hasCollision;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public int getHitboxt() {
		return hitbox;
	}

	public void setHitbox(int hitbox) {
		this.hitbox = hitbox;
	}

	public float getPurgeTime() {
		return purgeTime;
	}

	public void setPurgeTime(float purgeTime) {
		this.purgeTime = purgeTime;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
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

	public Enemy getTarget() {
		return target;
	}

	public void setTarget(EnemyGreen target) {
		this.target = target;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public float getLifetime() {
		return lifetime;
	}

	public float getExp() {
		return exp;
	}

	public void setExp(float exp) {
		this.exp = exp;
	}
	
	public void addExp(float exp) {
		this.exp += exp;
	}
}
