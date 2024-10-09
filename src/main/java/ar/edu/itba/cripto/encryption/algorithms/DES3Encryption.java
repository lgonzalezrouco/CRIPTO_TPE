package ar.edu.itba.cripto.encryption.algorithms;

import ar.edu.itba.cripto.encryption.EncryptionX;

public class DES3Encryption implements EncryptionX {

    private static final int KEY_SIZE_BYTES = 16; // 128 bits

    private static final DES3Encryption instance = new DES3Encryption();

    public DES3Encryption() {

    }

    public static DES3Encryption getInstance() {
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
