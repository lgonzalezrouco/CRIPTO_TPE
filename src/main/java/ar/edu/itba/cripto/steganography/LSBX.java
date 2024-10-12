package ar.edu.itba.cripto.steganography;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;
import ar.edu.itba.cripto.utils.PixelByte;

import java.nio.ByteBuffer;

public abstract class LSBX implements LSB {

    protected final int bitsToHide;

    protected LSBX(int bitsToHide) {
        this.bitsToHide = bitsToHide;
    }

    @Override
    public void hide(Bitmap carrier, byte[] message, String extension) {
        byte[] hideData = getBytesToHide(message, extension);
        BitmapIterator iterator = new BitmapIterator(carrier);
        int byteIndex = 0;
        while(iterator.hasNext() && byteIndex < hideData.length){
            writeByte(hideData[byteIndex], iterator);
            byteIndex++;
        }
    }

    private void writeByte(byte b, BitmapIterator iterator){
        int bitIndex = 7;
        while(iterator.hasNext()){
            PixelByte pixel = iterator.next();
            byte pixelValue = pixel.getValue();
            for(int j = 0; j < bitsToHide; j++){
                byte bit = (byte) ((b >> bitIndex) & 1);
                pixelValue = (byte) ((pixelValue & ~(1 << j)) | (bit << j)); // Clear target bit and set it to the message bit
                bitIndex--;
                if(bitIndex < 0){
                    iterator.setByte(pixelValue);
                    return;
                }
            }
            iterator.setByte(pixelValue);
        }
    }

    public int size(BitmapIterator iterator){
        int byteIndex = 0;
        int bitIndex = 7;
        byte [] num = new byte[4];
        // extract first 4 bytes
        while (iterator.hasNext()){
            Byte pixel = readByte(iterator);
            num[byteIndex] = pixel;
            byteIndex++;
            if(byteIndex == 4){
                return ByteBuffer.wrap(num).getInt();
            }
        }
        throw new IllegalArgumentException("No size found");
    }

    private Byte readByte(BitmapIterator iterator) {
        int bitIndex = 7;
        int currentByte = 0;

        while(iterator.hasNext()){
            PixelByte pixel = iterator.next();
            byte pixelValue = pixel.getValue();
            for(int j = 0; j < bitsToHide; j++){
                byte bit = (byte) ((pixelValue >> j) & 1);
                currentByte |= (bit << bitIndex);
                bitIndex--;
                if(bitIndex < 0){
                    return (byte) currentByte;
                }
            }
        }
        return null;
    }

    @Override
    public EmbeddedFile extract(Bitmap carrier) {
        BitmapIterator iterator = new BitmapIterator(carrier);
        int msgSize = size(iterator);

        byte[] extracted = new byte[msgSize];
        int byteIndex = 0;
        while(iterator.hasNext() && byteIndex < msgSize){
            Byte pixel = readByte(iterator);
            extracted[byteIndex] = pixel;
            byteIndex++;
        }
        if (byteIndex != msgSize) {
            throw  new IllegalArgumentException();
        }

        return new EmbeddedFile(extracted, getExtension(iterator));
    }

    public String getExtension(BitmapIterator iterator){

        StringBuilder extension = new StringBuilder();
        while(iterator.hasNext()){
            Byte b = readByte(iterator);
            // add to extension until \0
            if(b == 0){
                break;
            }
            extension.append((char) b.byteValue());
        }
        return extension.toString();
    }
}