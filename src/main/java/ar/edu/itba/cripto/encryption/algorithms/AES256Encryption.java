package ar.edu.itba.cripto.encryption.algorithms;

import ar.edu.itba.cripto.encryption.EncryptionAlgorithm;

public class AES256Encryption extends EncryptionAlgorithm {

    private static final int KEY_SIZE_BYTES = 32; // 256 bits
    private static final int IV_SIZE = 16;

    public AES256Encryption() {
        super("AES", IV_SIZE, KEY_SIZE_BYTES, "SHA-256");
    }
}
