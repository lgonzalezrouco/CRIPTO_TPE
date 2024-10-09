package ar.edu.itba.cripto.steganography;

import ar.edu.itba.cripto.utils.Bitmap;

import java.awt.image.BufferedImage;

public class LSBI implements LSB {

    @Override
    public void hide(Bitmap carrier, byte[] message, String extension) {
    }

    @Override
    public byte[] extract(Bitmap carrier) {
        return new byte[0];
    }
}
