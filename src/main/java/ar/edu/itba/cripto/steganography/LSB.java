package ar.edu.itba.cripto.steganography;

import ar.edu.itba.cripto.utils.Bitmap;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public interface LSB {


    void hide(Bitmap carrier, byte[] message, String extension);

    EmbeddedFile extract(Bitmap carrier);


    default byte[] getBytesToHide(byte[] message, String extension){
        // Validar la extensión
        if (!extension.startsWith(".")) {
            throw new IllegalArgumentException("La extensión debe comenzar con '.'");
        }
        if (!extension.endsWith("\0")) {
            extension += "\0";
        }

        // Convertir el ta  maño del mensaje a 4 bytes (big-endian)
        byte[] sizeBytes = ByteBuffer.allocate(4).putInt(message.length).array();

        // Convertir la extensión a bytes usando UTF-8
        byte[] extensionBytes = extension.getBytes(StandardCharsets.UTF_8);

        // Concatenar los arreglos usando ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(sizeBytes.length + message.length + extensionBytes.length);
        buffer.put(sizeBytes);
        buffer.put(message);
        buffer.put(extensionBytes);
        return buffer.array();
    }
}
