package util;

import org.lwjgl.Sys;

public class Clock {

	public static final float MIN_SPEED = -1f, MAX_SPEED = 7f;
	
	public static boolean paused = false;
	public static float delta = 0, speed = 1, totalTime = 0;
	public static long lastFrame;
	
	public static void update() {
		delta = getDelta();
		totalTime += delta;
	}
	
	public static long getTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public static float getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		
		float value = delta * 0.001f;
		if (value > 0.05f)
			value = 0.05f;
		return value;
	}
	
	public static float delta() {
		if (paused)
			return 0;
		else
			return delta * speed;
	}
	
	public static float totalTime() {
		return totalTime;
	}
	
	public static float speed() {
		return speed;
	}
	
	public static void changeSpeed(float change) {
		float newSpeed = speed + change;
		if (newSpeed >= MIN_SPEED && newSpeed <= MAX_SPEED)
			speed += change;
	}
	
	public static void pause() {
		paused = !paused;
	}
}
