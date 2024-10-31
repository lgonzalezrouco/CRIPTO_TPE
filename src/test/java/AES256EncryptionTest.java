import ar.edu.itba.cripto.encryption.EncryptionMode;
import ar.edu.itba.cripto.encryption.algorithms.AES256Encryption;
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

public class AES256EncryptionTest {

    private final String message = "This is a test message";
    private final String pass = "password";

    private final String hexaKey = "714B51F37D3FDE9DD7F4202033A1BFCBB5B5CC5BB6F94F6E8726FE3431F83E95";
    private final String hexaIV = "ECD56FF2C115CB8AE947C7FC8766C25F";

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
    public void AES256EncryptionCBCModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        AES256Encryption encryption = new AES256Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.CBC);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.CBC);
        Assertions.assertEquals(message, new String(decrypted));

        SecretKeySpec secretKey = new SecretKeySpec(opensslKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(opensslIV);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted2 = cipher.doFinal(encrypted);
        Assertions.assertEquals(message, new String(decrypted2));

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted2 = cipher.doFinal(message.getBytes());
        Assertions.assertEquals(new String(encrypted), new String(encrypted2));
        byte[] decrypted3 = encryption.decrypt(encrypted2, pass, EncryptionMode.CBC);
        Assertions.assertEquals(message, new String(decrypted3));
    }

    @Test
    public void AES256EncryptionCFBModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        AES256Encryption encryption = new AES256Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.CFB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.CFB);
        Assertions.assertEquals(message, new String(decrypted));

        SecretKeySpec secretKey = new SecretKeySpec(opensslKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(opensslIV);

        System.out.println();
        Cipher cipher=Cipher.getInstance("AES/CFB8/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey,ivSpec);
        byte[] decrypted2=cipher.doFinal(encrypted);

        Assertions.assertEquals(message, new String(decrypted2));

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted2 = cipher.doFinal(message.getBytes());
        Assertions.assertEquals(new String(encrypted), new String(encrypted2));
        byte[] decrypted3 = encryption.decrypt(encrypted2, pass, EncryptionMode.CFB);
        Assertions.assertEquals(message, new String(decrypted3));
    }

    @Test
    public void AES256EncryptionEBCModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        AES256Encryption encryption = new AES256Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.ECB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.ECB);
        Assertions.assertEquals(message, new String(decrypted));

        //Desencripto con las claves de openssl lo que encripte con mi funcion
        SecretKeySpec secretKey = new SecretKeySpec(opensslKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted2 = cipher.doFinal(encrypted);
        Assertions.assertEquals(message, new String(decrypted2));

        //Encripto con las claves de openssl y comparo con lo que cifre con mi funcion
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted2 = cipher.doFinal(message.getBytes());
        Assertions.assertEquals(new String(encrypted), new String(encrypted2));
        byte[] decrypted3 = encryption.decrypt(encrypted2, pass, EncryptionMode.ECB);
        Assertions.assertEquals(message, new String(decrypted3));
    }

    @Test
    public void AES256EncryptionOFBModeTest() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        AES256Encryption encryption = new AES256Encryption();
        byte[] encrypted = encryption.encrypt(message.getBytes(), pass, EncryptionMode.OFB);
        Assertions.assertNotEquals(message, new String(encrypted));
        byte[] decrypted = encryption.decrypt(encrypted, pass, EncryptionMode.OFB);
        Assertions.assertEquals(message, new String(decrypted));

        SecretKeySpec secretKey = new SecretKeySpec(opensslKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(opensslIV);
        Cipher cipher = Cipher.getInstance("AES/OFB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted2 = cipher.doFinal(encrypted);
        Assertions.assertEquals(message, new String(decrypted2));

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted2 = cipher.doFinal(message.getBytes());
        Assertions.assertEquals(new String(encrypted), new String(encrypted2));
        byte[] decrypted3 = encryption.decrypt(encrypted2, pass, EncryptionMode.OFB);
        Assertions.assertEquals(message, new String(decrypted3));
    }
}
