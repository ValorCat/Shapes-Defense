package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import util.Clock;

public class WaveManager {
	
	private boolean betweenWaves, wavesComplete;
	private float enemySpawnTimer, timeSinceWaveEnded, timeBetweenWaves, waveTime;
	private int totalWaveCount, waveNumber;
	private BufferedReader waveLoader;
	private String path;
	private static Wave currentWave;
	
	public WaveManager(float timeBetweenWaves) {
		this.timeBetweenWaves = timeBetweenWaves;
		
		this.betweenWaves = true;
		this.enemySpawnTimer = 0;
		this.path = "src/waves.txt";
		this.timeSinceWaveEnded = 0;
		this.wavesComplete = false;
		this.waveNumber = 0;
		this.waveTime = 0;

		try {
			this.waveLoader = new BufferedReader(new FileReader(path));
			this.setTotalWaveCount(Integer.parseInt(waveLoader.readLine()));
			waveLoader.readLine();
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		if (Clock.paused)
			return;
		
		if (betweenWaves) {
			timeSinceWaveEnded += Clock.delta();
			if (timeSinceWaveEnded >= timeBetweenWaves && Player.exp != 100) {
				launchWave();
			}
		} else if (currentWave.isComplete()) {
			endWave();
		} else {
			updateWave();
		}
	}
	
	private Wave loadWave() {
		// load next 4 lines from file
		String line1 = "", line2 = "", line3 = "";
		try {
			line1 = waveLoader.readLine(); // wave number, spawn interval
			line2 = waveLoader.readLine(); // enemy types
			line3 = waveLoader.readLine(); // enemy quantities
			waveLoader.readLine();         // blank line
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (line1 == null) {
			// game complete
			wavesComplete = true;
			System.out.println("Waves complete");
			return null;
		}
		
		// get timeBetweenEnemies
		float timeBetweenEnemies = Float.parseFloat(line1.substring(5));
		
		// get queue
		String[] enemyTypesStr = line2.split(", ");
		int length = enemyTypesStr.length;
		Integer[] queue = new Integer[length];
		for (int i = 0; i < length; i++) {
			queue[i] = Integer.parseInt(enemyTypesStr[i]);
		}
		
		// get queueQuantities
		String[] quantitiesStr = line3.split(", ");
		Integer[] queueQuantities = new Integer[length];
		for (int i = 0; i < length; i++) {
			queueQuantities[i] = Integer.parseInt(quantitiesStr[i]);
		}
		
		return new Wave(timeBetweenEnemies, queue, queueQuantities);
	}
	
	private void updateWave() {
		if (currentWave.getUnspawnedCount() > 0) {
			enemySpawnTimer += Clock.delta();
			if (enemySpawnTimer >= currentWave.getTimeBetweenEnemies()) {
				enemySpawnTimer = 0;
				spawnNextEnemy();
			}
		}
		currentWave.update();
		waveTime += Clock.delta();
	}
	
	private void launchWave() {
		betweenWaves = false;
		timeSinceWaveEnded = 0;
		currentWave = loadWave();
		if (wavesComplete) {
			return;
		}
		spawnNextEnemy();
		System.out.println("Starting wave " + (waveNumber + 1));
	}
	
	private void endWave() {
		System.out.println("Defeated " + currentWave.getSlainCount() + " enemies in " + Math.round(waveTime) + " seconds");
		betweenWaves = true;
		waveNumber++;
		Player.updateEnergy(2);
		Player.updateExp(Player.WAVE_COMPLETE_EXP);
		Player.updateEnergyModifier(Player.WAVE_COMPLETE_ENERGY_MOD);
		Player.updateExpModifier(Player.WAVE_COMPLETE_EXP_MOD);
		enemySpawnTimer = 0;
		waveTime = 0;
	}
	
	private void spawnNextEnemy() {
		currentWave.spawnNextEnemy(Game.grid.getStartTile(), Game.grid.getStartDirection());
	}
	
	public Wave getCurrentWave() {
		return currentWave;
	}
	
	public int getWaveNumber() {
		return waveNumber;
	}
	
	public boolean isBetweenWaves() {
		return betweenWaves;
	}
	
	public static CopyOnWriteArrayList<Enemy> getEnemies() {
		if (currentWave != null)
			return currentWave.getEnemies();
		else
			return new CopyOnWriteArrayList<Enemy>();
	}

	public int getTotalWaveCount() {
		return totalWaveCount;
	}

	public void setTotalWaveCount(int totalWaveCount) {
		this.totalWaveCount = totalWaveCount;
	}
}
