package ar.edu.itba.cripto.steganography;

public abstract class LSBX implements LSB {

    private int bitsToHide;

    public LSBX(int i) {
        this.bitsToHide = i;
    }

    @Override
    public void hide(byte[] carrier, byte[] message) {

    }
}
