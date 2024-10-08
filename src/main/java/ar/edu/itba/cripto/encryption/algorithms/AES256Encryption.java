package ar.edu.itba.cripto.encryption.algorithms;

import ar.edu.itba.cripto.encryption.EncryptionX;

public class AES256Encryption implements EncryptionX {

    private static final int KEY_SIZE_BYTES = 16; // 128 bits

    private static final AES256Encryption instance = new AES256Encryption();

    public AES256Encryption() {

    }

    public static AES256Encryption getInstance() {
        return instance;
    }

    @Override
    public byte[] encrypt(byte[] data, String pass) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] encriptedData, String pass) {
        return new byte[0];
    }
}
