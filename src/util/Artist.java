package util;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Artist {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 960;
	public static final int TILE_SIZE = 64;
	
	public static void beginSession() {
		Display.setTitle("Shapes Defense");
		
		/*
		// set icon to res/misc/icon.png
		try {
			BufferedImage icon = ImageIO.read(new File("src/res/misc/icon.png"));
			ByteArrayOutputStream converter = new ByteArrayOutputStream();
			ImageIO.write(icon, "jpg", converter);
			converter.flush();
			ByteBuffer buffer = ByteBuffer.wrap(converter.toByteArray());
			converter.close();
			Display.setIcon(new ByteBuffer[] {buffer});
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // minimize screen tearing
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND); // enable transparency
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static boolean isCollision(float x1, float y1, float w1, float h1,
			float x2, float y2, float w2, float h2) {
		boolean xInRange = (x1 + w1 > x2) && (x1 < x2 + w2);
		boolean yInRange = (y1 + h1 > y2) && (y1 < y2 + h2);
		return xInRange && yInRange;
	}
	
	public static void drawQuad(float x, float y, float w, float h) {
		glBegin(GL_QUADS);
		glVertex2f(x, y);         // top left
		glVertex2f(x + w, y);     // top right
		glVertex2f(x + w, y + h); // bottom right
		glVertex2f(x, y + h);     // bottom left
		glEnd();
	}
	
	public static void drawObject(Texture texture, float x, float y) {
		drawObject(texture, x, y, TILE_SIZE, TILE_SIZE);
	}
	
	public static void drawObject(Texture texture, float x, float y, float w, float h) {
		texture.bind();
		
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		
		glTexCoord2f(1, 0);
		glVertex2f(w, 0);
		
		glTexCoord2f(1, 1);
		glVertex2f(w, h);
		
		glTexCoord2f(0, 1);
		glVertex2f(0, h);
		
		glEnd();
		glLoadIdentity();
	}
	
	public static void drawRotatedObject(Texture texture, float x, float y, float a) {
		drawRotatedObject(texture, x, y, TILE_SIZE, TILE_SIZE, a);
	}
	
	public static void drawRotatedObject(Texture texture, float x, float y, float w, float h, float a) {
		texture.bind();
		
		glTranslatef(x + w/2, y + h/2, 0);
		glRotatef(a, 0, 0, 1);
		glTranslatef(-w/2, -h/2, 0);
		
		glBegin(GL_QUADS);
		
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		
		glTexCoord2f(1, 0);
		glVertex2f(w, 0);
		
		glTexCoord2f(1, 1);
		glVertex2f(w, h);
		
		glTexCoord2f(0, 1);
		glVertex2f(0, h);
		
		glEnd();
		glLoadIdentity();
	}
	
	public static void writeText(TrueTypeFont font, float x, float y, String text, Color color) {
		font.drawString(x, y, text, color);
		glColor3f(1, 1, 1);
	}
	
	public static Texture loadTexture(String folder, String name) {
		return loadTextureRaw("res/" + folder + "/" + name + ".png", "PNG");
	}
	
	public static Texture loadTextureRaw(String path, String fileType) {
		Texture texture = null;
		InputStream in = ResourceLoader.getResourceAsStream(path);
		
		try {
			texture = TextureLoader.getTexture(fileType, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}
}
