package ar.edu.itba.cripto;

import ar.edu.itba.cripto.arguments.Actions;
import ar.edu.itba.cripto.arguments.Parser;
import ar.edu.itba.cripto.arguments.Parser.Arguments;
import ar.edu.itba.cripto.encryption.exceptions.EncryptionException;
import ar.edu.itba.cripto.steganography.EmbeddedFile;
import ar.edu.itba.cripto.steganography.LSB;
import ar.edu.itba.cripto.steganography.exceptions.MessageToLargeException;
import ar.edu.itba.cripto.utils.Bitmap;

import java.io.*;
import java.util.Arrays;
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

            if (args.encryptionOptions().password() != null) {
                // dataToEmbed: cipherSize (4) | cipherData
                // cipherData: size (4) | data | extension
                try {
                    dataToEmbed = args.encryptionOptions().encrypt(dataToEmbed);
                } catch (EncryptionException e) {
                    throw new RuntimeException("Error encrypting data", e);
                }
                dataToEmbed = lsb.getBytesToHide(dataToEmbed);
            }

            try {
                lsb.hide(bitmap, dataToEmbed, args.getExtension(args.inputFile()));
            } catch (MessageToLargeException e) {
                throw new RuntimeException("Error: Data is too big for carrier", e);
            }

            bitmap.saveToFile(new File(args.outputFile()));

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found");
        }
    }

    public static void extract(Arguments args, LSB lsb, Bitmap bitmap) throws IOException {
        byte[] extractedData = lsb.extract(bitmap);

        if (args.encryptionOptions().password() != null) {
            try {
                extractedData = Arrays.copyOfRange(extractedData, 4, extractedData.length);
                extractedData = args.encryptionOptions().decrypt(extractedData);
            } catch (Exception e) {
                throw new RuntimeException("Error decrypting data", e);
            }
        }

        EmbeddedFile embeddedFile = lsb.parseToEmbeddedFile(extractedData);

        try (OutputStream stream = new FileOutputStream(args.outputFile() + embeddedFile.getExtension())) {
            stream.write(embeddedFile.getData());
        }
    }
}