package data;

import static util.Artist.TILE_SIZE;
import static util.Artist.drawObject;

import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.opengl.Texture;

import util.Clock;

public abstract class Tower implements Entity {

	private boolean hasTarget;
	private float angle, attackSpeed, damage, exp, expGoal, timeSinceLastAttack, x, y;
	private int h, level, range, w;
	private CopyOnWriteArrayList<Projectile> projectiles;
	private Enemy target;
	private Texture[] textures;

	public Tower(TowerType type, Tile tile) {
		this.textures = type.textures;
		this.damage = type.damage;
		this.range = type.range;
		this.attackSpeed = type.attackSpeed;
		this.x = tile.getX();
		this.y = tile.getY();
		this.w = TILE_SIZE;
		this.h = TILE_SIZE;
		
		this.angle = 0;
		this.exp = 0;
		this.expGoal = 60;
		this.hasTarget = false;
		this.level = 1;
		this.projectiles = new CopyOnWriteArrayList<Projectile>();
		this.timeSinceLastAttack = 0;
	}
	
	public void update() {
		if (hasTarget) {
			angle = calculateAngle();
			if (findEnemyDistance(target, x, y) > range) {
				acquireTarget();
			}
			if (canAttack()) {
				attack();
				setTimeSinceLastAttack(0);
			}
			if (!target.isAlive()) {
				hasTarget = false;
			}
		} else if (WaveManager.getEnemies().size() > 0) {
			acquireTarget();
		}
		draw();
		timeSinceLastAttack += Clock.delta();
		updateProjectiles();
	}

	public void draw() {
		drawObject(textures[0], x, y, w, h);
	}
	
	public void attack() {
		target.takeDamage(damage);
		if (!target.isAlive())
			updateExp(1);
	}
	
	public boolean canAttack() {
		return getTimeSinceLastAttack() >= attackSpeed;
	}
	
	private void acquireTarget() {
		if (WaveManager.getEnemies().size() == 0) {
			hasTarget = false;
		} else {
			Enemy closest = findClosestEnemy();
			if (closest != null) {
				target = closest;
				hasTarget = true;
				return;
			}
		}
		hasTarget = false;
	}
	
	public void targetEnemy(Enemy e) {
		if (findEnemyDistance(e, x, y) <= range) {
			target = e;
			hasTarget = true;
		}
	}
	
	private Enemy findClosestEnemy() {
		Enemy closestEnemy = null;
		float closestDistance = range + 1;
		for (Enemy e : WaveManager.getEnemies()) {
			float distance = findEnemyDistance(e, x, y);
			if (distance <= range && distance < closestDistance)	 {
				closestEnemy = e;
				closestDistance = distance;
			}
		}
		return closestEnemy;
	}
	
	public static float findEnemyDistance(Enemy e, float x, float y) {
		float xDistance = Math.abs(e.getX() + e.getWidth()/2 - x);
		float yDistance = Math.abs(e.getY() + e.getHeight()/2 - y);
		return (float) Math.hypot(xDistance, yDistance);
	}
	
	private float calculateAngle() {
		if (hasTarget) {
			float target_x = target.getX() + target.getWidth()/2;
			float target_y = target.getY() + target.getHeight()/2;
			double radians = Math.atan2(target_y - (y + h/2), target_x - (x + w/2));
			double degrees = Math.toDegrees(radians);
			return (float) degrees - 90;
		} else {
			return angle;
		}
	}
	
	public void loadProjectile(Projectile p) {
		projectiles.add(p);
	}
	
	private void updateProjectiles() {
		for (Projectile p : projectiles) {
			p.update();
			if (!p.isAlive()) {
				updateExp(p.getExp());
				projectiles.remove(p);
			}
		}
	}
	
	public void updateExp(float amount) {
		exp += amount;
		if (exp >= expGoal) {
			levelUp();
			level++;
			expGoal *= Player.TOWER_LEVEL_UP_EXP_MOD;
			exp = 0;
		}
	}
	
	public abstract void levelUp();
	
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
	
	public boolean hasTarget() {
		return hasTarget;
	}
	
	public float getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(float attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getTimeSinceLastAttack() {
		return timeSinceLastAttack;
	}

	public void setTimeSinceLastAttack(float timeSinceLastAttack) {
		this.timeSinceLastAttack = timeSinceLastAttack;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public Texture[] getTextures() {
		return textures;
	}

	public void setTextures(Texture[] textures) {
		this.textures = textures;
	}

	public float getAngle() {
		return angle;
	}

	public float getTimeSinceLastShot() {
		return getTimeSinceLastAttack();
	}
	
	public CopyOnWriteArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public Enemy getTarget() {
		return target;
	}

	public float getExp() {
		return exp;
	}

	public void setExp(float exp) {
		this.exp = exp;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public float getExpGoal() {
		return expGoal;
	}

	public void setExpGoal(float expGoal) {
		this.expGoal = expGoal;
	}
}
