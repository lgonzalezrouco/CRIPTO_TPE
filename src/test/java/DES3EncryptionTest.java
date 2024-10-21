import ar.edu.itba.cripto.encryption.EncryptionMode;
import ar.edu.itba.cripto.encryption.algorithms.DES3Encryption;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class DES3EncryptionTest {

    private final String message = "This is a test message";
    private final String pass = "password";

    @Test
    public void DES3EncryptionCBCModeTest() {
        DES3Encryption encryption = new DES3Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.CBC);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.CBC);
        Assertions.assertEquals(message, new String(decrypted));
    }

    @Test
    public void DES3EncryptionCFBModeTest() {
        DES3Encryption encryption = new DES3Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.CFB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.CFB);
        Assertions.assertEquals(message, new String(decrypted));
    }

    @Test
    public void DES3EncryptionEBCModeTest() {
        DES3Encryption encryption = new DES3Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.ECB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.ECB);
        Assertions.assertEquals(message, new String(decrypted));
    }

    @Test
    public void DES3EncryptionOFBModeTest() {
        DES3Encryption encryption = new DES3Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.OFB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.OFB);
        Assertions.assertEquals(message, new String(decrypted));
    }
}
