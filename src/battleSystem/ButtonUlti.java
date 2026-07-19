package battleSystem;

import java.awt.Color;

/**
 * Bouton stylisé dédié à la capacité Ultime.
 */
public class ButtonUlti extends ActionButton {
    public ButtonUlti(String text) {
        super(text, new Color(245, 176, 39));
        setToolTipText("Utiliser votre capacité ultime pour infliger de lourds dégâts !");
    }
}
