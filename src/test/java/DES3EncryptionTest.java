import ar.edu.itba.cripto.encryption.EncryptionMode;
import ar.edu.itba.cripto.encryption.algorithms.DES3Encryption;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DES3EncryptionTest {

    private final String message = "This is a test message";
    private final String pass = "password";

    private final String hexaKey = "714B51F37D3FDE9DD7F4202033A1BFCBB5B5CC5BB6F94F6E";

    private final String hexaIV = "8726FE3431F83E95";

    private final byte[] opensslKey = hexaStringtoByteArray(hexaKey);
    private final byte[] opensslIV = hexaStringtoByteArray(hexaIV);

    private byte[] hexaStringtoByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Test
    public void DES3EncryptionCBCModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        DES3Encryption encryption = new DES3Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.CBC);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.CBC);
        Assertions.assertEquals(message, new String(decrypted));


        SecretKeySpec secretKey = new SecretKeySpec(opensslKey, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(opensslIV);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted2 = cipher.doFinal(encrypted);
        Assertions.assertEquals(message, new String(decrypted2));

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted2 = cipher.doFinal(message.getBytes());
        Assertions.assertEquals(new String(encrypted), new String(encrypted2));
    }

    @Test
    public void DES3EncryptionCFBModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        DES3Encryption encryption = new DES3Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.CFB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.CFB);
        Assertions.assertEquals(message, new String(decrypted));

        SecretKeySpec secretKey = new SecretKeySpec(opensslKey, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(opensslIV);
        Cipher cipher = Cipher.getInstance("DESede/CFB8/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted2 = cipher.doFinal(encrypted);
        Assertions.assertEquals(message, new String(decrypted2));

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted2 = cipher.doFinal(message.getBytes());
        Assertions.assertEquals(new String(encrypted), new String(encrypted2));


    }

    @Test
    public void DES3EncryptionEBCModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        DES3Encryption encryption = new DES3Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.ECB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.ECB);
        Assertions.assertEquals(message, new String(decrypted));

        //Desencripto con las claves de openssl lo que encripte con mi funcion
        SecretKeySpec secretKey = new SecretKeySpec(opensslKey, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted2 = cipher.doFinal(encrypted);
        Assertions.assertEquals(message, new String(decrypted2));

        //Encripto con las claves de openssl y comparo con lo que cifre con mi funcion
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted2 = cipher.doFinal(message.getBytes());
        Assertions.assertEquals(new String(encrypted), new String(encrypted2));
    }

    @Test
    public void DES3EncryptionOFBModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        DES3Encryption encryption = new DES3Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.OFB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.OFB);
        Assertions.assertEquals(message, new String(decrypted));

        SecretKeySpec secretKey = new SecretKeySpec(opensslKey, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(opensslIV);
        Cipher cipher = Cipher.getInstance("DESede/OFB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted2 = cipher.doFinal(encrypted);
        Assertions.assertEquals(message, new String(decrypted2));

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted2 = cipher.doFinal(message.getBytes());
        Assertions.assertEquals(new String(encrypted), new String(encrypted2));
    }
}
