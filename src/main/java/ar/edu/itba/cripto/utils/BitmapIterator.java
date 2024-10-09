package ar.edu.itba.cripto.utils;

import lombok.Getter;

import java.util.Iterator;

public class BitmapIterator implements Iterator<Byte> {
    @Getter
    private final Bitmap bitmap;
    private int currentX;
    private int currentY;
    private Color currentColor; // 0 for blue, 1 for green, 2 for red
    private int lastIndex;

    public BitmapIterator(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.currentX = 0;
        this.currentY = bitmap.getHeight() - 1; // Start from the bottom row
        this.currentColor = Color.BLUE;
        this.lastIndex = 0;
    }

    // Check if there are more pixels to iterate
    @Override
    public boolean hasNext() {
        return currentY >= 0 && currentX < bitmap.getWidth();
    }

    @Override
    public Byte next() {
        if (!hasNext())
            throw new IllegalStateException("No more pixels to iterate.");

        // Calculate the pixel position
        int byteIndex = (currentY * bitmap.getWidth() + currentX) * 3 + currentColor.index;

        // Get the current color component value
        byte color = bitmap.getPixelData()[byteIndex];

        // Move to the next color component
        currentColor = currentColor.nextColor();
        if (currentColor == Color.BLUE) {
            // Move to the next pixel
            if (currentX < bitmap.getWidth() - 1) {
                currentX++;
            } else {
                currentX = 0;
                currentY--;
            }
        }
        lastIndex = byteIndex;
        return color;
    }

    // Set a new byte value at the current position
    public void setByte(byte color) {
        // Calculate the pixel position
        // Set the specific color component
        bitmap.getPixelData()[lastIndex] = color;
    }

    private enum Color {
        BLUE(0), GREEN(1), RED(2);

        private final int index;

        Color(int index) {
            this.index = index;
        }

        public Color nextColor() {
            return switch (this) {
                case BLUE -> GREEN;
                case GREEN -> RED;
                case RED -> BLUE;
            };
        }
    }
}