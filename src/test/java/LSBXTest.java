import ar.edu.itba.cripto.steganography.LSB1;
import ar.edu.itba.cripto.steganography.LSB4;
import ar.edu.itba.cripto.steganography.LSBX;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LSBXTest {

    private final static byte[] DATA = new byte[]{(byte) 0b01111110};
    private final static String EXTENSION = ".txt\0";
    private Bitmap bitmap;

    @BeforeEach
    public void setUp() throws IOException {
        bitmap = Bitmap.loadFile(new File("src/test/resources/sample1.bmp"));
    }

    @Test
    void extractSizeTest() {
        LSBX lsb1 = new LSB1();
        byte[] dataToEmbed = lsb1.getBytesToHide(DATA, EXTENSION);
        lsb1.hide(bitmap, dataToEmbed, EXTENSION);
        BitmapIterator iterator = new BitmapIterator(bitmap);
        int size = lsb1.size(iterator);
        assertEquals(1, size);
    }

    @Test
    void lsb4Test() throws IOException {
        LSBX lsb = new LSB4();
        byte[] dataToEmbed = lsb.getBytesToHide(DATA, EXTENSION);
        lsb.hide(bitmap, dataToEmbed, EXTENSION);
        bitmap.saveToFile(new File("src/test/resources/result1.bmp"));
        byte[] extracted = lsb.extract(bitmap);

        Assertions.assertArrayEquals(DATA, extracted);
    }

    @Test
    void lsb1Test() throws IOException {
        LSBX lsb = new LSB1();
        byte[] dataToEmbed = lsb.getBytesToHide(DATA, EXTENSION);
        lsb.hide(bitmap, dataToEmbed, EXTENSION);
        bitmap.saveToFile(new File("src/test/resources/result1.bmp"));
        byte[] extracted = lsb.extract(bitmap);

        Assertions.assertArrayEquals(DATA, extracted);
    }
}
