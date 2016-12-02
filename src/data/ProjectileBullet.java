package data;

import java.util.Random;

public class ProjectileBullet extends Projectile implements Entity {
	
	private float damage, speedModifier;
	
	public ProjectileBullet (float x, float y, float damage, float angle, Enemy target) {
		super(x, y, angle, target);
		this.damage = damage;
		this.type = ProjectileType.Bullet;
		setTexture(type.texture);
		setWidth(type.width);
		setHeight(type.height);
		setSpeed(type.speed);
		setHasCollision(type.hasCollision);
		setPurgeTime(type.purgeTime);
		setHitbox(type.hitbox);
		
		this.speedModifier = 0.75f;
	}
	
	@Override
	public void onCollision(Enemy enemy) {
		// damage
		Random rand = new Random();
		float damageMod = rand.nextInt(21);
		if (rand.nextBoolean())
			damageMod *= -1;
		enemy.takeDamage(damage * (1 + damageMod/100));
		
		// slowing
		float enemySpeedMod = enemy.getSpeedModifier();
		if (enemySpeedMod < 1)
			enemy.setSpeedModifier(speedModifier);
		else
			enemy.setSpeedModifier(enemySpeedMod * speedModifier / 2);
		
		// experience
		if (!enemy.isAlive())
			addExp(1);
		else
			addExp(0.1f);
	}
}
