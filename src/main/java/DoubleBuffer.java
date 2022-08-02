import java.awt.*;
import java.awt.Panel;
import java.io.Serial;


public class DoubleBuffer extends Panel {
    @Serial
    private static final long serialVersionUID = 1L;
    private int bufferWidth;
    private int bufferHeight;
    private Image bufferImage;
    private Graphics bufferGraphics;

    public DoubleBuffer() {
        super();
    }

    public void repaint(Graphics g) {
        // Définie la fonction repaint afin de pouvoir l'utiliser
        paint(g);
    }

    public void paint(Graphics g) {

        if (bufferWidth != getSize().width || bufferHeight != getSize().height || bufferImage == null || bufferGraphics == null) {
            // Vérifie que le buffer est toujours adapté à l'image. Si quelque chose ne vas pas (surtout les dimensions), reset le buffer.
            resetBuffer();
        }

        if (bufferGraphics != null) {
            // Netoie le buffer
            bufferGraphics.clearRect(0, 0, bufferWidth, bufferHeight);
            // Dessine les graphismes sur l'image du buffer
            paintBuffer(bufferGraphics);
            //Dessine l'image du Buffer sur l'écran
            g.drawImage(bufferImage, 0, 0, this);
        }
    }

    private void resetBuffer() {
        // Dimensions de l'image que l'on veut dessiner
        bufferWidth = getSize().width;
        bufferHeight = getSize().height;

        if (bufferGraphics != null) {
            // Nettoie l'image précédente 1/2
            bufferGraphics.dispose();
            bufferGraphics = null;
        }
        if (bufferImage != null) {
            // Nettoie l'image précédente 2/2
            bufferImage.flush();
            bufferImage = null;
        }
        // Vide la mémoire (Garbage Collector)
        System.gc();
        //Recrée le Buffer à la taille de l'image à "buffurisée"
        bufferImage = createImage(bufferWidth, bufferHeight);
        bufferGraphics = bufferImage.getGraphics();
    }

    public void paintBuffer(Graphics g) {
        // Méthode à utilisé pour créer des panels qui passent par le Buffer (remplace la méthode paint).
    }
}


