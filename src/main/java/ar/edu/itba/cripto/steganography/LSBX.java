package ar.edu.itba.cripto.steganography;

import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;
import ar.edu.itba.cripto.utils.PixelByte;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;

public abstract class LSBX implements LSB {

    protected final int bitsToHide;

    protected LSBX(int bitsToHide) {
        this.bitsToHide = bitsToHide;
    }

    @Override
    public void hide(Bitmap carrier, byte[] dataToEmbed, String extension) {
        BitmapIterator iterator = new BitmapIterator(carrier);
        int byteIndex = 0;
        while (iterator.hasNext() && byteIndex < dataToEmbed.length) {
            writeByte(dataToEmbed[byteIndex], iterator);
            byteIndex++;
        }
    }

    @Override
    public int getBitsToHidePerPixel() {
        return bitsToHide;
    }

    private void writeByte(byte b, BitmapIterator iterator) {
        int bitIndex = 7;
        while (iterator.hasNext() && bitIndex >= 0) {
            PixelByte pixel = iterator.next();
            byte pixelValue = pixel.getValue();

            for (int i = 0; i < bitsToHide && bitIndex >= 0; i++, bitIndex--) {
                byte bit = (byte) ((b >> bitIndex) & 1);
                pixelValue = (byte) ((pixelValue & ~(1 << i)) | (bit << i)); // Clear target bit and set it to the message bit
            }

            iterator.setByte(pixelValue);
        }
    }

    public int size(BitmapIterator iterator) {
        int byteIndex = 0;
        byte[] sizeByte = new byte[4];
        // extract first 4 bytes
        while (iterator.hasNext()) {
            Byte pixel = readByte(iterator);
            sizeByte[byteIndex] = Optional.ofNullable(pixel).orElse((byte) 0);
            byteIndex++;
            if (byteIndex == 4) {
                return ByteBuffer.wrap(sizeByte).getInt();
            }
        }
        throw new IllegalArgumentException("No size found");
    }

    private Byte readByte(BitmapIterator iterator) {
        int bitIndex = 7;
        int currentByte = 0;

        while (iterator.hasNext() && bitIndex >= 0) {
            PixelByte pixel = iterator.next();
            byte pixelValue = pixel.getValue();

            for (int i = 0; i < bitsToHide && bitIndex >= 0; i++, bitIndex--) {
                byte bit = (byte) ((pixelValue >> i) & 1);
                currentByte |= (bit << bitIndex);
            }
        }

        return bitIndex < 0 ? (byte) currentByte : null;
    }

    @Override
    public byte[] extract(Bitmap carrier) {
        BitmapIterator iterator = new BitmapIterator(carrier);
        int msgSize = size(iterator);

        byte[] extracted = new byte[msgSize];
        int byteIndex = 0;
        Byte pixel;
        while ((pixel = readByte(iterator)) != null && byteIndex < msgSize) {
            extracted[byteIndex] = pixel;
            byteIndex++;
        }

        return Arrays.copyOf(extracted, byteIndex); // resize to byteIndex
    }
}