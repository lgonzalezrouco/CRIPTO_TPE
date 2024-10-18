package ar.edu.itba.cripto.encryption;

public interface EncryptionX {

    byte[] encrypt(byte[] data, String pass, EncryptionMode encryptionMode);

    byte[] decrypt(byte[] encryptedData, String pass, EncryptionMode encryptionMode);


}
