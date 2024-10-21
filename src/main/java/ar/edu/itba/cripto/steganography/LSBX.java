package ar.edu.itba.cripto.steganography;

import ar.edu.itba.cripto.steganography.exceptions.MessageToLargeException;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;
import ar.edu.itba.cripto.utils.PixelByte;

import java.util.Arrays;

public abstract class LSBX implements LSB {

    protected final int bitsToHide;

    protected LSBX(int bitsToHide) {
        this.bitsToHide = bitsToHide;
    }

    @Override
    public void hide(Bitmap carrier, byte[] message, String extension) {
        int maxDataSize = carrier.getPixelDataSize() / bitsToHide;
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

    @Override
    public Byte readByte(BitmapIterator iterator) {
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
}