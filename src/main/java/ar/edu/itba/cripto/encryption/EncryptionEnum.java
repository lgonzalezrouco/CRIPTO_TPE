package ar.edu.itba.cripto.encryption;

import ar.edu.itba.cripto.encryption.algorithms.AES128Encryption;
import ar.edu.itba.cripto.encryption.algorithms.AES192Encryption;
import ar.edu.itba.cripto.encryption.algorithms.AES256Encryption;
import ar.edu.itba.cripto.encryption.algorithms.DES3Encryption;
import lombok.Getter;

@Getter
public enum EncryptionEnum {

    AES128(new AES128Encryption()),
    AES192(new AES192Encryption()),
    AES256(new AES256Encryption()),
    DES3(new DES3Encryption());

    private final EncryptionAlgorithm encryptionAlgorithm;

    EncryptionEnum(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public static EncryptionEnum fromString(String algorithm) {
        return switch (algorithm) {
            case "aes128" -> AES128;
            case "aes192" -> AES192;
            case "aes256" -> AES256;
            case "3des" -> DES3;
            default -> throw new IllegalArgumentException("Invalid algorithm");
        };
    }
}
