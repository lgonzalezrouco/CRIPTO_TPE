package ar.edu.itba.cripto.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BmpToArray {

    public static byte[] BmpToBytes(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }


    public static BufferedImage BmpToImage(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }
}
