import java.io.*;

public class Logger {

    public static final int ALL = 0;
    public static final int DEBUG = 100;
    public static final int INFO = 500;
    public static final int IMPORTANT = 900;
    public static final int OFF = Integer.MAX_VALUE;

    int level;
    String PrintWriter;

    public Logger() {
        this.level = Logger.DEBUG;
        PrintWriter = "System.err";
    }

    public Logger(int level) {
        this.level = level;
        PrintWriter = "System.err";
    }

    // Constructeur qui va chercher les infos dans le fichier config.txt
    public Logger(String fichier) {
        // Permet de détecter plusieurs espaces de suite
        final String SEPARATOR = "\\s+";
        BufferedReader br;
        File f = new File(fichier);
        if (f.exists()) {
            try {
                br = new BufferedReader(new FileReader(f));
                String ligne = br.readLine();
                // On sépare la ligne en récupérant tous les mots séparés par un ou plusieurs espaces (tabulations comprises) dans un tableau.
                String[] mots = ligne.split(SEPARATOR);
                // On attribue la valeur du level selon le premier mot lu dans le fichier
                this.level = mots[0].equals("ALL") ? Logger.ALL :
                        mots[0].equals("DEBUG") ? Logger.DEBUG :
                                mots[0].equals("IMPORTANT") ? Logger.IMPORTANT :
                                        mots[0].equals("INFO") ? Logger.INFO : Logger.OFF;
                // Le second mot lu est stocké en tant qu'adresse de fichier
                this.PrintWriter = mots[1];
                br.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } else
            System.out.println("Le fichier de configuration n'existe pas");
    }


    // Fonction qui écrit les événements dans la sortie voulue
    public void log(int level, String message) {
        BufferedWriter bw;
        // Dans System.err
        if (level >= this.level) {
            if (PrintWriter.equals("System.err")) {
                try {
                    bw = new BufferedWriter(
                            new OutputStreamWriter(System.err));
                    bw.append(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Dans System.out
            else if (PrintWriter.equals("System.out")) {
                try {
                    bw = new BufferedWriter(
                            new OutputStreamWriter(System.out));
                    bw.write(message);
                    bw.newLine();
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Dans un fichier quelconque
            else {
                try {
                    bw = new BufferedWriter(
                            new FileWriter(PrintWriter, true));
                    bw.write(message + "\n");
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
