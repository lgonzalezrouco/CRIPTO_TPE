
import ar.edu.itba.cripto.steganography.EmbeddedFile;
import ar.edu.itba.cripto.steganography.LSB1;
import ar.edu.itba.cripto.steganography.LSB4;
import ar.edu.itba.cripto.steganography.LSBX;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;
import ar.edu.itba.cripto.utils.PixelByte;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LSBXTest {


    private Bitmap bitmap;
    @BeforeEach
    public void setUp() throws IOException {

        bitmap = Bitmap.loadFile(new File("src/test/resources/sample1.bmp"));
    }

    @Test
    void extractSizeTest(){
        byte[] dataToEmbed = new byte[]{(byte) 0b01111110}; // 1 byte
        LSBX lsb1 = new LSB1();
        lsb1.hide(bitmap, dataToEmbed, ".txt\0");
        BitmapIterator iterator = new BitmapIterator(bitmap);
        int size = lsb1.size(iterator);
        assertEquals(1, size);

    }

    @Test
    void lsb4Test() throws IOException {

        byte[] dataToEmbed = new byte[]{(byte) 0b01111110}; // 1 byte

        LSBX lsb1 = new LSB4();
        lsb1.hide(bitmap, dataToEmbed, ".txt\0");
        bitmap.saveToFile(new File("src/test/resources/result1.bmp"));
        EmbeddedFile extracted = lsb1.extract(bitmap);

        Assertions.assertArrayEquals(dataToEmbed, extracted.getData());
    }

    @Test
    void lsb1Test() throws IOException {

        byte[] dataToEmbed = new byte[]{(byte) 0b01111110}; // 1 byte

        LSBX lsb1 = new LSB1();
        lsb1.hide(bitmap, dataToEmbed, ".txt\0");
        bitmap.saveToFile(new File("src/test/resources/result1.bmp"));
        EmbeddedFile extracted = lsb1.extract(bitmap);

        Assertions.assertArrayEquals(dataToEmbed, extracted.getData());
    }





}
