package ar.edu.itba.cripto.utils;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;

public class PixelIterator implements Iterator<int[]> {

    private final BufferedImage image;
    private int currentX = 0;
    private int currentY = 0;

    // Variable para almacenar el estado de la imagen en cada iteración
    private final List<int[][]> imageState;

    public PixelIterator(BufferedImage image) {
        this.image = image;
        this.imageState = new ArrayList<>();
        saveCurrentImageState(); // Guarda el estado inicial de la imagen
    }

    @Override
    public boolean hasNext() {
        return currentY < image.getHeight();
    }

    @Override
    public int[] next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        int rgb = image.getRGB(currentX, currentY);
        int[] colors = new int[3];
        colors[0] = (rgb >> 16) & 0xFF; // Red
        colors[1] = (rgb >> 8) & 0xFF;  // Green
        colors[2] = rgb & 0xFF;         // Blue

        // Mover al siguiente píxel
        currentX++;
        if (currentX >= image.getWidth()) {
            currentX = 0;
            currentY++;
        }

        // Guardar el estado actual de la imagen
        saveCurrentImageState();

        return colors;
    }

    /**
     * Establece el valor RGB en la posición de píxel actual con los colores proporcionados.
     *
     * @param colors Un array de 3 enteros que representan los nuevos valores R, G y B.
     */
    public void setRGB(int[] colors) {
        if (colors.length != 3) {
            throw new IllegalArgumentException("El array RGB debe tener exactamente 3 elementos.");
        }

        // Reconstruir el valor RGB
        int newRgb = (colors[0] << 16) | (colors[1] << 8) | colors[2];

        // Establecer el nuevo valor RGB en la posición actual
        int lastX = currentX == 0 ? image.getWidth() - 1 : currentX - 1;
        int lastY = currentX == 0 ? currentY - 1 : currentY;
        image.setRGB(lastX, lastY, newRgb);

        // Guardar el estado actual de la imagen
        saveCurrentImageState();
    }

    /**
     * Guarda el estado actual de la imagen como una matriz de valores RGB.
     */
    private void saveCurrentImageState() {
        int[][] currentImageState = new int[image.getWidth()][image.getHeight()];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                currentImageState[x][y] = image.getRGB(x, y);
            }
        }
        imageState.add(currentImageState);
    }

    /**
     * Devuelve la lista de estados de la imagen.
     */
    public List<int[][]> getImageStates() {
        return imageState;
    }
}
