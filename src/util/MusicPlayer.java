package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

import util.StateManager.GameState;

public class MusicPlayer {

	private static boolean waiting = true;
	private static float lastPosition = 0, timeWaited = 7, waitTime = 8;
	private static int index = 0, updatesSinceNewPosition = 0;
	private static ArrayList<String> playlist;
	public static Audio track;
	private static GameState lastState;
	
	public static void update() {
		GameState currentState = StateManager.gameState;
		boolean isNewState = currentState != lastState;
		boolean isNull = track == null;
		
		// continue playlist
		if (currentState != GameState.EDITOR) {
			if (!waiting && !isNewState) {
				if (track.getPosition() == lastPosition) {
					updatesSinceNewPosition++;
				} else {
					updatesSinceNewPosition = 0;
				}
				
				if (updatesSinceNewPosition >= 8) {
					// next song
					index++;
					if (index >= playlist.size())
						index = 0;
					playMusic(index);
				}
				
			} else {
				if (!isNull)
					track.stop();
				
				if (isNewState)
					waiting = true;
				
				if (waiting) {
					
					// wait
					if (timeWaited < waitTime) {
						timeWaited += Clock.delta();
					
					//create new playlist
					} else {
						timeWaited = 0;
						waiting = false;
						
						switch (currentState) {
						case MAINMENU:
							playlist = new ArrayList<String>();
							playlist.add("digitalus");
							break;
						case GAME:
							playlist = new ArrayList<String>();
							playlist.add("1990");
							playlist.add("assembly");
							playlist.add("bright_age");
							playlist.add("free");
							playlist.add("mad_dash");
							playlist.add("psykick");
							break;
						case EDITOR:
							playlist = new ArrayList<String>();
							break;
						}
						
						Collections.shuffle(playlist);
						playMusic(0);
					}
				}
			}
			
			SoundStore.get().poll(0);
			lastState = currentState;
			if (!isNull)
				lastPosition = track.getPosition();
		}
	}
	
	private static void playMusic(int index) {
		String song = playlist.get(index);
		track = loadMusic(song);
		track.playAsMusic(1, 1, false);
		updatesSinceNewPosition = 0;
		System.out.println("Playing " + song);
	}
	
	private static Audio loadMusic(String name) {
		Audio newMusic = null;
		try {
			newMusic = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("res/music/" + name + ".ogg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newMusic;
	}
}
