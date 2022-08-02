import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import package2048.Jeu2048;

public class ClavierController extends KeyAdapter {
    Jeu2048 jeu;
    Logger log;

    public ClavierController(Jeu2048 jeu, Logger log) {
        this.jeu = jeu;
        this.log = log;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Vérifie l'origine du KeyEvent et agit en conséquence.
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                jeu.decaler(Jeu2048.HAUT);
                log.log(Logger.DEBUG, "Décalage flèche HAUT");
            }
            case KeyEvent.VK_LEFT -> {
                jeu.decaler(Jeu2048.GAUCHE);
                log.log(Logger.DEBUG, "Décalage flèche GAUCHE");
            }
            case KeyEvent.VK_RIGHT -> {
                jeu.decaler(Jeu2048.DROITE);
                log.log(Logger.DEBUG, "Décalage flèche DROITE");
            }
            case KeyEvent.VK_DOWN -> {
                jeu.decaler(Jeu2048.BAS);
                log.log(Logger.DEBUG, "Décalage flèche BAS");
            }
        }
        for (int i = 0; i < Window.NOMBRE_LIGNES; i++) {
            for (int j = 0; j < Window.NOMBRE_COLONNES; j++) {
                if (jeu.tableauFusions()[i][j]) {
                    log.log(Logger.INFO, jeu.getGrilleString()[i][j] + " résultat d'une fusion dans (" + (i + 1) + ", " + (j + 1) + ")");
                }
            }
        }
    }
}
