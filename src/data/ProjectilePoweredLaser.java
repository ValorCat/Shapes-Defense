package data;

public class ProjectilePoweredLaser extends Projectile implements Entity {
	
	private float damage;
	
	public ProjectilePoweredLaser(float x, float y, float damage, float angle, Enemy target) {
		super(x, y, angle, target);
		this.damage = damage;
		this.type = ProjectileType.PoweredLaser;
		setTexture(type.texture);
		setWidth(type.width);
		setHeight(type.height);
		setSpeed(type.speed);
		setHasCollision(type.hasCollision);
		setPurgeTime(type.purgeTime);
		setHitbox(type.hitbox);
	}

	@Override
	public void onCollision(Enemy enemy) {
		enemy.takeDamage(damage);
		if (!enemy.isAlive())
			addExp(3);
	}
}
