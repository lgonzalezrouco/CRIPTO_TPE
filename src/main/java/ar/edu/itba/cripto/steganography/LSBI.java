package ar.edu.itba.cripto.steganography;

import ar.edu.itba.cripto.steganography.exceptions.MessageToLargeException;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;
import ar.edu.itba.cripto.utils.Color;
import ar.edu.itba.cripto.utils.PixelByte;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Optional;

public class LSBI implements LSB {

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
        if (message.length > availablePixels / 8) {
            throw new MessageToLargeException("Data is too big for carrier");
        }

        BitmapIterator iterator = new BitmapIterator(carrier);

        int byteIndex = 0;
        while (iterator.hasNext() && byteIndex < message.length) {
            writeByte(message[byteIndex], iterator);
            byteIndex++;
        }

        iterator = new BitmapIterator(carrier);
        byteIndex = 0;
        while (iterator.hasNext() && byteIndex < message.length) {
            checkByte(iterator);
            byteIndex++;
        }

        byte[] changedPatterns = new byte[4];
        for (BitPattern pattern : BitPattern.values()) {
            changedPatterns[pattern.ordinal()] = (byte) (changedBitPattern.get(pattern) > 0 ? 1 : 0);
        }

        byte[] newMessage = new byte[message.length + 1];

        newMessage[0] = (byte) (changedPatterns[0] << 3 | changedPatterns[1] << 2 | changedPatterns[2] << 1 | changedPatterns[3]);

        System.arraycopy(message, 0, newMessage, 1, message.length);

        iterator.getBitmap().setPixelData(newMessage);
    }

    @Override
    public void writeByte(byte b, BitmapIterator iterator) {
        int bitIndex = 7;
        while (iterator.hasNext() && bitIndex >= 0) {
            PixelByte pixel = iterator.next();
            byte pixelValue = pixel.getValue();
            if (pixel.getColor() != Color.RED) {
                BitPattern bitPattern = BitPattern.getBitPattern(pixel.getValue());


                byte bit = (byte) ((b >> bitIndex) & 1);
                pixelValue = (byte) ((pixelValue & ~1) | bit);
                bitIndex--;

                int changed = iterator.setByte(pixelValue);
                changedBitPattern.computeIfPresent(bitPattern, (k, v) -> v + changed);
            }
        }
    }

    private void checkByte(BitmapIterator iterator) {
        if (!iterator.hasNext())
            return;

        PixelByte pixel = iterator.next();
        if (pixel.getColor() != Color.RED) {
            BitPattern bitPattern = BitPattern.getBitPattern(pixel.getValue());
            int changed = changedBitPattern.get(bitPattern);
            if (changed > 0) {
                iterator.setByte((byte) (pixel.getValue() ^ 1));
            }
        }
    }

    @Override
    public byte[] extract(Bitmap carrier) {
        BitmapIterator iterator = new BitmapIterator(carrier);
        checkChangedBitPatterns(iterator);

        int msgSize = size(iterator);

        byte[] message = new byte[msgSize];
        int byteIndex = 0;
        while (iterator.hasNext() && byteIndex < msgSize) {
            Byte pixel = readByte(iterator);
            message[byteIndex] = Optional.ofNullable(pixel).orElse((byte) 0);
            byteIndex++;
        }

        return Arrays.copyOf(message, msgSize);
    }

    private void checkChangedBitPatterns(BitmapIterator iterator) {
        if (iterator.hasNext()) {
            Byte b = iterator.next().getValue();

            // check the 4 least significant bits
            // the last significant bit if for the 11, and the forth least significant bit is for 00
            byte[] changedPatterns = new byte[4];
            for (int i = 0; i < 4; i++) {
                changedPatterns[i] = (byte) ((b >> (3 - i)) & 1);

                if (changedPatterns[i] == 1)
                    changedBitPattern.put(BitPattern.values()[i], 1);
            }
        }
    }

    @Override
    public Byte readByte(BitmapIterator iterator) {
        int bitIndex = 7;
        int currentByte = 0;

        while (iterator.hasNext() && bitIndex >= 0) {
            PixelByte pixel = iterator.next();
            if (!pixel.getColor().equals(Color.RED)) {
                BitPattern bitPattern = BitPattern.getBitPattern(pixel.getValue());
                int changed = changedBitPattern.get(bitPattern);
                if (changed > 0)
                    iterator.setByte((byte) (pixel.getValue() ^ 1));

                byte pixelValue = pixel.getValue();

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
