package ar.edu.itba.cripto;

import ar.edu.itba.cripto.arguments.Actions;
import ar.edu.itba.cripto.arguments.Parser;
import ar.edu.itba.cripto.arguments.Parser.Arguments;
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

        if (arguments.action() == Actions.EXTRACT)
            extract(arguments, lsb, bitmap);
        else
            embed(arguments, lsb, bitmap);
    }

    public static void embed(Arguments args, LSB lsb, Bitmap bitmap) throws IOException {
        try (InputStream stream = new FileInputStream(args.inputFile())) {
            byte[] data = stream.readAllBytes();
            String extension = args.getExtension(args.inputFile());

            byte[] dataToEmbed = lsb.getBytesToHide(data, extension);
            // dataToEmbed: size (4) | data | extension

            if (args.encryptionOptions().getPassword() != null) {
                // dataToEmbed: cipherSize (4) | cipherData
                // cipherData: size (4) | data | extension
                try {
                    dataToEmbed = args.encryptionOptions().encrypt(dataToEmbed);
                } catch (Exception e) {
                    throw new RuntimeException("Error encrypting data", e);
                }
                dataToEmbed = lsb.getBytesToHide(dataToEmbed);
            }

            int maxDataSize = bitmap.getPixelDataSize() / lsb.getBitsToHidePerPixel();
            if (dataToEmbed.length > maxDataSize) {
                throw new IllegalArgumentException("Data is too big for carrier");
            }

            lsb.hide(bitmap, dataToEmbed, args.getExtension(args.inputFile()));
            bitmap.saveToFile(new File(args.outputFile()));
        }
    }

    public static void extract(Arguments args, LSB lsb, Bitmap bitmap) throws IOException {
        byte[] extractedData = lsb.extract(bitmap);

        if (args.encryptionOptions().getPassword() != null) {
            try {
                extractedData = args.encryptionOptions().decrypt(extractedData);
                EmbeddedFile embeddedFile = lsb.parseToEmbeddedFile(extractedData);
                extractedData = embeddedFile.getData();
            } catch (Exception e) {
                throw new RuntimeException("Error decrypting data", e);
            }
        }

        try (OutputStream stream = new FileOutputStream(args.outputFile())) {
            stream.write(extractedData);
        }
    }
}