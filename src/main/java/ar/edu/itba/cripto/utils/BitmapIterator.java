package ar.edu.itba.cripto.utils;

import lombok.Getter;

import java.util.Iterator;

public class BitmapIterator implements Iterator<PixelByte> {
    @Getter
    private final Bitmap bitmap;
    private int currentX;
    private int currentY;
    private int colorIndex; // 0 for blue, 1 for green, 2 for red
    private int lastIndex;
    public BitmapIterator(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.currentX = 0;
        this.currentY = bitmap.getHeight() - 1; // Start from the bottom row
        this.colorIndex = 0;

        this.lastIndex = 0;
    }

    // Check if there are more pixels to iterate
    @Override
    public boolean hasNext() {
        return currentY >= 0 && currentX < bitmap.getWidth();
    }

    @Override
    public PixelByte next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more pixels to iterate.");
        }
        // Calculate the pixel position
        int byteIndex = (currentY * bitmap.getWidth() + currentX) * 3 + colorIndex;
        // Get the current color component value
        byte color;
        color = bitmap.getPixelData()[byteIndex]; // Blue
        // Move to the next color component
        colorIndex++;
        if (colorIndex > 2) {
            colorIndex = 0;
            // Move to the next pixel
            if (currentX < bitmap.getWidth() - 1) {
                currentX++;
            } else {
                currentX = 0;
                currentY--;
            }
        }
        lastIndex = byteIndex;
        return new PixelByte(color, Color.values()[colorIndex]);
    }

    public void setByte(byte color) {
        bitmap.getPixelData()[lastIndex] = color;
    }


}