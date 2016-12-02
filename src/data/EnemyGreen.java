package data;

public class EnemyGreen extends Enemy {

	public EnemyGreen(Tile startTile, Direction direction) {
		super(startTile.getX(), startTile.getY(), direction);
		updateType(EnemyType.Green);
	}
	
}