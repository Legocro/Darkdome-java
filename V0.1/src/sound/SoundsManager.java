package sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

import javax.swing.JApplet;

import engineTester.Paths;

@SuppressWarnings("serial")
public class SoundsManager extends JApplet {

	public class Sound { 
		private AudioClip song;
		private URL songPath;
		Sound(String fileName, int fileType) { // 1 for music and 2 for sounds
			String path;
			if (fileType == 1)
				path = Paths.getMusic() + fileName;
			else
				path = Paths.getSounds() + fileName;
			
			try {
				songPath = new URL(getCodeBase(), path);
				song = Applet.newAudioClip(songPath);
			} catch (Exception e) { e.printStackTrace(); }
		}
		public void playSound() { song.loop(); }
		public void stopSound() { song.stop(); }
		public void playerSoundOnce() { song.play(); }
	}
	
	public void exampleInit() {
		Sound testSong = new Sound("song.mid", 1);
		testSong.playSound();
	}
}
