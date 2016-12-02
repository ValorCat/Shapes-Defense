package data;

import java.util.Random;

public class EnemyBlue extends Enemy {

	public EnemyBlue(Tile startTile, Direction direction) {
		super(startTile.getX(), startTile.getY(), direction);
		updateType(EnemyType.Blue);
	}

	@Override
	public void die(boolean giveExp) {
		super.die(giveExp);
		Random rand = new Random();
		if (rand.nextFloat() <= 0.03f) {
			Player.addTowerXPOrb(getX() + rand.nextInt(getWidth()), getY() + rand.nextInt(getHeight()));
		}
	}
}
