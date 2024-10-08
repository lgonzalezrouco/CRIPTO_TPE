package ar.edu.itba.cripto.steganography;
import ar.edu.itba.cripto.utils.RGBIterator;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
public abstract class LSBX implements LSB {

    private final int bitsToHide;

    protected LSBX(int bitsToHide) {
        this.bitsToHide = bitsToHide;
    }



    @Override
    public void hide(BufferedImage carrier, byte[] data) {
        byte[] dataToHide = getBytesToHide(data, ".txt");
        RGBIterator iterator = new RGBIterator(carrier);

        int dataIndex = 0;
        int bitIndex = 0;
        int totalBits = dataToHide.length * 8; // Total number of bits to hide

        while (iterator.hasNext() && dataIndex * 8 + bitIndex < totalBits) {
            int[] colors = iterator.next(); // Get the next pixel's RGB values (as R, G, B)

            // Modify the R, G, and B channels to hide the data
            for (int i = 0; i < 3; i++) { // Traverse R, G, and B channels
                if (dataIndex * 8 + bitIndex >= totalBits) {
                    break; // Stop if we've hidden all the data
                }

                // Get the bits to hide
                int bitsAvailable = 8 - bitIndex;
                int bitsToWrite = Math.min(bitsToHide, bitsAvailable);
                int currentByte = dataToHide[dataIndex] & 0xFF;

                // Extract the bits to hide from the current byte
                int bits = (currentByte >> (bitsAvailable - bitsToWrite)) & ((1 << bitsToWrite) - 1);

                // Clear the least significant bits and set the hidden bits in the current color channel
                colors[i] = (colors[i] & -(1 << bitsToWrite)) | bits;

                // Update the bit and data indices
                bitIndex += bitsToWrite;
                if (bitIndex >= 8) {
                    bitIndex = 0;
                    dataIndex++;
                }
            }

            // After modifying the colors, set the new RGB value in the image
            iterator.setRGB(colors);
        }
    }
    public byte[] extract(BufferedImage image) {
        int size = getMessageSize(image);
        byte[] bytes = new byte[4 + size + 4];
        int sizeIndex = 0;
        int bitIndex = 0;

        RGBIterator iterator = new RGBIterator(image);

        while (iterator.hasNext() && sizeIndex < size) {
            int[] colors = iterator.next();

            for (int color : colors) {
                // Get the least significant bits
                int lsbx = color & ((1 << bitsToHide) - 1);

                // Add bits to the current byte
                bytes[sizeIndex] |= (byte) (lsbx << bitIndex);

                // Update bitIndex and sizeIndex
                bitIndex += bitsToHide;
                if (bitIndex >= 8) {
                    bitIndex = 0;
                    sizeIndex++;
                }
            }
        }
        // dejemos afuera los primeros 4bytes y los ultimos 4

        return trimExtractedBytes(bytes);
    }

    private int getMessageSize(BufferedImage image) {
        byte[] sizeBytes = new byte[4]; // For storing the 4 bytes of size
        int sizeIndex = 0; // Current byte index
        int bitIndex = 0;  // Bit index inside the current byte

        RGBIterator iterator = new RGBIterator(image);

        while (iterator.hasNext() && sizeIndex < 4) {
            int[] colors = iterator.next(); // Get the R, G, B colors from the next pixel
            for (int color : colors) {
                // Extract the least significant bit
                int bit = color & 1;

                // Assign the extracted bit to the corresponding byte
                sizeBytes[sizeIndex] |= (byte) (bit << bitIndex);

                // Update bitIndex and sizeIndex
                bitIndex++;
                if (bitIndex == 8) {
                    bitIndex = 0;
                    sizeIndex++;
                }

                if (sizeIndex == 4) {
                    return ByteBuffer.wrap(sizeBytes).getInt(); // Convert the 4 bytes to an integer

                }
            }
        }

        throw new IllegalArgumentException();
    }


    private byte[] trimExtractedBytes(byte[] extractedBytes) {
        if (extractedBytes.length < 4) {
            throw new IllegalArgumentException("El arreglo debe tener al menos 4 bytes.");
        }
        int messageSize = ByteBuffer.wrap(extractedBytes, 0, 4).getInt();
        int totalLength = 4 + messageSize; // 4 bytes para el tamaño + tamaño del mensaje
        if (totalLength > extractedBytes.length) {
            throw new IllegalArgumentException("El tamaño del mensaje excede el tamaño del arreglo extraído.");
        }
        byte[] trimmedBytes = new byte[messageSize];
        System.arraycopy(extractedBytes, 4, trimmedBytes, 0, messageSize);
        return trimmedBytes;
    }
}