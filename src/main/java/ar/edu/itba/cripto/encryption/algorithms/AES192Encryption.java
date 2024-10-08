package ar.edu.itba.cripto.encryption.algorithms;

import ar.edu.itba.cripto.encryption.EncryptionX;

public class AES192Encryption implements EncryptionX {

    private static final int KEY_SIZE_BYTES = 16; // 128 bits

    private static final AES192Encryption instance = new AES192Encryption();

    public AES192Encryption() {

    }

    public static AES192Encryption getInstance() {
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
