package aa.tulybaev.client.render.components;

import javax.sound.sampled.*;

public class SoundManager {

    public static void play(String path) {
        try {
            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(
                            SoundManager.class.getResource(path)
                    );

            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
