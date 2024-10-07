package ar.edu.itba.cripto.encryption;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncryptionOptions {
    private EncryptionAlgorithm algorithm;
    private EncryptionMode mode;
    private String password;

    public EncryptionOptions(EncryptionAlgorithm algorithm, EncryptionMode mode, String password) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.password = password;
    }
}
