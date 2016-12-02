package data;

import static util.Artist.TILE_SIZE;
import static util.Artist.drawObject;
import static util.Artist.drawRotatedObject;
import static util.Artist.loadTexture;
import static util.Clock.delta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.opengl.Texture;

import util.Clock;

public abstract class Enemy implements Entity {

	private boolean alive, firstUpdate;
	private float areaDamageResist, health, healthRegen, heartChance, maxHealth, reward, rotation, speed, speedModifier, x, y;
	private int h, updatesSinceCentered, w;
	private Direction direction;
	public EnemyType type;
	private Texture healthBar, texture;
	
	public Enemy(float x, float y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;

		this.alive = true;
		this.areaDamageResist = 1;
		this.firstUpdate = true;
		this.h = 0;
		this.health = 1;
		this.healthBar = loadTexture("misc", "bar_green");
		this.healthRegen = 0;
		this.heartChance = 0;
		this.maxHealth = 1;
		this.reward = 0;
		this.rotation = 0;
		this.speed = 0;
		this.speedModifier = 1;
		this.updatesSinceCentered = 0;
		this.w = 0;
	}
	
	public void updateType(EnemyType type) {
		this.type = type;
		this.texture = type.texture;
		this.w = type.width;
		this.h = type.height;
		this.health = type.health;
		this.healthRegen = type.healthRegen;
		this.heartChance = type.lootChance;
		this.maxHealth = type.health;
		this.reward = type.reward;
		this.speed = type.speed;
	}
	
	public void randomize() {
		Random rand = new Random();
		
		// health
		int healthMod = (rand.nextInt(21) + 10) / 100;
		if (rand.nextBoolean()) {
			maxHealth *= 1 + healthMod;
		} else {
			maxHealth *= 1 - healthMod;
		}
		health = maxHealth;
		
		// speed
		int speedMod = (rand.nextInt(21) + 10) / 100;
		if (rand.nextBoolean()) {
			speed *= 1 + speedMod;
		} else {
			speed *= 1 - speedMod;
		}
	}
	
	public void update() {
		// startup lag spike
		if (firstUpdate) {
			firstUpdate = false;
			return;
		}
		
		if (direction != Direction.Null) {
			// change direction
			if (isCenteredOnTile()) {
				updatesSinceCentered = 0;
				changeDirection();
			}
			
			// move
			if (getCurrentTile().getType().passable) {
				move();
			} else {
				direction = Direction.Null;
			}
			
			// recover health
			if (health < maxHealth) {
				health += healthRegen * Clock.delta();
				if (health > maxHealth)
					health = maxHealth;
			}
		} else {
			takeDamage(Clock.delta()/20);
		}
		
		// recover speed if slowed
		if (speedModifier < 1) {
			speedModifier *= Math.max(1, 0.999f + Clock.delta());
			if (speedModifier > 1)
				speedModifier = 1;
		}
		
		// reduce area damage resistance
		if (areaDamageResist > 1) {
			areaDamageResist *= 1 - Clock.delta()/10;
			if (areaDamageResist < 1)
				areaDamageResist = 1;
		}
		
		updatesSinceCentered++;
		draw();
	}
	
	public void draw() {
		drawRotatedObject(texture, x, y, w, h, rotation);
		rotation += speed * speedModifier / 120;
		if (health > 0) {
			float healthPercent = health / maxHealth;
			float xOffset = (1 - healthPercent) * TILE_SIZE / 2;
			float width = healthPercent * TILE_SIZE;
			drawObject(healthBar, x + xOffset, y - 12, width, 5);
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	private void move() {
		float amount = Math.abs(delta() * speed * speedModifier);
		switch (direction) {
		case Up:
			y -= amount;
			break;
		case Down:
			y += amount;
			break;
		case Left:
			x -= amount;
			break;
		case Right:
			x += amount;
			break;
		}
	}
	
	public void takeDamage(float damage) {
		health -= damage;
		if (health < 0)
			die(true);
	}
	
	public void takeDamageNoExp(float damage) {
		health -= damage;
		if (health < 0)
			die(false);
	}
	
	public void takeAreaDamage(float damage) {
		takeDamage(damage / areaDamageResist);
		areaDamageResist *= 1.5;
		if (areaDamageResist > 5)
			areaDamageResist = 5;
	}
	
	public void die(boolean giveExp) {
		alive = false;
		Player.updateEnergy(0.3f);
		if (giveExp)
			Player.updateExp(reward);
		
		Random rand = new Random();
		if (rand.nextFloat() <= heartChance) {
			Player.addHeart(x + rand.nextInt(w), y + rand.nextInt(h));
		}
	}
	
	private void changeDirection() {
		// create randomly ordered list of directions
		List<Direction> directions = Arrays.asList(Direction.values());
		Collections.shuffle(directions, new Random());
		
		// default direction
		Direction opposite = direction.getOpposite();
		direction = Direction.Null;
		
		// pick a safe direction
		for (Direction dir : directions) {
			if (canMoveInDirection(dir) && dir != opposite) {
				direction = dir;
				break;
			}
		}
	}
	
	private boolean canMoveInDirection(Direction dir) {	
		int xTile = getXTile(), yTile = getYTile();
		Tile nextTile;
		switch (dir) {
		case Up:
			nextTile = Game.grid.getTile(xTile, yTile - 1);
			break;
		case Down:
			nextTile = Game.grid.getTile(xTile, yTile + 1);
			break;
		case Left:
			nextTile = Game.grid.getTile(xTile - 1, yTile);
			break;
		case Right:
			nextTile = Game.grid.getTile(xTile + 1, yTile);
			break;
		default:
			return false;
		}
		
		return nextTile.getType().passable;
	}
	
	private boolean isCenteredOnTile() {
		// ensure we don't return true too many times
		if (updatesSinceCentered < 350 / (speed * speedModifier))
			return false;
		
		// check if centered
		int margin = 2;
		int xOffset = Math.abs((int) x % TILE_SIZE);
		int yOffset = Math.abs((int) y % TILE_SIZE);
		return xOffset <= margin && yOffset <= margin;
	}
	
	public boolean isFinished() {
		return getCurrentTile().getType().finish;
	}
	
	public Tile getCurrentTile() {
		return Game.grid.getTile(getXTile(), getYTile());
	}
	
	public int getXTile() {
		return (int) Math.floor((x + TILE_SIZE/2) / TILE_SIZE);
	}
	
	public int getYTile() {
		return (int) Math.floor((y + TILE_SIZE/2) / TILE_SIZE);
	}
	
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeedModifier() {
		return speedModifier;
	}

	public void setSpeedModifier(float speedModifier) {
		this.speedModifier = speedModifier;
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
	
	public Texture getHealthBarTexture() {
		return healthBar;
	}

	public void setHealthBarTexture(Texture healthBar) {
		this.healthBar = healthBar;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Direction getDirection() {
		return direction;
	}
}
