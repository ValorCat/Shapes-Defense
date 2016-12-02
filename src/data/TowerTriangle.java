package data;

import util.Artist;
import util.Clock;

public class TowerTriangle extends Tower implements Entity {
	
	private float timeSinceLastAttack;
	private int attacksSincePowerUp, attacksToPowerUp;
	
	public TowerTriangle(TowerType type, Tile tile) {
		super(type, tile);
		this.attacksSincePowerUp = 0;
		this.attacksToPowerUp = 15;
		this.timeSinceLastAttack = 0;
	}
	
	@Override
	public void update() {
		super.update();
		timeSinceLastAttack += Clock.delta();
		if (timeSinceLastAttack > 5 && attacksSincePowerUp > 0) {
			attacksSincePowerUp--;
		}
	}
	
	@Override
	public void draw() {
		super.draw();
		float xOffset = getX() + getWidth()/2 - 3;
		float height = (float) (attacksSincePowerUp * -36 / attacksToPowerUp);
		Artist.drawObject(getTextures()[1], xOffset, getY() + 54, 6, height);
	}
	
	@Override
	public void attack() {
		Projectile projectile;
		float x = getX() + (getWidth() - ProjectileType.Laser.width) / 2;
		float y = getY() + (getHeight() - ProjectileType.Laser.height) / 2;
		if (attacksSincePowerUp < attacksToPowerUp) {
			projectile = new ProjectileLaser(x, y, getDamage(), getAngle(), getTarget());
			attacksSincePowerUp++;
		} else {
			projectile = new ProjectilePoweredLaser(x, y, getDamage() * 5, getAngle(), getTarget());
			attacksSincePowerUp = 0;
		}
		loadProjectile(projectile);
		timeSinceLastAttack = 0;
	}
	
	@Override
	public void levelUp() {
		setDamage(getDamage() * 1.2f);
		setRange((int) (getRange() * 1.05f));
	}
}
