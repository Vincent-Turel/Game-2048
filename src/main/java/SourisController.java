import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicReference;

import package2048.Jeu2048;

public class SourisController extends MouseAdapter {
    Jeu2048 jeu;
    Window window;
    Logger log;

    // Le nombre est une référence que j'utilise plus bas
    int nombre;

    public SourisController(Jeu2048 jeu, Window window, Logger log) {
        this.jeu = jeu;
        this.window = window;
        this.log = log;
    }

    public SourisController(Jeu2048 jeu, Window window, int nombre, Logger log) {
        this.jeu = jeu;
        this.nombre = nombre;
        this.window = window;
        this.log = log;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (nombre) {                                                                                                            // En fonction du nombre rentré dans la vue, sait d'où vient le signal et agis en conséquence
            case 0 -> {
                jeu.decaler(Jeu2048.HAUT);
                log.log(Logger.DEBUG, "Décalage click HAUT");
            }
            case 1 -> {
                jeu.decaler(Jeu2048.BAS);
                log.log(Logger.DEBUG, "Décalage click BAS");
            }
            case 2 -> {
                jeu.decaler(Jeu2048.GAUCHE);
                log.log(Logger.DEBUG, "Décalage click GAUCHE");
            }
            case 3 -> {
                jeu.decaler(Jeu2048.DROITE);
                log.log(Logger.DEBUG, "Décalage click DROITE");
            }
        }
        for (int i = 0; i < Window.NOMBRE_LIGNES; i++) {
            for (int j = 0; j < Window.NOMBRE_COLONNES; j++) {
                if (jeu.tableauFusions()[i][j]) {
                    log.log(Logger.INFO, jeu.getGrilleString()[i][j] + " résultat d'une fusion dans (" + i + ", " + j + ")");
                }
            }
        }
    }
}

