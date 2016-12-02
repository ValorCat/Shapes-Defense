package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class Wave {

	private float timeBetweenEnemies;
	private int totalEnemies, aliveEnemies, slainEnemies, finishedEnemies, unspawnedEnemies;
	private CopyOnWriteArrayList<Enemy> enemies;
	private ArrayList<Integer> queue, queueQuantities;
	
	public Wave(float timeBetweenEnemies, Integer[] queue, Integer[] queueQuantities) {
		this.timeBetweenEnemies = timeBetweenEnemies;
		this.queue = new ArrayList<Integer>(Arrays.asList(queue));
		this.queueQuantities = new ArrayList<Integer>(Arrays.asList(queueQuantities));
		
		// find sum of queueQuantities
		for (int i = 0; i < queueQuantities.length; i++) {
			this.totalEnemies += queueQuantities[i];
		}
		this.unspawnedEnemies = totalEnemies;
		
		this.aliveEnemies = 0;
		this.enemies = new CopyOnWriteArrayList<Enemy>();
		this.finishedEnemies = 0;
		this.slainEnemies = 0;
	}
	
	public void update() {
		for (Enemy enemy : enemies) {
			enemy.update();
			if (!enemy.isAlive()) {
				enemies.remove(enemy);
				slainEnemies++;
				aliveEnemies--;
			} else if (enemy.isFinished()) {
				enemies.remove(enemy);
				Player.updateEnergy(-enemy.getHealth() * Player.ENEMY_ESCAPE_ENERGY_LOSS_MOD - 0.05f);
				finishedEnemies++;
				aliveEnemies--;
			}
		}
	}
	
	public void spawnNextEnemy(Tile startTile, Direction dir) {
		// create the enemy object
		Enemy enemy = null;
		switch (queue.get(0)) {
		case 0:
			enemy = new EnemyGreen(startTile, dir);
			break;
		case 1:
			enemy = new EnemyBlue(startTile, dir);
			break;
		case 2:
			enemy = new EnemyYellow(startTile, dir);
			break;
		}
		enemy.randomize();
		enemies.add(enemy);
		aliveEnemies++;
		unspawnedEnemies--;
		
		// update queueQuantities
		queueQuantities.set(0, queueQuantities.get(0) - 1);
		if (queueQuantities.get(0) <= 0) {
			queue.remove(0);
			queueQuantities.remove(0);
		}
	}

	public void removeEnemy(EnemyGreen e) {
		enemies.remove(e);
	}
	
	public boolean isComplete() {
		return unspawnedEnemies == 0 && enemies.size() == 0;
	}
	
	public int getTotalCount() {
		return totalEnemies;
	}
	
	public int getAliveCount() {
		return aliveEnemies;
	}

	public int getSlainCount() {
		return slainEnemies;
	}

	public int getSpawnedCount() {
		return aliveEnemies + slainEnemies + finishedEnemies;
	}
	
	public int getUnspawnedCount() {
		return unspawnedEnemies;
	}

	public float getTimeBetweenEnemies() {
		return timeBetweenEnemies;
	}

	public void setTimeBetweenEnemies(float timeBetweenEnemies) {
		this.timeBetweenEnemies = timeBetweenEnemies;
	}

	public CopyOnWriteArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public ArrayList<Integer> getQueue() {
		return queue;
	}

	public ArrayList<Integer> getQueueQuantities() {
		return queueQuantities;
	}
}
