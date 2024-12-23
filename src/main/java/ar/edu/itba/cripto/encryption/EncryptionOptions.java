package ar.edu.itba.cripto.encryption;

import ar.edu.itba.cripto.encryption.exceptions.EncryptionException;

public record EncryptionOptions(EncryptionAlgorithm encryptionAlgorithm, EncryptionMode mode, String password) {

    public byte[] encrypt(byte[] data) throws EncryptionException {
        try {
            return encryptionAlgorithm.encrypt(data, password, mode);
        } catch (Exception e) {
            throw new EncryptionException("Error encrypting data: " + e.getMessage(), e);
        }
    }

    public byte[] decrypt(byte[] encryptedData) throws EncryptionException {
        try {
            return encryptionAlgorithm.decrypt(encryptedData, password, mode);
        } catch (Exception e) {
            throw new EncryptionException("Error decrypting data: " + e.getMessage(), e);
        }
    }
}
