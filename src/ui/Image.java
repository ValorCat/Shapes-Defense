package ui;

import org.newdawn.slick.opengl.Texture;

public class Image {

	private float x, y;
	private int h, w;
	private String name;
	private Texture texture;
	
	public Image(String name, Texture texture, float x, float y) {
		this.name = name;
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.w = texture.getImageWidth();
		this.h = texture.getImageHeight();
	}

	public Image(String name, Texture texture, int x, int y, int w, int h) {
		this.name = name;
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
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
}
