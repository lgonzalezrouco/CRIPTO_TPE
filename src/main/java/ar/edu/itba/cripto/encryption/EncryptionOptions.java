package ar.edu.itba.cripto.encryption;

public class EncryptionOptions {

    private final EncryptionAlgorithm algorithm;
    private final EncryptionMode mode;
    private final String password;

    public EncryptionOptions(EncryptionAlgorithm algorithm, EncryptionMode mode, String password) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.password = password;
    }
}
