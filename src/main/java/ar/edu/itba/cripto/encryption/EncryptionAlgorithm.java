package ar.edu.itba.cripto.encryption;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public abstract class EncryptionAlgorithm {

    private static final int ITERATIONS = 10000;

    private final String algorithmPrefix;
    private final int ivSize;
    private final int keySize;
    private final String hashAlgorithm;

    public EncryptionAlgorithm(String algorithmPrefix, int ivSize, int keySize, String hashAlgorithm) {
        this.algorithmPrefix = algorithmPrefix;
        this.ivSize = ivSize;
        this.keySize = keySize;
        this.hashAlgorithm = hashAlgorithm;
    }

    public byte[] encrypt(byte[] data, String pass, EncryptionMode encryptionMode) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] saltBytes = new byte[8];

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), saltBytes, ITERATIONS, (keySize + ivSize) * 8);

        byte[] randBytes = keyFactory.generateSecret(spec).getEncoded();

        byte[] keyBytes = Arrays.copyOf(randBytes, keySize);
        byte[] ivBytes = Arrays.copyOfRange(randBytes, keySize, randBytes.length);

        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, algorithmPrefix);
        for (byte b : secretKey.getEncoded()) {
            System.out.printf("%02X", b);
        }
        System.out.println();

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        for (byte b : ivSpec.getIV()) {
            System.out.printf("%02X", b);
        }
        System.out.println();

        Cipher cipher = Cipher.getInstance(algorithmPrefix + encryptionMode.getName());
        if (encryptionMode == EncryptionMode.ECB) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        }
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] encryptedData, String pass, EncryptionMode encryptionMode) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] saltBytes = new byte[8];

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), saltBytes, ITERATIONS, (keySize + ivSize) * 8);

        byte[] randBytes = keyFactory.generateSecret(spec).getEncoded();

        byte[] keyBytes = Arrays.copyOf(randBytes, keySize);
        byte[] ivBytes = Arrays.copyOfRange(randBytes, keySize, randBytes.length);

        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, algorithmPrefix);

        for (byte b : secretKey.getEncoded()) {
            System.out.printf("%02X", b);
        }
        System.out.println();

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        for (byte b : ivSpec.getIV()) {
            System.out.printf("%02X", b);
        }
        System.out.println();

        Cipher cipher = Cipher.getInstance(algorithmPrefix + encryptionMode.getName());
        if (encryptionMode == EncryptionMode.ECB) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        }
        return cipher.doFinal(encryptedData);
    }

    private SecretKey generateKeyFromPassword(String pass) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(hashAlgorithm);
        byte[] key = sha.digest(pass.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(Arrays.copyOf(key, keySize), algorithmPrefix);
    }
}
