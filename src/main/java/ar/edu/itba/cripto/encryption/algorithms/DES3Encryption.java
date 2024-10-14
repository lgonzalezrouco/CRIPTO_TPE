package ar.edu.itba.cripto.encryption.algorithms;

import ar.edu.itba.cripto.encryption.EncryptionMode;
import ar.edu.itba.cripto.encryption.EncryptionX;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class DES3Encryption implements EncryptionX {

    private static final int KEY_SIZE_BYTES = 21; // 168 bits
    private static final int SALT_LONG = 8; // 128 bits

    private static final DES3Encryption instance = new DES3Encryption();

    public DES3Encryption() {

    }

    public static DES3Encryption getInstance() {
        return instance;
    }

    @Override
    public byte[] encrypt(byte[] data, String pass, EncryptionMode encryptionMode) {
        try {
            SecretKey key = generateKeyFromPassword(pass);
            Cipher cipher = Cipher.getInstance("DESede"+encryptionMode.getName());
            IvParameterSpec iv = new IvParameterSpec(new byte[SALT_LONG]);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, String pass, EncryptionMode encryptionMode) {
        try {
            SecretKey key = generateKeyFromPassword(pass);
            Cipher cipher = Cipher.getInstance("DESede"+encryptionMode.getName());
            IvParameterSpec iv = new IvParameterSpec(new byte[SALT_LONG]);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar", e);
        }
    }


    private SecretKey generateKeyFromPassword(String pass) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(pass.getBytes("UTF-8"));
        return new SecretKeySpec(Arrays.copyOf(key, KEY_SIZE_BYTES), "AES");
    }
}
