package ar.edu.itba.cripto.steganography.exceptions;

public class NoSizeFoundException extends RuntimeException {
    public NoSizeFoundException(String noSizeFound) {
        super(noSizeFound);
    }
}
