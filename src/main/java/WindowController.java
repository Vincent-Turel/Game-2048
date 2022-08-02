import java.awt.event.WindowAdapter;

import package2048.Jeu2048;

public class WindowController extends WindowAdapter {
    Jeu2048 jeu;

    public WindowController(Jeu2048 jeu) {
        this.jeu = jeu;
    }
}
