package ar.edu.itba.cripto.encryption;

public interface EncryptionX {
    public byte[] encrypt(byte[] data, String pass);

    public byte[] decrypt(byte[] encriptedData, String pass);
}
