package ar.edu.itba.cripto.utils;

import lombok.Getter;

@Getter
public class PixelByte {

    Byte value;
    Color color;

    public PixelByte(Byte value, Color color) {
        this.value = value;
        this.color = color;
    }
}
