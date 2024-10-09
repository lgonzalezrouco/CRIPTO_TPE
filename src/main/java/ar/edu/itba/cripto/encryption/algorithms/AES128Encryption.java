package ar.edu.itba.cripto.encryption.algorithms;

import ar.edu.itba.cripto.encryption.EncryptionX;
import lombok.Getter;

public class AES128Encryption implements EncryptionX {

    private static final int KEY_SIZE_BYTES = 16; // 128 bits

    @Getter
    private static final AES128Encryption instance = new AES128Encryption();

    public AES128Encryption() {

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
