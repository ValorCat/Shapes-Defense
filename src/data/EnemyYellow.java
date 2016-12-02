package data;

public class EnemyYellow extends Enemy {

	public EnemyYellow(Tile startTile, Direction direction) {
		super(startTile.getX(), startTile.getY(), direction);
		updateType(EnemyType.Yellow);
	}

	@Override
	public void setSpeedModifier(float speedModifier) {
		if (speedModifier >= 1)
			super.setSpeedModifier(speedModifier);
	}
}
