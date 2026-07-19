package battleSystem;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;

/**
 * Lecture centralisée des effets sonores. Utilisé par le BattleSystem
 * pour jouer UltiSound.wav exactement au moment où une capacité ultime
 * est activée (et non au simple clic sur un bouton).
 */
public final class SoundPlayer {
    private SoundPlayer() {}

    public static void play(String resourcePath) {
        try {
            URL url = SoundPlayer.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("Son introuvable : " + resourcePath);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.start();
        } catch (Exception e) {
            System.err.println("Erreur lecture son (" + resourcePath + ") : " + e.getMessage());
        }
    }
}
