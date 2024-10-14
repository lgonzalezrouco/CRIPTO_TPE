package ar.edu.itba.cripto.encryption;

public interface EncryptionX {
    public byte[] encrypt(byte[] data, String pass,EncryptionMode encryptionMode);

    public byte[] decrypt(byte[] encryptedData, String pass,EncryptionMode encryptionMode);


}
