package ar.edu.itba.cripto.encryption;

import ar.edu.itba.cripto.encryption.exceptions.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public abstract class EncryptionAlgorithm {

    private final String algorithmPrefix;
    private final int saltLong;
    private final int keySize;
    private final String hashAlgorithm;

    public EncryptionAlgorithm(String algorithmPrefix, int saltLong, int keySize, String hashAlgorithm) {
        this.algorithmPrefix = algorithmPrefix;
        this.saltLong = saltLong;
        this.keySize = keySize;
        this.hashAlgorithm = hashAlgorithm;
    }

    public byte[] encrypt(byte[] data, String pass, EncryptionMode encryptionMode) {
        try {
            SecretKey key = generateKeyFromPassword(pass);
            Cipher cipher = Cipher.getInstance(algorithmPrefix + encryptionMode.getName());
            IvParameterSpec iv = new IvParameterSpec(new byte[saltLong]);
            if (encryptionMode == EncryptionMode.ECB) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            }
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new EncryptionException("Error at encrypting", e);
        }
    }

    public byte[] decrypt(byte[] encryptedData, String pass, EncryptionMode encryptionMode) {
        try {
            SecretKey key = generateKeyFromPassword(pass);
            Cipher cipher = Cipher.getInstance(algorithmPrefix + encryptionMode.getName());
            IvParameterSpec iv = new IvParameterSpec(new byte[saltLong]);
            if (encryptionMode == EncryptionMode.ECB) {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key, iv);
            }
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new EncryptionException("Error at decrypting", e);
        }
    }

    private SecretKey generateKeyFromPassword(String pass) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(hashAlgorithm);
        byte[] key = sha.digest(pass.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(Arrays.copyOf(key, keySize), algorithmPrefix);
    }
}
