
import ar.edu.itba.cripto.steganography.LSB1;
import ar.edu.itba.cripto.steganography.LSBX;
import ar.edu.itba.cripto.utils.Bitmap;
import ar.edu.itba.cripto.utils.BitmapIterator;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LSBXTest {


    private Bitmap bitmap;
    @BeforeEach
    public void setUp(){

        byte[] header = new byte[54]; // Encabezado vacío
        // init 4x7 pixeles * 3 bytes all in 0
        byte[] pixelData = new byte[4 * 7 * 3];
        Arrays.fill(pixelData, (byte) 0);

        bitmap = new Bitmap(4, 7, header, pixelData);
    }

    @Test
    void extractSizeTest(){
        byte[] dataToEmbed = new byte[]{(byte) 0b01111110}; // 1 byte


        LSBX lsb1 = new LSB1();
        byte[] data = lsb1.getBytesToHide(dataToEmbed, ".txt\0");

        lsb1.hide(bitmap, dataToEmbed, ".txt\0");
        bitmap.dumpHex();

        BitmapIterator iterator = new BitmapIterator(bitmap);
        int size = lsb1.size(iterator);

        // assertEquals
        assertEquals(1, size);

    }


    @Test
     void extractTest(){

        byte[] dataToEmbed = new byte[]{(byte) 0b01111110}; // 1 byte

        LSBX lsb1 = new LSB1();
        lsb1.hide(bitmap, dataToEmbed, ".txt\0");
        byte[] extracted = lsb1.extract(bitmap);

        Assertions.assertArrayEquals(dataToEmbed, extracted);
    }

}
