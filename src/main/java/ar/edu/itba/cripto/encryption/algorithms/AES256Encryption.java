package ar.edu.itba.cripto.encryption.algorithms;

import ar.edu.itba.cripto.encryption.EncryptionMode;
import ar.edu.itba.cripto.encryption.EncryptionX;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class AES256Encryption implements EncryptionX {

    private static final int KEY_SIZE_BYTES = 32; // 256 bits
    private static final int SALT_LONG = 8; // 128 bits

    private static final AES256Encryption instance = new AES256Encryption();

    public AES256Encryption() {

    }

    public static AES256Encryption getInstance() {
        return instance;
    }

    @Override
    public byte[] encrypt(byte[] data, String pass, EncryptionMode encryptionMode) {
        try {
            SecretKey key = generateKeyFromPassword(pass);
            Cipher cipher = Cipher.getInstance("AES"+encryptionMode.getName());
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
            Cipher cipher = Cipher.getInstance("AES"+encryptionMode.getName());
            IvParameterSpec iv = new IvParameterSpec(new byte[SALT_LONG]);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar", e);
        }
    }

    @Override
    public byte[] encrypt(byte[] data, String pass) {
        return encrypt(data,pass,EncryptionMode.CBC);
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, String pass) {
        return decrypt(encryptedData,pass,EncryptionMode.CBC);
    }

    private SecretKey generateKeyFromPassword(String pass) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(pass.getBytes("UTF-8"));
        return new SecretKeySpec(Arrays.copyOf(key, KEY_SIZE_BYTES), "AES");
    }
}
