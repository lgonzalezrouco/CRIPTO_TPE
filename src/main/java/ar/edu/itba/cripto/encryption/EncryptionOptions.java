package ar.edu.itba.cripto.encryption;

import ar.edu.itba.cripto.encryption.exceptions.EncryptionException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncryptionOptions {

    private final EncryptionAlgorithm algorithm;

    private final EncryptionMode mode;

    private final String password;

    public EncryptionOptions(EncryptionAlgorithm algorithm, EncryptionMode mode, String password) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.password = password;
    }


    public byte[] encrypt(byte[] data) throws EncryptionException {
        try {
            return algorithm.getEncryptionX().encrypt(data, password, mode);
        } catch (Exception e) {
            throw new EncryptionException("Error encrypting data: " + e.getMessage(), e);
        }
    }

    public byte[] decrypt(byte[] encryptedData) throws EncryptionException {
        try {
            return algorithm.getEncryptionX().decrypt(encryptedData, password, mode);
        } catch (Exception e) {
            throw new EncryptionException("Error decrypting data: " + e.getMessage(), e);
        }
    }
}
