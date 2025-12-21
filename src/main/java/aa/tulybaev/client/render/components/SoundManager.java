package aa.tulybaev.client.render.components;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundManager {

    public static void play(String path) {
        // Запускаем в отдельном потоке, чтобы не блокировать игру
        new Thread(() -> {
            try {
                InputStream audioSrc = SoundManager.class.getResourceAsStream(path);
                if (audioSrc == null) {
                    System.err.println("Sound not found: " + path);
                    return;
                }

                AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioSrc);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();

                // Ждём завершения воспроизведения, чтобы корректно закрыть ресурсы
                while (!clip.isRunning()) {
                    Thread.sleep(10);
                }
                while (clip.isRunning()) {
                    Thread.sleep(10);
                }

                clip.close();
                audioIn.close();
            } catch (Exception e) {
                System.err.println("Failed to play sound: " + path);
                e.printStackTrace();
            }
        }, "SoundPlayer").start();
    }
}