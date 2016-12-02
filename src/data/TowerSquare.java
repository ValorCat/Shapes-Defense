package data;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.opengl.Texture;

import util.Artist;
import util.Clock;

public class TowerSquare extends Tower {

	private float attackDrawTime, initAttackDrawTime;
	private ArrayList<Integer> enemyIndexes;
	private CopyOnWriteArrayList<Enemy> enemies;
	private Texture attackTexture;
	
	public TowerSquare(TowerType type, Tile tile) {
		super(type, tile);
		this.attackDrawTime = 0;
		this.attackTexture = Artist.loadTexture("misc", "wave2");
		this.enemies = WaveManager.getEnemies();
		this.enemyIndexes = new ArrayList<Integer>();
		this.initAttackDrawTime = 0.3f;
	}
	
	@Override
	public void update() {
		if (canAttack()) {
			enemies = WaveManager.getEnemies();
			float range = getRange();
			enemyIndexes.clear();
			for (int i = 0; i < enemies.size(); i++) {
				if (findEnemyDistance(enemies.get(i), getX(), getY()) <= range)
					enemyIndexes.add(i);
			}
			if (enemyIndexes.size() > 0) {
				attack();
				attackDrawTime = initAttackDrawTime;
				setTimeSinceLastAttack(0);
			}
		}
		if (attackDrawTime > 0) {
			attackDrawTime -= Clock.delta();
		}
		draw();
		float timeSinceLastAttack = getTimeSinceLastAttack();
		if (timeSinceLastAttack < getAttackSpeed())
			setTimeSinceLastAttack(timeSinceLastAttack + Clock.delta());
	}
	
	@Override
	public void draw() {
		super.draw();
		Artist.drawObject(getTextures()[1], getX() + 16, getY() + 48, 32, -31 * (getTimeSinceLastAttack() / getAttackSpeed()));
		Artist.drawObject(getTextures()[2], getX() + 16, getY() + 16, 32, 32);
		if (attackDrawTime > 0) {
			float n = (initAttackDrawTime - attackDrawTime) / initAttackDrawTime;
			float r = getRange();
			float x = getX() + getWidth()/2 - r*n;
			float y = getY() + getHeight()/2 - r*n;
			float w = r*2 * n;
			float h = r*2 * n;
			Artist.drawObject(attackTexture, x, y, w, h);
		}
	}
	
	@Override
	public void attack() {
		float damage = getDamage();
		for (int index : enemyIndexes) {
			Enemy enemy = enemies.get(index);
			enemy.takeAreaDamage(enemy.getMaxHealth() * damage);
			if (!enemy.isAlive())
				updateExp(2);
			else
				updateExp(0.2f);
		}
	}
	
	@Override
	public void levelUp() {
		setRange((int) (getRange() * 1.1f));
		setAttackSpeed(getAttackSpeed() * 0.95f);
	}
}
