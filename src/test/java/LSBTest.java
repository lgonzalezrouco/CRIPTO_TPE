import ar.edu.itba.cripto.steganography.LSB;
import ar.edu.itba.cripto.steganography.LSB1;
import ar.edu.itba.cripto.steganography.LSB4;
import ar.edu.itba.cripto.steganography.LSBI;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LSBTest {

    private final static byte[] DATA = new byte[]{(byte) 0b01111110};
    private final static String EXTENSION = ".txt\0";
    private Bitmap bitmap;

    @BeforeEach
    public void setUp() throws IOException {
        bitmap = Bitmap.loadFile(new File("src/test/resources/sample1.bmp"));
    }

    @Test
    void lsbxExtractSizeTest() {
        LSB lsb1 = new LSB1();
        byte[] dataToEmbed = lsb1.getBytesToHide(DATA, EXTENSION);
        lsb1.hide(bitmap, dataToEmbed, EXTENSION);
        BitmapIterator iterator = new BitmapIterator(bitmap);
        int size = lsb1.size(iterator);
        assertEquals(1, size);
    }

    @Test
    void lsbiExtractSizeTest() {
        LSB lsbi = new LSBI();
        byte[] dataToEmbed = lsbi.getBytesToHide(DATA, EXTENSION);
        lsbi.hide(bitmap, dataToEmbed, EXTENSION);
        BitmapIterator iterator = new BitmapIterator(bitmap);
        int size = lsbi.size(iterator);
        assertEquals(1, size);
    }

    @Test
    void lsbiTest() throws IOException {
        LSB lsb = new LSBI();
        byte[] dataToEmbed = lsb.getBytesToHide(DATA, EXTENSION);
        lsb.hide(bitmap, dataToEmbed, EXTENSION);
        bitmap.saveToFile(new File("src/test/resources/result1.bmp"));
        byte[] extracted = lsb.extract(bitmap);

        Assertions.assertArrayEquals(DATA, extracted);
    }

    @Test
    void lsb4Test() throws IOException {
        LSB lsb = new LSB4();
        byte[] dataToEmbed = lsb.getBytesToHide(DATA, EXTENSION);
        lsb.hide(bitmap, dataToEmbed, EXTENSION);
        bitmap.saveToFile(new File("src/test/resources/result1.bmp"));
        byte[] extracted = lsb.extractWithExtension(bitmap);

        Assertions.assertArrayEquals(dataToEmbed, extracted);
    }

    @Test
    void lsb1Test() throws IOException {
        LSB lsb = new LSB1();
        byte[] dataToEmbed = lsb.getBytesToHide(DATA, EXTENSION);
        lsb.hide(bitmap, dataToEmbed, EXTENSION);
        bitmap.saveToFile(new File("src/test/resources/result1.bmp"));
        byte[] extracted = lsb.extract(bitmap);

        Assertions.assertArrayEquals(DATA, extracted);
    }
}
