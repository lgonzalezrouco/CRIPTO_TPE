package ar.edu.itba.cripto.encryption;

import lombok.Getter;

@Getter
public enum EncryptionMode {
    ECB("/ECB/PKCS5Padding"),
    CFB("/CFB/NoPadding"),
    OFB("/OFB/NoPadding"),
    CBC("/CBC/PKCS5Padding");

    private final String name;

    EncryptionMode(String name) {
        this.name = name;
    }
}
