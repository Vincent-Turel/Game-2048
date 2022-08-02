import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.util.concurrent.atomic.AtomicReference;
import java.util.ArrayList;

import package2048.Jeu2048;

public class Window extends Frame {
    @Serial
    private static final long serialVersionUID = 1L;
    public static final int HAUTEUR = 750;
    public static final int LARGEUR = 750;
    public static final int NOMBRE_LIGNES = 4;
    public static final int NOMBRE_COLONNES = 4;
    public static final int OBJECTIF = 2048;

    public static void main(String[] args) {
        // Initialisation du modèle
        Jeu2048 jeu = new Jeu2048(NOMBRE_LIGNES, NOMBRE_COLONNES, OBJECTIF);
        // Initialisation de la vue avec le modèle
        Window window = new Window("INFO403 - TP3/4  - Vincent TUREL", jeu);
        // Initialise directement une partie
        jeu.nouveauJeu();
        // Affiche la fenêtre au milieu de l'écran au lieu d'en haut à gauche
        window.setLocationRelativeTo(null);
    }

    @SuppressWarnings("deprecation")
    public Window(String nom, Jeu2048 jeu) {
        super(nom);
        this.setSize(LARGEUR, HAUTEUR);
        this.setBackground(new Color(250, 248, 239));            // Param�tre de base de la vue, enl�ve layout par d�faut
        setLayout(null);

        Color couleur = new Color(187, 173, 160);                                // Couleur qui sera utile dans la plupart des dessins

        Image artifice = Toolkit.getDefaultToolkit().createImage(".\\image\\artifice_3.gif");
        Image artifice2 = Toolkit.getDefaultToolkit().createImage(".\\image\\artifice_4.gif");
        Image artifice3 = Toolkit.getDefaultToolkit().createImage(".\\image\\artifice_5.gif");

        Panel p2 = new Panel(new GridLayout(NOMBRE_LIGNES, NOMBRE_COLONNES, 10, 10));
        p2.setBackground(couleur);

        // List qui tient à jour l'historique
        ArrayList<Integer> tab_historique = new ArrayList<>();

        // Variables références qui influent sur le comportement de l'update de la vue
        AtomicReference<Integer> nb_btr1 = new AtomicReference<>(0);
        AtomicReference<Integer> nb_btr2 = new AtomicReference<>(0);
        AtomicReference<Integer> nb_hist1 = new AtomicReference<>(0);
        AtomicReference<Integer> nb_hist2 = new AtomicReference<>(0);
        AtomicReference<Integer> nb_save1 = new AtomicReference<>(0);
        AtomicReference<Integer> nb_save2 = new AtomicReference<>(0);
        AtomicReference<Integer> nb_rec1 = new AtomicReference<>(0);
        AtomicReference<Integer> nb_rec2 = new AtomicReference<>(0);

        try {
            jeu.setBestScore(Gestion_fichier.retrieveBestScore());
        } catch (Exception e) {
            jeu.setBestScore(0);
        }

        Logger log = new Logger("config.txt");

        log.log(Logger.INFO, "Nouveau jeu. NbCols : " + jeu.getNbCols() + ", NbLignes : " + jeu.getNbLignes() + ", But a atteindre : " + jeu.getNbBut());

        FileDialog fd;
        fd = new FileDialog(this, "Enregistrer sous");
        fd.setDirectory(".\\save\\");
        fd.setMultipleMode(false);

        Label[][] grille = new Label[NOMBRE_LIGNES][NOMBRE_COLONNES];
        for (int i = 0; i < NOMBRE_LIGNES; i++) {
            for (int j = 0; j < NOMBRE_COLONNES; j++) {
                grille[i][j] = new Label();                                        // Initialise la grille de jeu de l'on met dans le panel p2
                grille[i][j].setAlignment(Label.CENTER);
                p2.add(grille[i][j]);
            }
        }


        // Une série de dessins.
        // Ce sont tous des DoubleBuffer, type que j'ai cree qui hérite de Label, mais qui "bufferise" toutes les images
        // afin de supprimer les "clignotements" dues au fonctionnement du repaint() qui efface le dessin avant de le redessiner.
        DoubleBuffer dessin = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                g.setColor(couleur);
                g.fillRoundRect((int) (getWidth() * 0.77), (int) (0.3 * getHeight()), (int) (getWidth() * 0.18), (int) (0.515 * getHeight()), 15, 15);
                g.fillRoundRect((int) (getWidth() * 0.55), (int) (0.3 * getHeight()), (int) (getWidth() * 0.18), (int) (0.515 * getHeight()), 15, 15);
                g.setColor(Color.BLACK);
                g.drawRoundRect((int) (getWidth() * 0.55), (int) (0.3 * getHeight()), (int) (getWidth() * 0.18), (int) (0.515 * getHeight()), 15, 15);
                g.drawRoundRect((int) (getWidth() * 0.77), (int) (0.3 * getHeight()), (int) (getWidth() * 0.18), (int) (0.515 * getHeight()), 15, 15);
                Font font1 = new Font("Serif", Font.BOLD, (int) (getWidth() * 0.02));
                FontMetrics fm1 = getFontMetrics(font1);
                int size1 = fm1.stringWidth("MEILLEUR");
                int size2 = fm1.stringWidth("SCORE");
                g.setFont(font1);
                g.drawString("SCORE", (int) (getWidth() * 0.55) + ((int) (getWidth() * 0.18 / 2 - size2 / 2)), (int) (0.45 * getHeight()));
                g.drawString("MEILLEUR", (int) (getWidth() * 0.77) + ((int) (getWidth() * 0.18 / 2 - size1 / 2)), (int) (0.45 * getHeight()));
                Font font2 = new Font("Serif", Font.BOLD, (int) (getHeight() * 0.31));
                FontMetrics fm2 = getFontMetrics(font2);
                int size3 = fm2.stringWidth(Integer.toString(jeu.getScore()));
                int size4 = fm2.stringWidth(Integer.toString(jeu.getBestScore()));
                g.setFont(font2);
                g.setColor(Color.WHITE);
                g.drawString(Integer.toString(jeu.getScore()), (int) (getWidth() * 0.55) + ((int) (getWidth() * 0.18 / 2 - size3 / 2)), (int) (0.72 * getHeight()));
                g.drawString(Integer.toString(jeu.getBestScore()), (int) (getWidth() * 0.77) + ((int) (getWidth() * 0.18 / 2 - size4 / 2)), (int) (0.72 * getHeight()));
            }
        };

        DoubleBuffer feu_artifice_gauche = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                if (artifice != null) {
                    g.drawImage(artifice, 20, 0, this);
                    g.drawImage(artifice2, 20, 100, this);
                    g.drawImage(artifice3, 20, 200, this);
                    g.drawImage(artifice, 20, 300, this);
                    g.drawImage(artifice2, 20, 400, this);
                }
            }
        };

        DoubleBuffer fond = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                g.setColor(couleur);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g.drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        DoubleBuffer dessin_triangle_haut = new DoubleBuffer();
        dessin_triangle_haut.setBackground(couleur);

        DoubleBuffer dessin_triangle_bas = new DoubleBuffer();
        dessin_triangle_bas.setBackground(couleur);

        DoubleBuffer dessin_triangle_gauche = new DoubleBuffer();
        dessin_triangle_gauche.setBackground(couleur);

        DoubleBuffer dessin_triangle_droit = new DoubleBuffer();
        dessin_triangle_droit.setBackground(couleur);

        DoubleBuffer feu_artifice_droit = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                if (artifice != null) {
                    g.drawImage(artifice3, 20, 0, this);
                    g.drawImage(artifice, 20, 100, this);
                    g.drawImage(artifice2, 20, 200, this);
                    g.drawImage(artifice, 20, 300, this);
                    g.drawImage(artifice3, 20, 400, this);
                }
            }
        };

        DoubleBuffer nom_jeu = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                g.setColor(couleur);
                g.setFont(new Font("Serif", Font.BOLD, (int) (dessin.getWidth() * 0.1)));
                g.drawString("2048", (int) (getWidth() * 0.01), (int) (getHeight() * 0.98));
            }
        };

        DoubleBuffer bouton_restart = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                Color c1 = nb_btr1.get() == 0 ? couleur : new Color(147, 133, 120);
                g.setColor(couleur);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g.setColor(c1);
                if (nb_btr1.get() == 1) {
                    g.fillRoundRect(1, 1, getWidth() - 4, getHeight() - 4, 20, 20);
                }
                Color c2 = nb_btr2.get() == 0 ? new Color(125, 125, 125) : Color.BLACK;
                g.setColor(c2);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g.setColor(Color.BLACK);
                Font font = new Font("Serif", Font.BOLD, (int) (getWidth() * 0.09));
                g.setFont(font);
                FontMetrics fm = getFontMetrics(font);
                int size = fm.stringWidth("RECOMMENCER");
                g.drawString("RECOMMENCER", getWidth() / 2 - size / 2, (int) (getHeight() * 0.6));
            }
        };

        DoubleBuffer bouton_save = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                Color c1 = nb_save1.get() == 0 ? couleur : new Color(147, 133, 120);
                g.setColor(couleur);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g.setColor(c1);
                if (nb_save1.get() == 1) {
                    g.fillRoundRect(1, 1, getWidth() - 4, getHeight() - 4, 20, 20);
                }
                Color c2 = nb_save2.get() == 0 ? new Color(125, 125, 125) : Color.BLACK;
                g.setColor(c2);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g.setColor(Color.BLACK);
                Font font = new Font("Serif", Font.BOLD, (int) (getWidth() * 0.08));
                g.setFont(font);
                FontMetrics fm = getFontMetrics(font);
                int size = fm.stringWidth("SAUVEGARDER");
                g.drawString("SAUVEGARDER", getWidth() / 2 - size / 2, (int) (getHeight() * 0.65));
            }
        };

        DoubleBuffer bouton_retrieve = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                Color c1 = nb_rec1.get() == 0 ? couleur : new Color(147, 133, 120);
                g.setColor(couleur);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g.setColor(c1);
                if (nb_rec1.get() == 1) {
                    g.fillRoundRect(1, 1, getWidth() - 4, getHeight() - 4, 20, 20);
                }
                Color c2 = nb_rec2.get() == 0 ? new Color(125, 125, 125) : Color.BLACK;
                g.setColor(c2);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g.setColor(Color.BLACK);
                Font font = new Font("Serif", Font.BOLD, (int) (getWidth() * 0.08));
                g.setFont(font);
                FontMetrics fm = getFontMetrics(font);
                int size = fm.stringWidth("RÉCUPÉRER");
                g.drawString("RÉCUPÉRER", getWidth() / 2 - size / 2, (int) (getHeight() * 0.65));
            }
        };


        DoubleBuffer bouton_history = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                Color c1 = nb_hist1.get() == 0 ? couleur : new Color(147, 133, 120);
                g.setColor(couleur);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g.setColor(c1);
                if (nb_hist1.get() == 1) {
                    g.fillRoundRect(1, 1, getWidth() - 4, getHeight() - 4, 20, 20);
                }
                Color c2 = nb_hist2.get() == 0 ? new Color(125, 125, 125) : Color.BLACK;
                g.setColor(c2);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g.setColor(Color.BLACK);
                Font font = new Font("Serif", Font.BOLD, (int) (getWidth() * 0.09));
                g.setFont(font);
                FontMetrics fm = getFontMetrics(font);
                int size = fm.stringWidth("VOIR HISTORIQUE");
                g.drawString("VOIR HISTORIQUE", getWidth() / 2 - size / 2, (int) (getHeight() * 0.6));
            }
        };

        DoubleBuffer historique = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                int interligne = (int) (getHeight() * 0.058);
                g.setColor(couleur);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g.setColor(Color.BLACK);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g.setFont(new Font("Serif", Font.BOLD, 35));
                g.drawString("HISTORIQUE", (int) (getWidth() * 0.33), 70);
                g.setFont(new Font("Serif", Font.ITALIC, 25));
                if (tab_historique.size() > 20) {
                    for (int i = 0; i < tab_historique.size() - 1; i++) {
                        tab_historique.set(i, tab_historique.get(i + 1));
                    }
                    tab_historique.remove(20);
                }
                if (tab_historique.size() <= 10) {
                    for (int i = 0; i < tab_historique.size(); i++) {
                        g.drawString("Partie " + (i + 1) + ": \t " + tab_historique.get(i).toString(), (int) (getWidth() * 0.1), 150 + i * interligne);
                    }
                } else if (tab_historique.size() < 21) {
                    g.drawLine((int) (getWidth() * 0.5), 100, (int) (getWidth() * 0.5), (int) (getHeight() * 0.8));
                    for (int i = 0; i < 10; i++) {
                        g.drawString("Partie " + (i + 1) + ": \t " + tab_historique.get(i).toString(), (int) (getWidth() * 0.1), 150 + i * interligne);
                    }
                    for (int i = 10; i < tab_historique.size(); i++) {
                        g.drawString("Partie " + (i + 1) + ": \t " + tab_historique.get(i).toString(), (int) (getWidth() * 0.6), 150 + (i - 10) * interligne);
                    }
                }
            }
        };

        DoubleBuffer perdu = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                g.setColor(couleur);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g.setColor(Color.BLACK);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                Font font = new Font("Serif", Font.BOLD, 25);
                g.setFont(font);
                FontMetrics fm = getFontMetrics(font);
                int size = fm.stringWidth("Vous avez perdu ! Souhaitez-vous rejouer ?");
                g.drawString("Vous avez perdu ! Souhaitez-vous rejouer ?", (getWidth() / 2) - size / 2, (int) (getHeight() * 0.3));

            }
        };

        DoubleBuffer gagner = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                g.setColor(couleur);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g.setColor(Color.BLACK);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                Font font = new Font("Serif", Font.BOLD, 25);
                g.setFont(font);
                FontMetrics fm = getFontMetrics(font);
                int size = fm.stringWidth("Bravo ! Vous avez gagner ! Souhaitez-vous rejouer ?");
                g.drawString("Bravo ! Vous avez gagner ! Souhaitez-vous rejouer ?", (getWidth() / 2) - size / 2, (int) (getHeight() * 0.3));

            }
        };

        DoubleBuffer oui = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                g.setColor(couleur);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                Font font = new Font("Serif", Font.BOLD, 25);
                g.setFont(font);
                FontMetrics fm = getFontMetrics(font);
                int size = fm.stringWidth("Oui");
                g.drawString("Oui", this.getWidth() / 2 - size / 2, (int) (getHeight() * 0.76));

            }
        };

        DoubleBuffer non = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                g.setColor(couleur);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                Font font = new Font("Serif", Font.BOLD, 25);
                g.setFont(font);
                FontMetrics fm = getFontMetrics(font);
                int size = fm.stringWidth("Non");
                g.drawString("Non", this.getWidth() / 2 - size / 2, (int) (this.getHeight() * 0.76));

            }
        };

        DoubleBuffer retour = new DoubleBuffer() {
            @Serial
            private static final long serialVersionUID = 1L;

            public void paintBuffer(Graphics g) {
                g.setColor(new Color(180, 180, 180));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g.setFont(new Font("Serif", Font.BOLD, 19));
                g.drawString("RETOUR", (int) (getWidth() * 0.3), (int) (getHeight() * 0.6));
            }
        };


        // Placement de tous les éléments à la main relativement à la dimension de la fenêtre afin d'avoir un
        // semblant de redimensionnement malgré l'absence de Layout
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                // Cette boucle met à jour couleur de fond des cases du jeu et couleur des chiffres à l'intérieur
                for (int i = 0; i < NOMBRE_LIGNES; i++) {
                    for (int j = 0; j < NOMBRE_COLONNES; j++) {
                        grille[i][j].setFont(new Font("Serif", Font.BOLD, (int) (getHeight() / (2.4 * NOMBRE_COLONNES)))); // Met � jour la taille des nombres en fonction de la taille de la grille et du nombre de colonnes
                    }
                }
                nom_jeu.setBounds((int) (getWidth() * 0.07), (int) (getHeight() * 0.01), (int) (getWidth() * 0.2), (int) (getHeight() * 0.17));
                bouton_history.setBounds((int) (getWidth() * 0.31), (int) (getHeight() * 0.075), (int) (getWidth() * 0.2), (int) (getHeight() * 0.06));
                bouton_restart.setBounds((int) (getWidth() * 0.31), (int) (getHeight() * 0.144), (int) (getWidth() * 0.2), (int) (getHeight() * 0.06));
                bouton_save.setBounds((int) (getWidth() * 0.1), (int) (getHeight() * 0.9), (int) (getWidth() * 0.35), (int) (getHeight() * 0.06));
                bouton_retrieve.setBounds((int) (getWidth() * 0.55), (int) (getHeight() * 0.9), (int) (getWidth() * 0.35), (int) (getHeight() * 0.06));
                retour.setBounds((int) (getWidth() * 0.4), (int) (getHeight() * 0.88), (int) (getWidth() * 0.2), (int) (getHeight() * 0.07));
                historique.setBounds((int) (getWidth() * 0.17), (int) (getHeight() * 0.228), (int) (getWidth() * 0.66), (int) (getHeight() * 0.756));
                perdu.setBounds((int) (getWidth() * 0.1), (int) (getHeight() * 0.45), (int) (getWidth() * 0.8), (int) (getHeight() * 0.17));
                gagner.setBounds((int) (getWidth() * 0.1), (int) (getHeight() * 0.45), (int) (getWidth() * 0.8), (int) (getHeight() * 0.17));
                dessin.setBounds(0, 0, getWidth(), (int) (getHeight() * 0.25));
                p2.setBounds((int) (getWidth() * 0.2), (int) (getHeight() * 0.28), (int) (getWidth() * 0.6), (int) (getHeight() * 0.55));
                fond.setBounds((int) (getWidth() * 0.17), (int) (getHeight() * 0.25), (int) (getWidth() * 0.66), (int) (getHeight() * 0.61));
                non.setBounds((int) (getWidth() * 0.3), (int) (getHeight() * 0.55), (int) (getWidth() * 0.07), (int) (getHeight() * 0.035));
                oui.setBounds((int) (getWidth() * 0.7 - 50), (int) (getHeight() * 0.55), (int) (getWidth() * 0.07), (int) (getHeight() * 0.035));
                feu_artifice_gauche.setBounds(0, (int) (getHeight() * 0.3), (int) (getWidth() * 0.17), (int) (getHeight() * 0.7));
                feu_artifice_droit.setBounds((int) (getWidth() * (0.66 + 0.17)), (int) (getHeight() * 0.3), (int) (getWidth() * (0.17)), (int) (getHeight() * 0.7));
                dessin_triangle_haut.setBounds((int) (getWidth() * 0.2), (int) (getHeight() * 0.25), (int) (getWidth() * 0.6), (int) (getHeight() * 0.03));
                dessin_triangle_bas.setBounds((int) (getWidth() * 0.2), (int) (getHeight() * 0.8305), (int) (getWidth() * 0.6), (int) (getHeight() * 0.03));
                dessin_triangle_gauche.setBounds((int) (getWidth() * 0.17), (int) (getHeight() * 0.28), (int) (getWidth() * 0.03), (int) (getHeight() * 0.55));
                dessin_triangle_droit.setBounds((int) (getWidth() * 0.8), (int) (getHeight() * 0.28), (int) (getWidth() * 0.03), (int) (getHeight() * 0.55));
                nom_jeu.repaint();
                dessin.repaint();
                dessin_triangle_haut.repaint();
                dessin_triangle_bas.repaint();
                dessin_triangle_gauche.repaint();
                dessin_triangle_droit.repaint();
            }
        });


        // update qui met à jour la vue (couleur des cases de la grille, historique, affichage ponctuel) en fonction des différents
        // événements du modèle (victoire/défaite/recommencer)
        jeu.addObserver((o, arg) -> {
            // Cette grande boucle met à jour couleur de fond des cases du jeu et couleur des chiffres à l'intérieur
            for (int i = 0; i < NOMBRE_LIGNES; i++) {
                for (int j = 0; j < NOMBRE_COLONNES; j++) {
                    grille[i][j].setText(jeu.getGrilleString()[i][j]);
                    // Enlève les zéros de la grille
                    if (grille[i][j].getText().contentEquals("0")) {
                        grille[i][j].setText(" ");
                    }
                    switch (jeu.getGrilleInt()[i][j]) {
                        case 0 -> {
                            grille[i][j].setBackground(new Color(195, 180, 170));
                            grille[i][j].setForeground(Color.BLACK);
                        }
                        case 2 -> {
                            grille[i][j].setBackground(new Color(220, 210, 200));
                            grille[i][j].setForeground(Color.BLACK);
                        }
                        case 4 -> {
                            grille[i][j].setBackground(new Color(220, 210, 190));
                            grille[i][j].setForeground(Color.BLACK);
                        }
                        case 8 -> {
                            grille[i][j].setBackground(new Color(230, 170, 120));
                            grille[i][j].setForeground(Color.WHITE);
                        }
                        case 16 -> {
                            grille[i][j].setBackground(new Color(235, 140, 85));
                            grille[i][j].setForeground(Color.WHITE);
                        }
                        case 32 -> {
                            grille[i][j].setBackground(new Color(245, 125, 95));
                            grille[i][j].setForeground(Color.WHITE);
                        }
                        case 64 -> {
                            grille[i][j].setBackground(new Color(235, 90, 55));
                            grille[i][j].setForeground(Color.WHITE);
                        }
                        case 128 -> {
                            grille[i][j].setBackground(new Color(220, 190, 110));
                            grille[i][j].setForeground(Color.WHITE);
                        }
                        case 256 -> {
                            grille[i][j].setBackground(new Color(220, 190, 95));
                            grille[i][j].setForeground(Color.WHITE);
                        }
                        case 512 -> {
                            grille[i][j].setBackground(new Color(225, 185, 80));
                            grille[i][j].setForeground(Color.WHITE);
                        }
                        case 1024 -> {
                            grille[i][j].setBackground(new Color(225, 180, 65));
                            grille[i][j].setForeground(Color.WHITE);
                        }
                        case 2048 -> {
                            grille[i][j].setBackground(new Color(200, 200, 200));
                            grille[i][j].setForeground(Color.WHITE);
                        }
                    }
                }
            }

            // Met à jour le score, mais seulement un carré autour du nombre pour éviter toute sensation de clignotement
            dessin.repaint(5, (int) (dessin.getWidth() * 0.57), (int) (0.5 * dessin.getHeight()), (int) (dessin.getWidth() * 0.3), 80);
            // Met à jour le meilleur score, seulement quand il y a besoin
            if (jeu.getBestScore() == jeu.getScore()) {
                dessin.repaint(5, (int) (dessin.getWidth() * 0.77), (int) (0.5 * dessin.getHeight()), (int) (dessin.getWidth() * 0.3), 80);
            }
            // Met à jour l'historique
            historique.repaint();

            // Si gagner, affiche un message et des feux d'artifice
            if (jeu.estVainquer()) {
                gagner.show();
                oui.show();
                non.show();
                if (jeu.getScore() != 0) {
                    //tab_historique.add(jeu.getScore());
                    feu_artifice_gauche.show();
                    feu_artifice_droit.show();
                }
            }
            if (jeu.estTermine() && !(jeu.estVainquer())) {                                                                            // Si perdu, affiche un message
                perdu.show();
                oui.show();
                non.show();
                if (jeu.getScore() != 0) {
                    //tab_historique.add(jeu.getScore());
                }
            }
        });

        // Bouton que je n'affiche pas et qui sert seulement à supporter le KeyListener et le MouseListener
        Button b1 = new Button();
        b1.addKeyListener(new ClavierController(jeu, log));
        b1.addMouseListener(new SourisController(jeu, this, log));
        // Les 4 dessins qui permettent de jouer à la souris
        dessin_triangle_haut.addMouseListener(new SourisController(jeu, this, 0, log));
        dessin_triangle_bas.addMouseListener(new SourisController(jeu, this, 1, log));
        dessin_triangle_gauche.addMouseListener(new SourisController(jeu, this, 2, log));
        dessin_triangle_droit.addMouseListener(new SourisController(jeu, this, 3, log));

        // Une série de MouseListener qui transforme les dessins en veritable bouton
        // Je les ai tous mis dans la vue, car ils n'interagissent jamais avec le modèle à part le fait de créer un nouveau jeu...

        // Bouton retour qui ferme l'historique
        retour.addMouseListener(new SourisController(jeu, this, log) {
            @Override
            public void mouseReleased(MouseEvent e) {
                log.log(Logger.INFO, "Fermeture de l'historique");
                historique.hide();
                retour.hide();
            }
        });

        // Bouton qui affiche l'historique
        bouton_history.addMouseListener(new SourisController(jeu, this, log) {
            @Override
            public void mouseClicked(MouseEvent e) {
                bouton_history.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                log.log(Logger.INFO, "Ouverture de l'historique");
                historique.show();
                retour.show();
                nb_hist1.set(1);
                bouton_history.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                nb_hist2.set(1);
                bouton_history.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nb_hist2.set(0);
                nb_hist1.set(0);
                bouton_history.repaint();
            }
        });

        // Bouton "Recommencer"
        bouton_restart.addMouseListener(new SourisController(jeu, this, log) {
            @Override
            public void mousePressed(MouseEvent e) {
                nb_btr1.set(1);
                bouton_restart.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                nb_btr2.set(1);
                bouton_restart.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (jeu.getScore() != 0) {
                    tab_historique.add(jeu.getScore());
                }
                log.log(Logger.DEBUG, "Click sur \"Nouveau jeu\"");
                log.log(Logger.INFO, "Nouveau jeu");
                jeu.nouveauJeu();
                gagner.hide();
                perdu.hide();
                oui.hide();
                non.hide();
                feu_artifice_gauche.hide();
                feu_artifice_droit.hide();
                nb_btr1.set(0);
                bouton_restart.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nb_btr2.set(0);
                nb_btr1.set(0);
                bouton_restart.repaint();
            }
        });

        // Bouton "Sauvegarder"
        bouton_save.addMouseListener(new SourisController(jeu, this, log) {
            @Override
            public void mousePressed(MouseEvent e) {
                nb_save1.set(1);
                bouton_save.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                nb_save2.set(1);
                bouton_save.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                log.log(Logger.INFO, "Click sur \"Sauvegarde\" du jeu");
                fd.setMode(FileDialog.SAVE);
                fd.setVisible(true);
                String chemin_fichier = fd.getDirectory() + fd.getFile();
                Gestion_fichier fichier = new Gestion_fichier(chemin_fichier, jeu);
                fichier.save();
                nb_save1.set(0);
                bouton_save.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nb_save2.set(0);
                nb_save1.set(0);
                bouton_save.repaint();
            }
        });

        // Bouton "Récupérer"
        bouton_retrieve.addMouseListener(new SourisController(jeu, this, log) {

            @Override
            public void mousePressed(MouseEvent e) {
                nb_rec1.set(1);
                bouton_retrieve.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                nb_rec2.set(1);
                bouton_retrieve.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                log.log(Logger.INFO, "Click sur \"Récupérer\" jeu");
                Jeu2048 j;
                fd.setMode(FileDialog.LOAD);
                // Affiche la boite de dialogue pour sélectionner un fichier
                fd.setVisible(true);
                // Récupère le chemin du fichier sélectionne
                String chemin_fichier = fd.getDirectory() + fd.getFile();
                Gestion_fichier fichier = new Gestion_fichier(chemin_fichier);
                // Récupère la sauvegarde du jeu
                j = fichier.retrieve(chemin_fichier);
                // Met à jour les différents elements d'un jeu
                if (jeu.getBestScore() < j.getBestScore()) {
                    this.jeu.setBestScore(j.getBestScore());
                }
                this.jeu.setGrilleInt(j.getGrilleInt());
                this.jeu.setScore(j.getScore());
                this.jeu.setNbBut(j.getNbBut());
                // Met à jour la vue
                jeu.notifyObservers();
                nb_rec1.set(0);
                bouton_retrieve.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nb_rec2.set(0);
                nb_rec1.set(0);
                bouton_retrieve.repaint();
            }
        });

        // Bouton "oui" qui s'affiche si le joueur a perdu ou gagné
        oui.addMouseListener(new SourisController(jeu, this, log) {
            @Override
            public void mouseReleased(MouseEvent e) {
                log.log(Logger.DEBUG, "Click sur \"oui\"");
                log.log(Logger.INFO, "Nouveau jeu");
                tab_historique.add(jeu.getScore());
                jeu.nouveauJeu();
                gagner.hide();
                perdu.hide();
                oui.hide();
                non.hide();
                feu_artifice_gauche.hide();
                feu_artifice_droit.hide();
            }
        });

        // Bouton "non" qui s'affiche si le joueur a perdu ou gagné
        non.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent we) {
                Gestion_fichier.saveBestScore(jeu.getBestScore());
                log.log(Logger.INFO, "Fermer fenêtre\n");
                System.exit(0);
            }
        });

        addWindowListener(new WindowController(jeu) {
            @Override
            public void windowClosing(WindowEvent we) {
                Gestion_fichier.saveBestScore(jeu.getBestScore());
                log.log(Logger.INFO, "Fermer fenêtre\n");
                System.exit(0);
            }
        });

        // Enlève toute possibilité de changement de focus pour pouvoir jouer au clavier quoi que vous fassiez
        dessin.setFocusable(false);
        feu_artifice_gauche.setFocusable(false);
        feu_artifice_droit.setFocusable(false);
        nom_jeu.setFocusable(false);
        bouton_restart.setFocusable(false);
        bouton_save.setFocusable(false);
        bouton_retrieve.setFocusable(false);
        bouton_history.setFocusable(false);
        fond.setFocusable(false);
        historique.setFocusable(false);
        retour.setFocusable(false);
        dessin_triangle_haut.setFocusable(false);
        dessin_triangle_bas.setFocusable(false);
        dessin_triangle_gauche.setFocusable(false);
        dessin_triangle_droit.setFocusable(false);

        // Ajoute tout les elements sur la fenêtre et cache tous ceux qui ne doivent pas être visible au départ
        this.add(retour);
        this.add(historique);
        this.add(oui);
        this.add(non);
        this.add(perdu);
        this.add(gagner);
        this.add(dessin_triangle_haut);
        this.add(dessin_triangle_bas);
        this.add(dessin_triangle_gauche);
        this.add(dessin_triangle_droit);
        this.add(feu_artifice_gauche);
        this.add(feu_artifice_droit);
        this.add(nom_jeu);
        this.add(bouton_restart);
        this.add(bouton_history);
        this.add(bouton_retrieve);
        this.add(bouton_save);
        this.add(p2);
        this.add(dessin);
        this.add(b1);
        this.add(fond);

        feu_artifice_gauche.hide();
        feu_artifice_droit.hide();
        gagner.hide();
        perdu.hide();
        oui.hide();
        non.hide();
        historique.hide();
        retour.hide();

        // La ligne la plus importante de tout le programme
        setVisible(true);
    }
}

