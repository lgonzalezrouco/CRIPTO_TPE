package ar.edu.itba.cripto.steganography;

import lombok.Getter;

@Getter
public enum SteganographyType {
    LSB1(new LSB1()),
    LSB4(new LSB4()),
    LSBI(new LSBI());

    private final LSB algorithm;

    SteganographyType(LSB algorithm) {
        this.algorithm = algorithm;
    }
}
