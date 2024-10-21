package ar.edu.itba.cripto.utils;

public enum Color {
    BLUE(0), GREEN(1), RED(2);

    private final int index;

    Color(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }

    public Color nextColor() {
        return switch (this) {
            case BLUE -> GREEN;
            case GREEN -> RED;
            case RED -> BLUE;
        };
    }
}