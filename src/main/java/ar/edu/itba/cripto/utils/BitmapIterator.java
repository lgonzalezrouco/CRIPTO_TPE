package ar.edu.itba.cripto.utils;

import lombok.Getter;

import java.util.Iterator;

public class BitmapIterator implements Iterator<PixelByte> {
    @Getter
    private final Bitmap bitmap;
    private int currentX;
    private int currentY;
    private Color currentColor;
    private int lastIndex;


    public BitmapIterator(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.currentX = 0;
        this.currentY = 0;
        this.currentColor = Color.BLUE;
        this.lastIndex = 0;
    }

    // Check if there are more pixels to iterate
    @Override
    public boolean hasNext() {
        return currentY < bitmap.getHeight() && currentX < bitmap.getWidth();
    }

    @Override
    public PixelByte next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more pixels to iterate.");
        }
        // Calculate the pixel position
        int byteIndex = (currentY * bitmap.getWidth() + currentX) * 3 + currentColor.index();

        // Get the current color component value
        byte data = bitmap.getPixelData()[byteIndex];
        Color color = currentColor;

        currentColor = currentColor.nextColor();
        if (currentColor == Color.BLUE) {
            // Move to the next pixel
            if (currentX < bitmap.getWidth() - 1) {
                currentX++;
            } else {
                currentX = 0;
                currentY++;
            }
        }
        lastIndex = byteIndex;
        return new PixelByte(data, color);
    }

    // Set a new byte value at the current position
    public int setByte(byte pixel) {
        if (bitmap.getPixelData()[lastIndex] == pixel) {
            return -1;
        }
        bitmap.getPixelData()[lastIndex] = pixel;
        return 1;
    }

    // Go back to the beginning of the bitmap
    public void reset() {
        this.currentX = 0;
        this.currentY = 0;
        this.currentColor = Color.BLUE;
        this.lastIndex = 0;
    }
}