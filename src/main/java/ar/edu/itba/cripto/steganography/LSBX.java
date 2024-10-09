package ar.edu.itba.cripto.steganography;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;

import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class LSBX implements LSB {

    private final int bitsToHide;

    protected LSBX(int bitsToHide) {
        this.bitsToHide = bitsToHide;
    }

    @Override
    public void hide(Bitmap carrier, byte[] message, String extension) {
        byte[] hideData = getBytesToHide(message, extension);
        BitmapIterator iterator = new BitmapIterator(carrier);

        int byteIndex = 0;
        int bitIndex = 7;

        while(iterator.hasNext() && byteIndex < hideData.length){
            byte pixel = iterator.next();
            for(int j = 0; j < bitsToHide; j++){
                if(byteIndex < hideData.length){
                    byte bit = (byte) ((hideData[byteIndex] >> bitIndex) & 1);
                    pixel|= (byte) (bit << j);
                    bitIndex--;
                    if(bitIndex < 0){
                        bitIndex = 7;
                        byteIndex++;
                    }
                }
            }
            // set pixel
            iterator.setByte(pixel);
        }
    }

    public int size(BitmapIterator iterator){
        int byteIndex = 0;
        int bitIndex = 7;
        byte [] num = new byte[4];
        // extract first 4 bytes
        while (iterator.hasNext()){
            byte pixel = iterator.next();
            for(int j = 0; j < bitsToHide; j++) {
                byte bit = (byte) ((pixel >> j) & 1);
                num[byteIndex] |= (byte) (bit << bitIndex);
                bitIndex--;
                if(bitIndex < 0){
                    bitIndex = 7;
                    byteIndex++;
                    if(byteIndex == 4){
                        return ByteBuffer.wrap(num).getInt();
                    }
                }
            }
        }
        throw new IllegalArgumentException("No size found");
    }

    private String extension(BitmapIterator iterator){
        // Implement the logic to extract the extension if needed
        return "";
    }

    @Override
    public byte[] extract(Bitmap carrier) {
        BitmapIterator iterator = new BitmapIterator(carrier);
        int msgSize = size(iterator);
        byte[] extracted = new byte[msgSize];
        int byteIndex = 0;
        int bitIndex = 0;
        int currentByte = 0;

        while(iterator.hasNext()){
            byte pixel = iterator.next();
            for(int j = 0; j < bitsToHide; j++){
                byte bit = (byte) ((pixel >> j) & 1);
                currentByte |= (bit << bitIndex);
                bitIndex++;

                if(bitIndex >= 8){
                    extracted[byteIndex] = (byte) currentByte;
                    byteIndex++;
                    bitIndex = 0;
                    currentByte = 0;
                    if (byteIndex == msgSize) {
                        return extracted;
                    }
                }
            }
        }
        throw new IllegalArgumentException("No message found");
    }
}