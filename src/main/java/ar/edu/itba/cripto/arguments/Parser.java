package ar.edu.itba.cripto.arguments;

import ar.edu.itba.cripto.encryption.EncryptionEnum;
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
        options.addOption("pass", "pass", true, "Encryption key");
        options.addOption("a", "a", true, "Encryption algorithm");
        options.addOption("m", "m", true, "Encryption mode");
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
        String password = cmd.getOptionValue("pass");
        String carrierFile = cmd.getOptionValue("p");
        String outputFile = cmd.getOptionValue("out");
        SteganographyType steganographyType = SteganographyType.valueOf(cmd.getOptionValue("steg").toUpperCase());

        EncryptionEnum encryptionEnum = EncryptionEnum.fromString(cmd.getOptionValue("a", "aes128"));
        EncryptionMode encryptionMode = EncryptionMode.valueOf(cmd.getOptionValue("m", "cbc").toUpperCase());

        EncryptionOptions encryptionOptions = new EncryptionOptions(
                encryptionEnum,
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
            if (encryptionOptions.password() == null) {
                if (encryptionOptions.mode() != null || encryptionOptions.encryptionEnum() != null) {
                    System.err.println("Password is required");
                    return false;
                }
            }

            return true;
        }

        public String getExtension(String file) {
            return file.substring(file.lastIndexOf('.'));
        }
    }
}
