package ar.edu.itba.cripto.encryption.algorithms;

import ar.edu.itba.cripto.encryption.EncryptionAlgorithm;

public class AES192Encryption extends EncryptionAlgorithm {

    private static final int KEY_SIZE_BYTES = 24; // 192 bits
    private static final int SALT_LONG = 16;

    public AES192Encryption() {
        super("AES", SALT_LONG, KEY_SIZE_BYTES, "SHA-256");
    }
}
