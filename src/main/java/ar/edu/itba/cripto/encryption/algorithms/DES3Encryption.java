package ar.edu.itba.cripto.encryption.algorithms;

import ar.edu.itba.cripto.encryption.EncryptionAlgorithm;

public class DES3Encryption extends EncryptionAlgorithm {

    private static final int KEY_SIZE_BYTES = 24; // 192 bits
    private static final int SALT_LONG = 8;

    public DES3Encryption() {
        super("DESede", SALT_LONG, KEY_SIZE_BYTES, "SHA-1");
    }
}
