/*
import ar.edu.itba.cripto.encryption.EncryptionMode;
import ar.edu.itba.cripto.encryption.algorithms.AES256Encryption;
import ar.edu.itba.cripto.encryption.algorithms.DES3Encryption;
import ar.edu.itba.cripto.steganography.*;
import ar.edu.itba.cripto.utils.Bitmap;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

*/
/**
 * Created by Mateo on 26/10/2020.
 * This class is used to test the steganography methods.
 * We use professors example images to test the methods.
 *//*

public class SteganographyTest {


    @Test
    public void lsb1() throws IOException {
        Bitmap bitmap = Bitmap.loadFile(new File("src/main/resources/ejemplo2024/ladoLSB1.bmp"));
        LSB lsb = new LSB1();
        byte[] data = lsb.extractWithExtension(bitmap);
        // parse to embedded file
        EmbeddedFile embeddedFile = lsb.parseToEmbeddedFile(data);
        // save to file
        try (OutputStream stream = new FileOutputStream("src/test/resources/results/LSB1Test" + embeddedFile.getExtension())) {
            stream.write(embeddedFile.getData());
        }
    }

    @Test
    public void lsb4() throws IOException {
        Bitmap bitmap = Bitmap.loadFile(new File("src/main/resources/ejemplo2024/ladoLSB4.bmp"));
        LSB lsb = new LSB4();
        byte[] data = lsb.extractWithExtension(bitmap);
        // parse to embedded file
        EmbeddedFile embeddedFile = lsb.parseToEmbeddedFile(data);
        // save to file
        try (OutputStream stream = new FileOutputStream("src/test/resources/results/LSB4Test" + embeddedFile.getExtension())) {
            stream.write(embeddedFile.getData());
        }
    }

    // test for lsbi simple
    @Test
    public void lsbiTest() throws IOException {
        Bitmap bitmap = Bitmap.loadFile(new File("src/main/resources/ejemplo2024/ladoLSBI.bmp"));
        LSB lsb = new LSBI();
        byte[] data = lsb.extractWithExtension(bitmap);
        // parse to embedded file
        EmbeddedFile embeddedFile = lsb.parseToEmbeddedFile(data);
        // save to file
        try (OutputStream stream = new FileOutputStream("src/test/resources/results/LSBI" + embeddedFile.getExtension())) {
            stream.write(embeddedFile.getData());
        }
    }
    @Test
    public void fileTests() throws IOException {
        Bitmap bitmap = Bitmap.loadFile(new File("src/main/resources/grupo14/avatar.bmp"));
        LSB lsb = new LSBI();
        byte[] data = lsb.extractWithExtension(bitmap);
        // parse to embedded file
        EmbeddedFile embeddedFile = lsb.parseToEmbeddedFile(data);
        // save to file
        try (OutputStream stream = new FileOutputStream("src/test/resources/results/avatar" + embeddedFile.getExtension())) {
            stream.write(embeddedFile.getData());
        }
    }

    @Test
    public void lsbiEncryptedAes() throws IOException {
        Bitmap bitmap = Bitmap.loadFile(new File("src/main/resources/ejemplo2024/ladoLSBIaes256ofb.bmp"));
        LSB lsb = new LSBI();
        byte[] data = lsb.extract(bitmap);
        data = Arrays.copyOfRange(data, 4, data.length);
        AES256Encryption aes256Encryption = new AES256Encryption();
        byte[] decrypted = aes256Encryption.decrypt(data, "margarita", EncryptionMode.OFB);
        // parse to embedded file
        EmbeddedFile embeddedFile = lsb.parseToEmbeddedFile(decrypted);
        // save to file
        try (OutputStream stream = new FileOutputStream("src/test/resources/results/LSBIaes256ofb" + embeddedFile.getExtension())) {
            stream.write(embeddedFile.getData());
        }
    }

    @Test
    public void lsbiEncryptedDes() throws IOException {
        Bitmap bitmap = Bitmap.loadFile(new File("src/main/resources/ejemplo2024/ladoLSBIdescfb.bmp"));
        LSB lsb = new LSBI();
        byte[] data = lsb.extract(bitmap);
        data = Arrays.copyOfRange(data, 4, data.length);
        DES3Encryption encryptionOptions = new DES3Encryption();
        byte[] decrypted = encryptionOptions.decrypt(data, "margarita", EncryptionMode.CFB);
        // parse to embedded file
        EmbeddedFile embeddedFile = lsb.parseToEmbeddedFile(decrypted);
        // save to file
        try (OutputStream stream = new FileOutputStream("src/test/resources/results/LSBIdescfb" + embeddedFile.getExtension())) {
            stream.write(embeddedFile.getData());
        }
    }
    @Test
    public void lsbiSecretoTest() throws IOException {
        Bitmap bitmap = Bitmap.loadFile(new File("src/main/resources/grupo14/secreto1.bmp"));
        LSB lsb = new LSBI();
        byte[] data = lsb.extract(bitmap);
        data = Arrays.copyOfRange(data, 4, data.length);
        DES3Encryption aes256Encryption = new DES3Encryption();
        byte[] decrypted = aes256Encryption.decrypt(data, "metadata", EncryptionMode.OFB);
        // parse to embedded file
        EmbeddedFile embeddedFile = lsb.parseToEmbeddedFile(decrypted);
        // save to file
        try (OutputStream stream = new FileOutputStream("src/test/resources/results/metadata" + embeddedFile.getExtension())) {
            stream.write(embeddedFile.getData());
        }
    }

}
*/
