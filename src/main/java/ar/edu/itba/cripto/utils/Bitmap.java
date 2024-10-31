package ar.edu.itba.cripto.utils;


import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Getter
public class Bitmap {

    private static final int HEADER_SIZE = 54;
    private final int width;
    private final int height;
    private final byte[] header;
    @Setter
    private byte[] pixelData;

    public Bitmap(int width, int height, byte[] header, byte[] data) {
        this.width = width;
        this.height = height;
        this.header = header;
        this.pixelData = data;
    }

    public static Bitmap loadFile(File path) throws IOException {
        try (InputStream stream = new FileInputStream(path)) {
            return readFromStream(stream);
        }
    }

    public static Bitmap readFromStream(InputStream stream) throws IOException {
        byte[] header = new byte[HEADER_SIZE];
        int bytesRead = stream.read(header);

        if (bytesRead != HEADER_SIZE) {
            throw new IOException("Invalid header size");
        }

        // check no compression
        if (header[29] != 0 || header[30] != 0) {
            throw new IOException("Compression not supported");
        }

        int width = ByteBuffer.wrap(header, 18, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        int height = ByteBuffer.wrap(header, 22, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        byte[] pixelData = new byte[width * height * 3];
        bytesRead = stream.read(pixelData);

        if (bytesRead != pixelData.length) {
            throw new IOException("Invalid pixel data size");
        }

        return new Bitmap(width, height, header, pixelData);
    }

    public void saveToFile(File path) throws IOException {
        try (OutputStream stream = new FileOutputStream(path)) {
            stream.write(header);
            stream.write(pixelData);
        }
    }

    public int getPixelDataSize() {
        return pixelData.length;
    }
}
