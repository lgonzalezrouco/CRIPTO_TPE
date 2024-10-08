package ar.edu.itba.cripto.steganography;

import java.awt.image.BufferedImage;

public class LSBI implements LSB {

    @Override
    public void hide(BufferedImage carrier, byte[] message) {
    }

    @Override
    public byte[] extract(BufferedImage carrier) {
        return new byte[0];
    }
}
