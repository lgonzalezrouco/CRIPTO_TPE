package ar.edu.itba.cripto;

import ar.edu.itba.cripto.arguments.Actions;
import ar.edu.itba.cripto.arguments.Parser.Arguments;
import ar.edu.itba.cripto.arguments.Parser;
import ar.edu.itba.cripto.steganography.EmbeddedFile;
import ar.edu.itba.cripto.steganography.LSB;
import ar.edu.itba.cripto.utils.Bitmap;

import java.io.*;
import java.util.Optional;

public class Main {


    public static void main(String[] args) throws IOException {
        Optional<Arguments> cli = new Parser().parse(args);

        if (cli.isEmpty()) {
            System.err.println("Error: Invalid arguments");
            return;
        }
        Arguments arguments = cli.get();

        String inputFile = arguments.inputFile();
        String carrierFile = arguments.carrierFile();

        LSB lsb = arguments.steganographyType().getAlgorithm();
        Bitmap bitmap = Bitmap.loadFile(new File(carrierFile));


        if (arguments.action() == Actions.EXTRACT) {
            EmbeddedFile data = lsb.extract(bitmap);
            try (OutputStream stream = new FileOutputStream(arguments.outputFile() + data.getExtension())) {
                stream.write(data.getData());
            }
        } else {
            String extension = arguments.getExtension();
            try (InputStream stream = new FileInputStream(inputFile)) {
                byte[] data = stream.readAllBytes();
                lsb.hide(bitmap, data, extension);
                bitmap.saveToFile(new File(arguments.outputFile()));
            }

        }

        System.out.println("Arguments: " + arguments);
    }
}