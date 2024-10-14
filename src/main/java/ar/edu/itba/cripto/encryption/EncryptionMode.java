package ar.edu.itba.cripto.encryption;

public enum EncryptionMode {
    ECB("/ECB/PKCS5Padding"),
    CFB("/CFB/NoPadding"),
    OFB("/OFB/NoPadding"),
    CBC("/CBC/PKCS5Padding");

    String name;

    EncryptionMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
