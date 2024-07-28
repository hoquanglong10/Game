package model;

import javafx.scene.media.AudioClip;

public class SoundEffects {
	public static void playSound(String soundFile) {
		try {
			AudioClip sound = new AudioClip(SoundEffects.class.getResource(soundFile).toURI().toString());
			sound.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
