package ar.edu.itba.cripto.utils;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RGBIterator implements Iterator<int[]> {

    private final BufferedImage image;
    private int currentX = 0;
    private int currentY = 0;

    public RGBIterator(BufferedImage image) {
        this.image = image;
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

        // Move to the next pixel
        currentX++;
        if (currentX >= image.getWidth()) {
            currentX = 0;
            currentY++;
        }

        return colors;
    }

    /**
     * Sets the RGB value at the current pixel position with the provided colors.
     *
     * @param colors An array of 3 integers representing the new R, G, and B values.
     */
    public void setRGB(int[] colors) {
        if (colors.length != 3) {
            throw new IllegalArgumentException("RGB array must have exactly 3 elements.");
        }

        // Reconstruct the RGB value
        int newRgb = (colors[0] << 16) | (colors[1] << 8) | colors[2];

        // Set the new RGB value at the current position
        int lastX = currentX == 0 ? image.getWidth() -1 : currentX - 1;
        int lastY = currentX == 0 ? currentY - 1 : currentY;
        image.setRGB(lastX, lastY, newRgb); // currentX - 1 because next() has already moved the pixel
    }
}