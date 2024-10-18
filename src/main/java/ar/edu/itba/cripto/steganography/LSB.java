package ar.edu.itba.cripto.steganography;

import ar.edu.itba.cripto.utils.Bitmap;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public interface LSB {

    void hide(Bitmap carrier, byte[] message, String extension);

    int getBitsToHidePerPixel();

    byte[] extract(Bitmap carrier);

    default EmbeddedFile parseToEmbeddedFile(byte[] dataToParse) {
        // dataToParse: size (4) | data | extension
        ByteBuffer buffer = ByteBuffer.wrap(dataToParse);

        int size = buffer.getInt(); // it reads the first 4 bytes and moves the position

        byte[] message = new byte[size];
        buffer.get(message);

        byte[] extensionBytes = new byte[dataToParse.length - 4 - size];
        buffer.get(extensionBytes);
        String extension = new String(extensionBytes, StandardCharsets.UTF_8);

        return new EmbeddedFile(message, extension);
    }

    default byte[] getBytesToHide(byte[] message, String extension) {
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

    default byte[] getBytesToHide(byte[] message) {
        // Turn the message size into 4 bytes (big-endian)
        byte[] sizeBytes = ByteBuffer.allocate(4).putInt(message.length).array();

        ByteBuffer buffer = ByteBuffer.allocate(sizeBytes.length + message.length);
        buffer.put(sizeBytes);
        buffer.put(message);
        return buffer.array();
    }
}
