package ar.edu.itba.cripto.steganography;

import ar.edu.itba.cripto.steganography.exceptions.MessageToLargeException;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;
import ar.edu.itba.cripto.utils.Color;
import ar.edu.itba.cripto.utils.PixelByte;

import java.util.EnumMap;

public class LSBI extends LSB {

    // if the integer value is greater than 0, then there are more changed bits than unchanged bits
    private final EnumMap<BitPattern, Integer> changedBitPattern = new EnumMap<>(BitPattern.class);

    public LSBI() {
        changedBitPattern.put(BitPattern.ZERO_ZERO, 0);
        changedBitPattern.put(BitPattern.ZERO_ONE, 0);
        changedBitPattern.put(BitPattern.ONE_ZERO, 0);
        changedBitPattern.put(BitPattern.ONE_ONE, 0);
    }

    @Override
    public void hide(Bitmap carrier, byte[] message, String extension) {
        int availablePixels = (int) Math.floor((carrier.getPixelDataSize() / 3.0) * 2.0);
        if ((message.length + 4) > availablePixels / 8) {
            throw new MessageToLargeException("Data is too big for carrier");
        }

        BitmapIterator iterator = new BitmapIterator(carrier);
        stepForward(iterator);

        int byteIndex = 0;
        while (iterator.hasNext() && byteIndex < message.length) {
            writeByte(message[byteIndex], iterator);
            byteIndex++;
        }

        iterator.reset();
        byteIndex = 0;
        stepForward(iterator);
        while (iterator.hasNext() && byteIndex < message.length) {
            checkByte(iterator);
            byteIndex++;
        }

        byte[] changedPatterns = new byte[4];
        for (BitPattern pattern : BitPattern.values()) {
            changedPatterns[pattern.ordinal()] = (byte) (changedBitPattern.get(pattern) > 0 ? 0x1 : 0x0);
        }

        iterator.reset();
        for (int i = 0; i < 4; i++) {
            if (iterator.hasNext()) {
                PixelByte pixelByte = iterator.next();
                byte patternByte = (byte) ((pixelByte.getValue() & ~1) | changedPatterns[i]);
                iterator.setByte(patternByte);
            } else {
                throw new MessageToLargeException("Data is too big for carrier");
            }
        }
    }

    @Override
    public void writeByte(byte b, BitmapIterator iterator) {
        int bitIndex = 7;
        while (bitIndex >= 0 && iterator.hasNext()) {
            PixelByte pixel = iterator.next();
            byte pixelValue = pixel.getValue();

            if (pixel.getColor() != Color.RED) {
                BitPattern bitPattern = BitPattern.getBitPattern(pixelValue);

                byte bit = (byte) ((b >> bitIndex) & 1);
                pixelValue = (byte) ((pixelValue & ~1) | bit);
                bitIndex--;

                int changed = iterator.setByte(pixelValue);
                changedBitPattern.computeIfPresent(bitPattern, (k, v) -> v + changed);
            }
        }
    }

    private void checkByte(BitmapIterator iterator) {
        int bitIndex = 7;
        while (bitIndex >= 0 && iterator.hasNext()) {
            PixelByte pixel = iterator.next();
            byte pixelValue = pixel.getValue();

            if (pixel.getColor() != Color.RED) {
                BitPattern bitPattern = BitPattern.getBitPattern(pixelValue);
                int changed = changedBitPattern.get(bitPattern);

                if (changed > 0) {
                    iterator.setByte((byte) (pixel.getValue() ^ 1));
                }

                bitIndex--;
            }
        }
    }

    private void stepForward(BitmapIterator iterator) {
        for (int i = 0; i < 4; i++) {
            if (!iterator.hasNext())
                throw new MessageToLargeException("Data is too big for carrier");
            iterator.next();
        }
    }

    @Override
    public int size(BitmapIterator iterator) {
        checkChangedBitPatterns(iterator);
        return super.size(iterator);
    }

    private void checkChangedBitPatterns(BitmapIterator iterator) {
        for (int i = 0; i < 4; i++) {
            if (iterator.hasNext()) {
                Byte b = iterator.next().getValue();

                if ((b & 0x1) == 1)
                    changedBitPattern.put(BitPattern.values()[i], 1);
                else
                    changedBitPattern.put(BitPattern.values()[i], 0);
            }
        }
    }

    @Override
    public Byte readByte(BitmapIterator iterator) {
        int bitIndex = 7;
        int currentByte = 0;

        while (iterator.hasNext() && bitIndex >= 0) {
            PixelByte pixel = iterator.next();
            byte pixelValue = pixel.getValue();

            if (pixel.getColor() != Color.RED) {
                BitPattern bitPattern = BitPattern.getBitPattern(pixelValue);
                int changed = changedBitPattern.get(bitPattern);
                if (changed > 0)
                    pixelValue = (byte) (pixelValue ^ 1);

                byte bit = (byte) (pixelValue & 1);
                currentByte |= (bit << bitIndex);

                bitIndex--;
            }
        }

        return bitIndex < 0 ? (byte) currentByte : null;
    }

    private enum BitPattern {
        ZERO_ZERO, ZERO_ONE, ONE_ZERO, ONE_ONE;

        public static BitPattern getBitPattern(Byte pixelByte) {
            switch (pixelByte & 0b0000_0110) {
                case 0b0000_0000 -> {
                    return ZERO_ZERO;
                }
                case 0b0000_0010 -> {
                    return ZERO_ONE;
                }
                case 0b0000_0100 -> {
                    return ONE_ZERO;
                }
                case 0b0000_0110 -> {
                    return ONE_ONE;
                }
                default -> throw new IllegalStateException("Unexpected value: " + pixelByte);
            }
        }
    }
}
