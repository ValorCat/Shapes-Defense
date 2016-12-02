package data;

public class ProjectileLaser extends Projectile implements Entity {

	private boolean untouched;
	private float primaryDamage, secondaryDamage;
	
	public ProjectileLaser(float x, float y, float damage, float angle, Enemy target) {
		super(x, y, angle, target);
		this.primaryDamage = damage;
		this.secondaryDamage = damage * 0.85f;
		this.type = ProjectileType.Laser;
		setTexture(type.texture);
		setWidth(type.width);
		setHeight(type.height);
		setSpeed(type.speed);
		setHasCollision(type.hasCollision);
		setPurgeTime(type.purgeTime);
		setHitbox(type.hitbox);
		
		this.untouched = true;
	}

	@Override
	public void onCollision(Enemy enemy) {
		float damage = untouched ? primaryDamage : secondaryDamage;
		enemy.takeDamage(damage);
		untouched = false;
		if (!enemy.isAlive())
			addExp(1);
	}
}
