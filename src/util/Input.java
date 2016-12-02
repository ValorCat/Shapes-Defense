
package util;

import static util.Artist.HEIGHT;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {
	
	private static boolean isLeftMouseDown = false, isRightMouseDown = false;
	
	public static void update() {
		isLeftMouseDown = Mouse.isButtonDown(0);
		isRightMouseDown = Mouse.isButtonDown(1);
	}
	
	public static int getMouseX() {
		return Mouse.getX();
	}
	
	public static int getMouseY() {
		return HEIGHT - Mouse.getY() - 1;
	}
	
	public static boolean leftMouseButtonDown() {
		return Mouse.isButtonDown(0) && !isLeftMouseDown;
	}
	
	public static boolean isRightMouseButtonDown() {
		return Mouse.isButtonDown(1) && !isRightMouseDown;
	}
	
	public static Integer[] getKeysDown() {
		ArrayList<Integer> keys = new ArrayList<Integer>();
		Keyboard.poll();
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState())
				keys.add(Keyboard.getEventKey());
		}
		return keys.toArray(new Integer[keys.size()]);
	}
}
