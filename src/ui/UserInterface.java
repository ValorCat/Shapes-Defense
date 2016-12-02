package ui;

import static util.Artist.HEIGHT;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import util.Artist;
import util.Clock;

public class UserInterface {

	private ArrayList<Button> buttons;
	private ArrayList<Image> images;
	private Texture buttonOverlay;
	
	public UserInterface() {
		this.buttons = new ArrayList<Button>();
		this.images = new ArrayList<Image>();
		
		this.buttonOverlay = Artist.loadTexture("gui", "btn_overlay");
	}
	
	public void draw() {
		for (Image i : images) {
			Artist.drawObject(i.getTexture(), i.getX(), i.getY(), i.getWidth(), i.getHeight());
		}
		for (Button b : buttons) {
			Artist.drawObject(b.getTexture(), b.getX(), b.getY(), b.getWidth(), b.getHeight());
			if (!Clock.paused && b.isHighlight() && isButtonHoveredOn(b.getName()))
				Artist.drawObject(buttonOverlay, b.getX(), b.getY(), b.getWidth(), b.getHeight());
		}
	}
	
	public void addButton(String name, String texture, int x, int y, boolean highlight) {
		buttons.add(new Button(name, Artist.loadTexture("gui", texture), x, y, highlight));
	}
	
	public void addImage(String name, String texture, int x, int y) {
		images.add(new Image(name, Artist.loadTexture("gui", texture), x, y));
	}
	
	public boolean isButtonHoveredOn(String name) {
		Button button = getButton(name);
		if (button != null) {
			float mouseX = Mouse.getX();
			float mouseY = HEIGHT - Mouse.getY() - 1;
			float buttonXEnd = button.getX() + button.getWidth();
			float buttonYEnd = button.getY() + button.getHeight();
			boolean xInRange = mouseX >= button.getX() && mouseX <= buttonXEnd;
			boolean yInRange = mouseY >= button.getY() && mouseY <= buttonYEnd;
			return xInRange && yInRange;
		} else {
			return false;
		}
	}
	
	public Button getButton(String name) {
		Button button = null;
		for (Button b : buttons) {
			if (b.getName().compareTo(name) == 0) {
				button = b;
				break;
			}
		}
		return button;
	}
}
