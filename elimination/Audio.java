package elimination;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio {
    File audioFile;
    Clip clip;
    boolean play = false;
    public int time;

    /**
     * Play a given audio file.
     * 
     * @param audioFilePath Path of the audio file.
     */
    Audio(String audioFilePath) {
        try {
            String FilePath = audioFilePath;
            audioFile = new File(FilePath);
            clip = AudioSystem.getClip();
            // clip.open(AudioSystem.getAudioInputStream(audioFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loop() {
        if (play == true)
            return;
        play = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clip.open(AudioSystem.getAudioInputStream(audioFile));
                    clip.loop(Clip.LOOP_CONTINUOUSLY);

                    while (play) {
                        Thread.sleep(100);
                    }
                    clip.close();
                    play = false;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void start(int t) {
        this.time = t;
        if (play == true)
            return;
        play = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clip.open(AudioSystem.getAudioInputStream(audioFile));
                    clip.start();
                    while (play && time > 0) {
                        time--;
                        Thread.sleep(1000);
                    }
                    clip.close();
                    play = false;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void pause() {
        clip.stop();
    }

    public void pause(long millis) {
        clip.stop();
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        clip.start();
    }

    public void resume() {
        clip.start();
    }

    public void stop() {
        play = false;
    }

    public static void main(String[] args) {
        Audio player = new Audio("resource/music/bgm.wav");
        player.loop();
        try {
            Thread.sleep(5000);
            player.stop();
            Thread.sleep(5000);
            player.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}