import ar.edu.itba.cripto.encryption.EncryptionMode;
import ar.edu.itba.cripto.encryption.algorithms.AES256Encryption;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AES256EncryptionTest {

    private final String message = "This is a test message";
    private final String pass = "password";

    @Test
    public void AES256EncryptionCBCModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        AES256Encryption encryption = new AES256Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.CBC);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.CBC);
        Assertions.assertEquals(message, new String(decrypted));
    }

    @Test
    public void AES256EncryptionCFBModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        AES256Encryption encryption = new AES256Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.CFB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.CFB);
        Assertions.assertEquals(message, new String(decrypted));
    }

    @Test
    public void AES256EncryptionEBCModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        AES256Encryption encryption = new AES256Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.ECB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.ECB);
        Assertions.assertEquals(message, new String(decrypted));
    }

    @Test
    public void AES256EncryptionOFBModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        AES256Encryption encryption = new AES256Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.OFB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.OFB);
        Assertions.assertEquals(message, new String(decrypted));
    }
}
