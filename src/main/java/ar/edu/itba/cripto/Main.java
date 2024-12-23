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
                    System.err.println("Error encrypting data: " + e);
                    return;
                }
                dataToEmbed = lsb.getEncryptedBytesToHide(dataToEmbed);
            }

            try {
                lsb.hide(bitmap, dataToEmbed, args.getExtension(args.inputFile()));
            } catch (MessageToLargeException e) {
                int maxMessageSize = lsb.maxMessageSize(bitmap);
                System.err.printf("Error: Data is too big for carrier, the max amount of bytes you can embed: %d", maxMessageSize);
                return;
            }

            bitmap.saveToFile(new File(args.outputFile()));

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found");
        }
    }

    public static void extract(Arguments args, LSB lsb, Bitmap bitmap) {
        byte[] extractedData;

        if (args.encryptionOptions().password() != null) {
            try {
                extractedData = lsb.extract(bitmap);
                extractedData = Arrays.copyOfRange(extractedData, 4, extractedData.length);
                extractedData = args.encryptionOptions().decrypt(extractedData);

            } catch (Exception e) {
                System.err.println("Error decrypting data: " + e);
                return;
            }
        } else {
            extractedData = lsb.extractWithExtension(bitmap);
        }

        EmbeddedFile embeddedFile = lsb.parseToEmbeddedFile(extractedData);
        try (OutputStream stream = new FileOutputStream(args.outputFile() + embeddedFile.getExtension())) {
            stream.write(embeddedFile.getData());
        } catch (Exception e) {
            System.err.println("There was a problem writing to file: " + e);
        }
    }
}