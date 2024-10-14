package ar.edu.itba.cripto.steganography;

import lombok.Getter;

@Getter
public class EmbeddedFile {
    byte [] data;
    String extension;

    public EmbeddedFile(byte [] data, String extension){
        this.data = data;
        this.extension = extension;
    }
}
