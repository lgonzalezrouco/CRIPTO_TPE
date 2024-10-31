/*
import ar.edu.itba.cripto.encryption.EncryptionMode;
import ar.edu.itba.cripto.encryption.algorithms.AES192Encryption;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class AES192EncryptionTest {

    private final String message = "This is a test message";
    private final String pass = "password";

    @Test
    public void AES192EncryptionCBCModeTest() {
        AES192Encryption encryption = new AES192Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.CBC);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.CBC);
        Assertions.assertEquals(message, new String(decrypted));
    }

    @Test
    public void AES192EncryptionCFBModeTest() {
        AES192Encryption encryption = new AES192Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.CFB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.CFB);
        Assertions.assertEquals(message, new String(decrypted));
    }

    @Test
    public void AES192EncryptionEBCModeTest() {
        AES192Encryption encryption = new AES192Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.ECB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.ECB);
        Assertions.assertEquals(message, new String(decrypted));
    }

    @Test
    public void AES192EncryptionOFBModeTest() {
        AES192Encryption encryption = new AES192Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.OFB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.OFB);
        Assertions.assertEquals(message, new String(decrypted));
    }
}
*/
