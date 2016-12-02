package data;

import static util.Artist.TILE_SIZE;
import static util.Artist.WIDTH;
import static util.Artist.loadTexture;

import java.awt.Font;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import util.Artist;
import util.Clock;
import util.Input;

public class Player {

	private final float ENERGY_METER_INCREMENT = 0.4f;
	private final float EXP_METER_INCREMENT = 2;
	private final float NATURAL_ENERGY_LOSS = 0.25f;
	private final float NATURAL_EXP_GAIN = 0.28f;
	
	public final static float ENEMY_ESCAPE_ENERGY_LOSS_MOD = 6;
	public final static float TOWER_LEVEL_UP_EXP_MOD = 1.9f;
	public final static float WAVE_COMPLETE_EXP = 6;
	public final static float WAVE_COMPLETE_ENERGY_MOD = 0.94f;
	public final static float WAVE_COMPLETE_EXP_MOD = 0.85f;
	
	public static float energy, energyModifier, exp, expModifier;
	public static ArrayList<Tower> towers;
	public static CopyOnWriteArrayList<LootHeart> hearts;
	public static CopyOnWriteArrayList<LootTowerXP> towerXPOrbs;
	
	private float attackDamage, attackDrawSize, attackDrawTime, attackRange, attackRotation, attackX, attackY, drawnEnergy, drawnExp, initAttackDrawTime;
	private int selectedTowerIDToPlace;
	private CopyOnWriteArrayList<Enemy> enemies;
	private CopyOnWriteArrayList<ShredderOrb> shredders;
	private String[] towerSelectorItems;
	private Texture attackEffect, energyMeter, expMeter, expMeterFull, tileHighlight, towerExpMeter, towerHighlight, towerRange, towerRangeSmall, towerTypeHighlight;
	private Tile highlightedTile;
	private Tower selectedTower;
	private TrueTypeFont levelFont;
	
	public Player() {		
		Player.energy = 50;
		Player.energyModifier = 1;
		Player.exp = 100;
		Player.expModifier = 1;
		Player.hearts = new CopyOnWriteArrayList<LootHeart>();
		Player.towers = new ArrayList<Tower>();
		Player.towerXPOrbs = new CopyOnWriteArrayList<LootTowerXP>();
		
		this.attackDamage = 0.15f;
		this.attackDrawSize = 20;
		this.attackDrawTime = 0;
		this.attackEffect = loadTexture("misc", "bubble");
		this.attackRange = 30;
		this.attackRotation = 0;
		this.drawnEnergy = 0;
		this.drawnExp = 100;
		this.enemies = new CopyOnWriteArrayList<Enemy>();
		this.energyMeter = loadTexture("misc", "bar_red");
		this.expMeter = loadTexture("misc", "bar_cyan");
		this.expMeterFull = loadTexture("misc", "bar_purple");
		this.highlightedTile = null;
		this.initAttackDrawTime = 0.2f;
		this.levelFont = new TrueTypeFont(new Font("Consolas", Font.BOLD, 19), true);
		this.selectedTowerIDToPlace = 0;
		this.shredders = new CopyOnWriteArrayList<ShredderOrb>();
		this.tileHighlight = loadTexture("gui", "highlight_green");
		this.towerExpMeter = loadTexture("misc", "bar_blue");
		this.towerHighlight = loadTexture("gui", "highlight_blue");
		this.towerRange = loadTexture("gui", "range");
		this.towerRangeSmall = loadTexture("gui", "range_small");
		this.towerSelectorItems = new String[] {"circle", "triangle", "square", "shredder"};
		this.towerTypeHighlight = loadTexture("gui", "highlight_red");
	}
	
	public void update() {
		// get the currently highlighted tile
		int mouseXTile = (int) (Input.getMouseX() / TILE_SIZE);
		int mouseYTile = (int) (Input.getMouseY() / TILE_SIZE);
		highlightedTile = Game.grid.getTile(mouseXTile, mouseYTile);
		
		// energy and experience
		if (!Game.waves.isBetweenWaves() && energy > 0) {
			energy -= NATURAL_ENERGY_LOSS * Clock.delta();
			exp += NATURAL_EXP_GAIN * Clock.delta();
		}
		
		drawnEnergy = calculateMeterValue(energy, drawnEnergy, ENERGY_METER_INCREMENT);
		drawnExp = calculateMeterValue(exp, drawnExp, EXP_METER_INCREMENT);
		
		if (!Clock.paused) {
			// highlight tiles before drawing towers
			if (highlightedTile != null /*&& getHoveredOnTowerSelectorItem() < 0*/)
				Artist.drawObject(tileHighlight, highlightedTile.getX(), highlightedTile.getY());
			if (selectedTower != null)
				Artist.drawObject(towerHighlight, selectedTower.getX(), selectedTower.getY());
			
			// update children
			updateTowers();
			updateShredders();
			updateLoot();
			draw();
			
			if (attackDrawTime > 0) {
				attackDrawTime -= Clock.delta();
			}
			
			if (Input.leftMouseButtonDown() && !selectTowerToPlace()) {
				Tower newTower = selectTower();
				if (newTower != null) {
					if (newTower == selectedTower)
						selectedTower = null;
					else
						selectedTower = newTower;
				} else if (attackDrawTime <= 0) {
					attackX = Input.getMouseX();
					attackY = Input.getMouseY();
					attack();
				}
			}
			
			if (Input.isRightMouseButtonDown()) {
				placeItem();
			}
		}
		
		// handle key commands
		for (Integer key : Input.getKeysDown()) {
			switch (key) {
			case Keyboard.KEY_SPACE:
				Clock.pause();
				break;
			case Keyboard.KEY_UP:
				if (selectedTowerIDToPlace > 0)
					selectedTowerIDToPlace--;
				break;
			case Keyboard.KEY_DOWN:
				if (selectedTowerIDToPlace < towerSelectorItems.length - 1)
					selectedTowerIDToPlace++;
				break;
			case Keyboard.KEY_RIGHT:
				Clock.changeSpeed(0.25f);
				break;
			case Keyboard.KEY_LEFT:
				Clock.changeSpeed(-0.25f);
				break;
			case Keyboard.KEY_RSHIFT:
				Clock.speed = (Clock.speed() == 0) ? 1 : 0;
				break;
			}
		}
	}
	
	public void draw() {
		// highlight selected tower
		if (selectedTower != null) {
			drawSelectedTower();
		}
		
		// highlight selected tower type (in selector)
		Artist.drawObject(towerTypeHighlight, WIDTH - TILE_SIZE, (selectedTowerIDToPlace + 1) * TILE_SIZE);
		
		// draw meters
		float offset = 16;
		float widthWhenFull = WIDTH - offset*2;
		float energyWidth = drawnEnergy / 100 * widthWhenFull;
		float expWidth = drawnExp / 100 * widthWhenFull;
		Texture expTexture = (exp >= 100) ? expMeterFull : expMeter;
		Artist.drawObject(energyMeter, offset, 14, energyWidth, 12);
		Artist.drawObject(expTexture, offset, 32, expWidth, 12);
		
		// draw attack
		if (attackDrawTime > 0) {
			float n = (initAttackDrawTime - attackDrawTime) / initAttackDrawTime;
			float x = attackX - attackDrawSize*n;
			float y = attackY - attackDrawSize*n;
			float w = attackDrawSize*2 * n;
			Artist.drawRotatedObject(attackEffect, x, y, w, w, attackRotation);
			attackRotation += 5;
		}
	}
	
	private void drawSelectedTower() {
		float x = selectedTower.getX();
		float y = selectedTower.getY();
		float w = selectedTower.getWidth();
		float h = selectedTower.getHeight();
		float range = selectedTower.getRange();
		
		// range
		Texture rangeTexture = (range >= 160) ? towerRange : towerRangeSmall;
		Artist.drawObject(rangeTexture, x + w/2 - range, y + h/2 - range, range*2, range*2);
		
		// level
		if (selectedTower.getLevel() > 1)
			Artist.writeText(levelFont, x+w-6, y+h-6, Integer.toString(selectedTower.getLevel()), Color.blue);
		
		// experience meter
		float expPercent = selectedTower.getExp() / selectedTower.getExpGoal();
		if (expPercent >= 0.05f) {
			float xOffset = (1 - expPercent) * TILE_SIZE / 2;
			float width = expPercent * TILE_SIZE;
			Artist.drawObject(towerExpMeter, x + xOffset, y - 12, width, 5);
		}
	}
	
	private void attack() {
		enemies = WaveManager.getEnemies();
		int enemiesHit = 0;
		Enemy lastEnemy = null;
		for (Enemy enemy : enemies) {
			float dist = Tower.findEnemyDistance(enemy, Input.getMouseX(), Input.getMouseY());
			if (dist <= attackRange) {
				enemy.takeDamageNoExp(attackDamage);
				lastEnemy = enemy;
				enemiesHit++;
			}
		}
		if (enemiesHit > 0) {
			Player.updateEnergy(-0.1f);
			if (selectedTower != null)
				selectedTower.targetEnemy(lastEnemy);
			else
				for (Tower tower : towers)
					tower.targetEnemy(lastEnemy);
		}
		attackDrawTime = initAttackDrawTime;
		attackRotation = 0;
	}
	
	private void updateTowers() {
		for (Tower tower : towers) {
			tower.update();
		}
	}
	
	private void updateShredders() {
		for (ShredderOrb shredder : shredders) {
			if (shredder.isAlive())
				shredder.update();
			else
				shredders.remove(shredder);
		}
	}
	
	private void updateLoot() {
		for (LootHeart heart : hearts) {
			if (heart.isAlive())
				heart.update();
			else
				hearts.remove(heart);
		}
		for (LootTowerXP orb : towerXPOrbs) {
			if (orb.isAlive())
				orb.update();
			else
				towerXPOrbs.remove(orb);
		}
	}
	
	private int getHoveredOnTowerSelectorItem() {
		for (int i = 0; i < towerSelectorItems.length; i++) {
			if (Game.gui.isButtonHoveredOn("select_" + towerSelectorItems[i])) {
				return i;
			}
		}
		return -1;
	}
	
	private Tower selectTower() {
		float x = highlightedTile.getX();
		float y = highlightedTile.getY();
		for (Tower t : towers) {
			if (x == t.getX() && y == t.getY()) {
				return t;
			}
		}
		return null;
	}
	
	private boolean selectTowerToPlace() {
		int button = getHoveredOnTowerSelectorItem();
		if (button >= 0) {
			selectedTowerIDToPlace = button;
			return true;
		}
		return false;
	}
	
	private void placeItem() {
		switch (selectedTowerIDToPlace) {
		case 0:
			placeTower(new TowerCircle(TowerType.Circle, highlightedTile));
			break;
		case 1:
			placeTower(new TowerTriangle(TowerType.Triangle, highlightedTile));
			break;
		case 2:
			placeTower(new TowerSquare(TowerType.Square, highlightedTile));
			break;
		case 3:
			placeOrb(new ShredderOrb(highlightedTile.getX() + TILE_SIZE/4, highlightedTile.getY() + TILE_SIZE/4));
			break;
		}	
	}
	
	private void placeTower(Tower tower) {
		if (exp >= 100 && highlightedTile.getType().buildable) {
			towers.add(tower);
			exp = 0;
		}
	}
	
	private void placeOrb(ShredderOrb orb) {
		if (exp >= 15 && highlightedTile.getType().passable) {
			shredders.add(orb);
			exp -= 15;
		}
	}
	
	private float calculateMeterValue(float value, float drawnValue, float increment) {
		if (Math.abs(value - drawnValue) <= increment)
			return value;
		else if (value > drawnValue)
			return Math.min(drawnValue + increment, 100);
		else
			return Math.max(drawnValue - increment, 0);
	}
	
	public static void updateEnergy(float amount) {
		energy += amount * energyModifier;
		if (energy > 100)
			energy = 100;
		else if (energy < 0)
			energy = 0;
	}
	
	public static void updateExp(float amount) {
		exp += amount * expModifier;
		if (exp > 100)
			exp = 100;
		else if (exp < 0)
			exp = 0;
	}
	
	public static void updateEnergyModifier(float amount) {
		energyModifier *= amount;
	}
	
	public static void updateExpModifier(float amount) {
		expModifier *= amount;
	}
	
	public static void addHeart(float x, float y) {
		hearts.add(new LootHeart(x, y));
	}
	
	public static void addTowerXPOrb(float x, float y) {
		towerXPOrbs.add(new LootTowerXP(x, y));
	}
}
