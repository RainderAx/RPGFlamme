package battleSystem;

import javax.swing.Timer;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class ButtonUlti extends ActionButton {
    
    private Clip ultiClip;
    private Timer soundTimer;

    public ButtonUlti(String text) {
        // Appelle le constructeur de ActionButton avec la couleur #F5B027 en RGB
        super(text, new Color(245, 176, 39));
        
        setToolTipText("Utiliser votre attaque ultime pour infliger de lourds dégâts !");

        // 1. Initialisation du son
        try {
            URL soundURL = getClass().getResource("/assets/UltiSound.wav"); 
            if (soundURL != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                ultiClip = AudioSystem.getClip();
                ultiClip.open(audioIn);
            } else {
                System.err.println("Fichier audio introuvable ! Vérifiez le chemin /assets/UltiSound.wav");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du son :");
            e.printStackTrace();
        }

        // 2. Création du Timer pour arrêter le son après 5 secondes
        soundTimer = new Timer(5000, e -> {
            if (ultiClip != null && ultiClip.isRunning()) {
                ultiClip.stop();
            }
        });
        soundTimer.setRepeats(false); 

        // 3. Ajout de l'événement de souris : déclenchement au CLIC (mousePressed)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // On vérifie que le clic provient bien du clic gauche principal
                if (e.getButton() == MouseEvent.BUTTON1 && ultiClip != null) {
                    
                    // Optionnel : Si le son joue déjà et qu'on reclique, 
                    // on l'arrête proprement avant de le relancer
                    if (ultiClip.isRunning()) {
                        ultiClip.stop();
                    }
                    
                    ultiClip.setFramePosition(0); // Rembobine le son au début
                    ultiClip.start();             // Lance la lecture
                    soundTimer.restart();         // Lance (ou relance) le chrono de 5 secondes
                }
            }
        });
    }
}