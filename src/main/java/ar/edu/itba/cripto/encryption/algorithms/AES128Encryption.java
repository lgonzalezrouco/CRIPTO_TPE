package ar.edu.itba.cripto.encryption.algorithms;

import ar.edu.itba.cripto.encryption.EncryptionAlgorithm;
import lombok.Getter;

public class AES128Encryption extends EncryptionAlgorithm {

    private static final int KEY_SIZE_BYTES = 16; // 128 bits
    private static final int SALT_LONG = 16;

    @Getter
    private static final AES128Encryption instance = new AES128Encryption();

    public AES128Encryption() {
        super("AES", SALT_LONG, KEY_SIZE_BYTES, "SHA-256");
    }
}
