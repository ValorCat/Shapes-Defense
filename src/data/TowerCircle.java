package data;

import util.Artist;

public class TowerCircle extends Tower implements Entity {
	
	public TowerCircle(TowerType type, Tile tile) {
		super(type, tile);
	}

	@Override
	public void draw() {
		super.draw();
		if (hasTarget())
			Artist.drawRotatedObject(getTextures()[1], getX(), getY(), getAngle());
		else
			Artist.drawRotatedObject(getTextures()[2], getX(), getY(), getAngle());
	}
	
	@Override
	public void attack() {
		float x = getX() + (getWidth() - ProjectileType.Bullet.width) / 2;
		float y = getY() + (getHeight() - ProjectileType.Bullet.height) / 2;
		loadProjectile(new ProjectileBullet(x, y, getDamage(), getAngle(), getTarget()));
	}
	
	@Override
	public void levelUp() {
		setDamage(getDamage() * 1.2f);
		setRange((int) (getRange() * 1.05f));
		setAttackSpeed(getAttackSpeed() * 0.9f);
	}
}
