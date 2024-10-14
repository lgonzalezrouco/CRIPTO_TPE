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

        LSB lsb = arguments.steganographyType().getAlgorithm();
        Bitmap bitmap = Bitmap.loadFile(new File(arguments.carrierFile()));


        if (arguments.action() == Actions.EXTRACT) {
           extract(arguments, lsb, bitmap);
        } else {
            embed(arguments, lsb, bitmap);
        }
    }


    public static void embed(Arguments args, LSB lsb , Bitmap bitmap ) throws IOException {
        try (InputStream stream = new FileInputStream(args.inputFile())) {
            byte[] data = stream.readAllBytes();
            lsb.hide(bitmap, data, args.getExtension());
            bitmap.saveToFile(new File(args.outputFile()));
        }
    }

    public static void extract(Arguments args, LSB lsb , Bitmap bitmap) throws IOException {
        EmbeddedFile data = lsb.extract(bitmap);
        try (OutputStream stream = new FileOutputStream(args.outputFile() + data.getExtension())) {
            stream.write(data.getData());
        }
    }
}