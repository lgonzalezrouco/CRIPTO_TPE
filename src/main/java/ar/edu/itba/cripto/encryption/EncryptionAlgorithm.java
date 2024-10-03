package ar.edu.itba.cripto.encryption;

public enum EncryptionAlgorithm {

    AES128,
    AES192,
    AES256,
    DES3;

    public static EncryptionAlgorithm fromString(String algorithm) {
        return switch (algorithm) {
            case "aes128" -> AES128;
            case "aes192" -> AES192;
            case "aes256" -> AES256;
            case "3des" -> DES3;
            default -> throw new IllegalArgumentException("Invalid algorithm");
        };
    }
}
