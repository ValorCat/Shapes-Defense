package ui;

import org.newdawn.slick.opengl.Texture;

public class Button {

	private boolean highlight;
	private int x, y, w, h;
	private String name;
	private Texture texture;

	public Button(String name, Texture texture, int x, int y, boolean highlight) {
		this.name = name;
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.setHighlight(highlight);
		this.w = texture.getImageWidth();
		this.h = texture.getImageHeight();
	}
	
	public Button(String name, Texture texture, int x, int y, int w, int h, boolean highlight) {
		this.name = name;
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.setHighlight(highlight);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}
}
