package ar.edu.itba.cripto.arguments;

import ar.edu.itba.cripto.encryption.EncryptionAlgorithm;
import ar.edu.itba.cripto.encryption.EncryptionMode;
import ar.edu.itba.cripto.encryption.EncryptionOptions;
import ar.edu.itba.cripto.steganography.SteganographyType;
import org.apache.commons.cli.*;

import java.util.Optional;

public class Parser {

    private final CommandLineParser parser = new DefaultParser();

    private final Options options = new Options();

    public Parser() {
        options.addOptionGroup(Actions.getOptions());
        options.addOption("in", "in", true, "Input file");
        options.addRequiredOption("p", "p", true, "Carrier file");
        options.addRequiredOption("out", "out", true, "Output file");
        options.addRequiredOption("steg", "steg", true, "Steganography algorithm");
        options.addOption("a", "Encryption algorithm");
        options.addOption("m", "Encryption mode");
        options.addOption("pass", true, "Encryption key");
    }

    public Optional<Arguments> parse(String[] args) {
        try {
            return getArguments(parser.parse(options, args, true));
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.err.println("Error parsing command line arguments");
            return Optional.empty();
        }
    }

    private Optional<Arguments> getArguments(CommandLine cmd) {
        Actions action = cmd.hasOption("embed") ? Actions.EMBED : Actions.EXTRACT;
        String inputFile = cmd.getOptionValue("in");
        String carrierFile = cmd.getOptionValue("p");
        String outputFile = cmd.getOptionValue("out");
        SteganographyType steganographyType = SteganographyType.valueOf(cmd.getOptionValue("steg").toUpperCase());

        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.fromString(cmd.getOptionValue("a", "aes128").toUpperCase());
        EncryptionMode encryptionMode = EncryptionMode.valueOf(cmd.getOptionValue("m", "cbc").toUpperCase());
        String password = cmd.getOptionValue("pass");

        EncryptionOptions encryptionOptions = new EncryptionOptions(
                encryptionAlgorithm,
                encryptionMode,
                password
        );


        Arguments args = new Arguments(action, inputFile, carrierFile, outputFile, steganographyType, encryptionOptions);

        return args.isValid() ? Optional.of(args) : Optional.empty();
    }

    public record Arguments(Actions action, String inputFile, String carrierFile, String outputFile,
                            SteganographyType steganographyType, EncryptionOptions encryptionOptions) {

        public boolean isValid() {
            if (action == Actions.EMBED && inputFile == null) {
                System.err.println("Input file is required");
                return false;
            } else if (action == Actions.EXTRACT && inputFile != null) {
                System.err.println("Input file is not required");
                return false;
            }
            return true;
        }
    }
}
