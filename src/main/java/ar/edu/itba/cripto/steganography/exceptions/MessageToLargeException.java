package ar.edu.itba.cripto.steganography.exceptions;

public class MessageToLargeException extends RuntimeException {
    public MessageToLargeException(String message) {
        super(message);
    }
}
