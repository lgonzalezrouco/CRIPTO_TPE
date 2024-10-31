package ar.edu.itba.cripto.steganography;

import ar.edu.itba.cripto.steganography.exceptions.MessageToLargeException;
import ar.edu.itba.cripto.steganography.exceptions.NoSizeFoundException;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class LSB {

    public abstract void hide(Bitmap carrier, byte[] message, String extension) throws MessageToLargeException;

    public byte[] extractWithExtension(Bitmap carrier) {
        BitmapIterator iterator = new BitmapIterator(carrier);
        byte[] extracted = extractMessage(iterator);
        byte[] extensionBytes = extractExtension(iterator);

        ByteBuffer buffer = ByteBuffer.allocate(4 + extracted.length + extensionBytes.length);
        buffer.putInt(extracted.length);
        buffer.put(extracted);
        buffer.put(extensionBytes);

        return buffer.array();
    }

    public byte[] extract(Bitmap carrier) {
        BitmapIterator iterator = new BitmapIterator(carrier);
        byte[] extracted = extractMessage(iterator);

        ByteBuffer buffer = ByteBuffer.allocate(4 + extracted.length);
        buffer.putInt(extracted.length);
        buffer.put(extracted);

        return buffer.array();
    }

    private byte[] extractMessage(BitmapIterator iterator) {
        int msgSize = size(iterator);
        byte[] extracted = new byte[msgSize];
        int byteIndex = 0;
        Byte pixel;

        while (byteIndex < msgSize && (pixel = readByte(iterator)) != null) {
            extracted[byteIndex] = pixel;
            byteIndex++;
        }

        return extracted;
    }

    private byte[] extractExtension(BitmapIterator iterator) {
        List<Byte> extension = new ArrayList<>();
        Byte pixel;

        while ((pixel = readByte(iterator)) != null && pixel != '\0') {
            extension.add(pixel);
        }

        extension.add((byte) '\0');

        byte[] extensionBytes = new byte[extension.size()];
        for (int i = 0; i < extension.size(); i++) {
            extensionBytes[i] = extension.get(i);
        }

        return extensionBytes;
    }

    public abstract Byte readByte(BitmapIterator iterator);

    public abstract void writeByte(byte b, BitmapIterator iterator);

    public EmbeddedFile parseToEmbeddedFile(byte[] dataToParse) {
        // dataToParse: size (4) | data | extension
        ByteBuffer buffer = ByteBuffer.wrap(dataToParse);

        int size = buffer.getInt();

        byte[] message = new byte[size];

        if (size > dataToParse.length - 4)
            throw new MessageToLargeException("The size of the message is bigger than the data");

        buffer.get(message);
        byte[] extensionBytes = new byte[dataToParse.length - 4 - size - 1];
        buffer.get(extensionBytes);
        String extension = new String(extensionBytes, StandardCharsets.UTF_8);

        return new EmbeddedFile(message, extension);
    }

    public byte[] getBytesToHide(byte[] message, String extension) {
        if (!extension.startsWith("."))
            throw new IllegalArgumentException("The extension must start with a '.'");

        if (!extension.endsWith("\0"))
            extension += "\0";

        // Turn the message size into 4 bytes (big-endian)
        byte[] sizeBytes = ByteBuffer.allocate(4).putInt(message.length).array();

        // Turn the extension into bytes using UTF-8
        byte[] extensionBytes = extension.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(sizeBytes.length + message.length + extensionBytes.length);
        buffer.put(sizeBytes);
        buffer.put(message);
        buffer.put(extensionBytes);
        return buffer.array();
    }

    public byte[] getEncryptedBytesToHide(byte[] message) {
        // Turn the message size into 4 bytes (big-endian)
        byte[] sizeBytes = ByteBuffer.allocate(4).putInt(message.length).array();

        ByteBuffer buffer = ByteBuffer.allocate(sizeBytes.length + message.length);
        buffer.put(sizeBytes);
        buffer.put(message);
        return buffer.array();
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
        throw new NoSizeFoundException("No size found");
    }
}
