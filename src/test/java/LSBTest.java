import ar.edu.itba.cripto.encryption.EncryptionEnum;
import ar.edu.itba.cripto.encryption.EncryptionMode;
import ar.edu.itba.cripto.encryption.EncryptionOptions;
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
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LSBTest {

    private final static byte[] DATA = new byte[]{(byte) 0b01111110};
    private final static String EXTENSION = ".txt\0";
    private final static EncryptionOptions ENCRYPTION_OPTIONS = new EncryptionOptions(EncryptionEnum.AES128, EncryptionMode.CBC, "password");
    private Bitmap bitmap;

    @BeforeEach
    public void setUp() throws IOException {
        bitmap = Bitmap.loadFile(new File("src/main/resources/ejemplo2024/lado.bmp"));
        // bitmap = Bitmap.loadFile(new File("src/test/resources/sample1.bmp"));

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
        byte[] extracted = lsb.extractWithExtension(bitmap);

        Assertions.assertArrayEquals(dataToEmbed, extracted);
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
        byte[] extracted = lsb.extractWithExtension(bitmap);

        Assertions.assertArrayEquals(dataToEmbed, extracted);
    }

    @Test
    void lsb1EncryptionTest() throws IOException {
        LSB lsb = new LSB1();

        byte[] dataToEmbed = lsb.getBytesToHide(DATA, EXTENSION);
        byte[] encryptedData = ENCRYPTION_OPTIONS.encrypt(dataToEmbed);
        encryptedData = lsb.getEncryptedBytesToHide(encryptedData);

        lsb.hide(bitmap, encryptedData, EXTENSION);
        bitmap.saveToFile(new File("src/test/resources/result1.bmp"));

        byte[] extracted = lsb.extract(bitmap);
        extracted = Arrays.copyOfRange(extracted, 4, extracted.length);
        extracted = ENCRYPTION_OPTIONS.decrypt(extracted);

        Assertions.assertArrayEquals(dataToEmbed, extracted);
    }

    @Test
    void lsb4EncryptionTest() throws IOException {
        LSB lsb = new LSB4();

        byte[] dataToEmbed = lsb.getBytesToHide(DATA, EXTENSION);
        byte[] encryptedData = ENCRYPTION_OPTIONS.encrypt(dataToEmbed);
        encryptedData = lsb.getEncryptedBytesToHide(encryptedData);

        lsb.hide(bitmap, encryptedData, EXTENSION);
        bitmap.saveToFile(new File("src/test/resources/result1.bmp"));

        byte[] extracted = lsb.extract(bitmap);
        extracted = Arrays.copyOfRange(extracted, 4, extracted.length);
        extracted = ENCRYPTION_OPTIONS.decrypt(extracted);

        Assertions.assertArrayEquals(dataToEmbed, extracted);
    }

    @Test
    void lsbiEncryptionTest() throws IOException {
        LSB lsb = new LSBI();

        byte[] dataToEmbed = lsb.getBytesToHide(DATA, EXTENSION);
        byte[] encryptedData = ENCRYPTION_OPTIONS.encrypt(dataToEmbed);
        encryptedData = lsb.getEncryptedBytesToHide(encryptedData);

        lsb.hide(bitmap, encryptedData, EXTENSION);
        bitmap.saveToFile(new File("src/test/resources/result1.bmp"));

        byte[] extracted = lsb.extract(bitmap);
        extracted = Arrays.copyOfRange(extracted, 4, extracted.length);
        extracted = ENCRYPTION_OPTIONS.decrypt(extracted);

        Assertions.assertArrayEquals(dataToEmbed, extracted);
    }


}
