import java.io.*;

import package2048.Jeu2048;

public class Gestion_fichier {

    String nameFile;
    Jeu2048 j;

    public Gestion_fichier(String nameFile, Jeu2048 jeu) {
        this.nameFile = nameFile;
        this.j = jeu;
    }

    public Gestion_fichier(String nameFile) {
        this.nameFile = nameFile;
        this.j = null;
    }

    // Enregistre le meilleur score dans le fichier score
    public static void saveBestScore(int bestScore) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(".\\save\\score"));
            oos.writeObject(bestScore);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Récupère le meilleur score dans le fichier score
    public static int retrieveBestScore() throws IOException {
        int score = 0;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(
                    new FileInputStream(".\\save\\score"));
            try {
                score = (int) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //ois.close();
        } catch (EOFException e) {
            assert ois != null;
            ois.close();
            e.printStackTrace();
        }
        return score;
    }

    // Enregistre le jeu entier
    public void saveAs(String fichier, Jeu2048 jeu) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(fichier));
            oos.writeObject(jeu);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Jeu2048 retrieve(String fichier) { // R�cup�re le jeu entier
        Jeu2048 j = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(fichier));
            try {
                j = (Jeu2048) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return j;
    }

    public void save() {
        saveAs(nameFile, j);
    }
}
			


