package data;

public enum Direction {

	Null(0),
	Up(3),
	Right(4),
	Down(1),
	Left(2);
	
	private int opposite;
	
	Direction(int opposite) {
		this.opposite = opposite;
	}
	
	public Direction getOpposite() {
		return Direction.values()[opposite];
	}
}
