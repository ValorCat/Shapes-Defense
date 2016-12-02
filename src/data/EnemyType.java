package data;

import org.newdawn.slick.opengl.Texture;

import util.Artist;

public enum EnemyType {

	Green ("green",  64, 64, 1.6f, 0.006f, 55,  2,  0.05f),
	Blue  ("blue",   64, 64, 3.7f, 0.08f,  75,  6,  0.03f),
	Yellow("yellow", 64, 64, 9.5f, 0f,     135, 10, 0.08f);
	
	public float health, healthRegen, lootChance, reward, speed;
	public int height, width;
	public Texture texture;
	
	EnemyType(String textureName, int width, int height, float health, float healthRegen, float speed, float reward, float lootChance) {
		this.texture = Artist.loadTexture("enemy", textureName);
		this.width = width;
		this.height = height;
		this.health = health;
		this.healthRegen = healthRegen;
		this.speed = speed;
		this.reward = reward;
		this.lootChance = lootChance;
	}
}
