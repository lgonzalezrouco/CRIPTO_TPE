package ar.edu.itba.cripto.steganography;

import ar.edu.itba.cripto.steganography.exceptions.MessageToLargeException;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;

public abstract class LSBX extends LSB {

    protected final int bitsToHide;
    private int remainingPixelBits = 0;
    private Byte currentPixel = null;

    protected LSBX(int bitsToHide) {
        this.bitsToHide = bitsToHide;
    }

    @Override
    public void hide(Bitmap carrier, byte[] message, String extension) {
        int maxDataSize = maxMessageSize(carrier);
        if (message.length > maxDataSize) {
            throw new MessageToLargeException("Data is too big for carrier");
        }

        BitmapIterator iterator = new BitmapIterator(carrier);
        int byteIndex = 0;
        while (iterator.hasNext() && byteIndex < message.length) {
            writeByte(message[byteIndex], iterator);
            byteIndex++;
        }
    }

    @Override
    public void writeByte(byte b, BitmapIterator iterator) {
        int bitIndex = 7;

        while (bitIndex >= 0) {
            if (remainingPixelBits == 0) {
                if (!iterator.hasNext()) break;
                currentPixel = iterator.next().getValue();
                remainingPixelBits = bitsToHide;
            }
            // Itera solo sobre los bits disponibles
            int writeBits = Math.min(remainingPixelBits, bitIndex + 1);
            for (int i = writeBits - 1; i >= 0; i--, bitIndex--, remainingPixelBits--) {
                byte bit = (byte) ((b >> bitIndex) & 1);
                currentPixel = (byte) ((currentPixel & ~(1 << i)) | (bit << i)); // Modifica bit en `i`
            }

            iterator.setByte(currentPixel);
        }
    }

    @Override
    public Byte readByte(BitmapIterator iterator) {
        int bitIndex = 7;
        int currentByte = 0;

        while (bitIndex >= 0) {

            if (remainingPixelBits == 0) {
                if (!iterator.hasNext()) break;
                currentPixel = iterator.next().getValue();
                remainingPixelBits = bitsToHide;
            }
            // Itera solo sobre los bits disponibles
            int readBits = Math.min(remainingPixelBits, bitIndex + 1);
            for (int i = readBits - 1; i >= 0; i--, bitIndex--, remainingPixelBits--) {
                byte bit = (byte) ((currentPixel >> i) & 1);
                currentByte |= (bit << bitIndex);
            }
        }

        return bitIndex < 0 ? (byte) currentByte : null;
    }

    @Override
    public int maxMessageSize(Bitmap carrier) {
        return (carrier.getPixelDataSize() * bitsToHide) / 8;
    }

}