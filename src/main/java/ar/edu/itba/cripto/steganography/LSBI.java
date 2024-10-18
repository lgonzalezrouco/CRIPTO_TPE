package ar.edu.itba.cripto.steganography;

import ar.edu.itba.cripto.utils.Bitmap;

public class LSBI implements LSB {

    @Override
    public void hide(Bitmap carrier, byte[] message, String extension) {
    }

    @Override
    public int getBitsToHidePerPixel() {
        return 0;
    }

    @Override
    public byte[] extract(Bitmap carrier) {
        return new byte[0];
    }
}
