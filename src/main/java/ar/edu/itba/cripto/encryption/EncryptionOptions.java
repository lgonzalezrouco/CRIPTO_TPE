package ar.edu.itba.cripto.encryption;

import ar.edu.itba.cripto.encryption.exceptions.EncryptionException;
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


    public byte[] encrypt(byte[] data) throws EncryptionException {
        try {
            return algorithm.getEncryptionX().encrypt(data, password);
        } catch (Exception e) {
            throw new EncryptionException("Error encrypting data: " + e.getMessage(), e);
        }
    }

    public byte[] decrypt(byte[] encryptedData) throws EncryptionException {
        try {
            return algorithm.getEncryptionX().decrypt(encryptedData, password);
        } catch (Exception e) {
            throw new EncryptionException("Error decrypting data: " + e.getMessage(), e);
        }
    }
}
