package ar.edu.itba.cripto.encryption;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public abstract class EncryptionAlgorithm {

    private static final int ITERATIONS = 10000;
    private static final byte[] SALT = new byte[8];
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";

    private final String algorithmPrefix;
    private final int ivSize;
    private final int keySize;

    public EncryptionAlgorithm(String algorithmPrefix, int ivSize, int keySize) {
        this.algorithmPrefix = algorithmPrefix;
        this.ivSize = ivSize;
        this.keySize = keySize;
    }

    public byte[] encrypt(byte[] data, String pass, EncryptionMode encryptionMode) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        KeyIvPair keyIvPair = generateKeyAndIv(pass);
        SecretKeySpec secretKey = keyIvPair.secretKey;
        IvParameterSpec ivSpec = keyIvPair.ivSpec;

        Cipher cipher = Cipher.getInstance(algorithmPrefix + encryptionMode.getName());
        if (encryptionMode == EncryptionMode.ECB) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        }
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] encryptedData, String pass, EncryptionMode encryptionMode) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        KeyIvPair keyIvPair = generateKeyAndIv(pass);
        SecretKeySpec secretKey = keyIvPair.secretKey;
        IvParameterSpec ivSpec = keyIvPair.ivSpec;

        Cipher cipher = Cipher.getInstance(algorithmPrefix + encryptionMode.getName());
        if (encryptionMode == EncryptionMode.ECB) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        }
        return cipher.doFinal(encryptedData);
    }

    private KeyIvPair generateKeyAndIv(String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
        PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), SALT, ITERATIONS, (keySize + ivSize) * 8);

        byte[] randBytes = keyFactory.generateSecret(spec).getEncoded();

        byte[] keyBytes = Arrays.copyOf(randBytes, keySize);
        byte[] ivBytes = Arrays.copyOfRange(randBytes, keySize, randBytes.length);

        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, algorithmPrefix);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        return new KeyIvPair(secretKey, ivSpec);
    }

    private static class KeyIvPair {
        SecretKeySpec secretKey;
        IvParameterSpec ivSpec;

        KeyIvPair(SecretKeySpec secretKey, IvParameterSpec ivSpec) {
            this.secretKey = secretKey;
            this.ivSpec = ivSpec;
        }
    }
}