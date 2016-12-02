package data;

public enum TileType {

	Blue("blue", false, true, false),
	Gray("gray", true, false, false),
	Red("red", false, false, false),
	Finish("gray", true, false, true),
	Blank("red", false, false, false);
	 
	public boolean passable, buildable, finish;
	public String textureName;
	
	TileType(String textureName, boolean passable, boolean buildable, boolean finish) {
		this.textureName = textureName;
		this.passable = passable;
		this.buildable = buildable;
		this.finish = finish;
	}
}
