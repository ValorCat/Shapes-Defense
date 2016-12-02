package data;

import static util.Artist.loadTexture;

import org.newdawn.slick.opengl.Texture;

public enum TowerType {

	Circle  (new String[] {"circle", "eye_on", "eye_off"},           0.7f,  200, 1.2f),
	Triangle(new String[] {"triangle", "laser_meter"},               0.08f, 225, 0.5f),
	Square  (new String[] {"square", "pulse_meter", "diamond_mask"}, 0.35f, 130, 2.4f);
	
	public float attackSpeed, damage;
	public int range;
	public Texture[] textures;
	
	TowerType(String[] textureNames, float damage, int range, float attackSpeed) {
		this.textures = new Texture[textureNames.length];
		for (int i = 0; i < textureNames.length; i++) {
			this.textures[i] = loadTexture("tower", textureNames[i]);
		}
		this.damage = damage;
		this.range = range;
		this.attackSpeed = attackSpeed;
	}
}
