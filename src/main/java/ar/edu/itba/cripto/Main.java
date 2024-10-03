package ar.edu.itba.cripto;

import ar.edu.itba.cripto.arguments.Parser;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Optional<Parser.Arguments> cli = new Parser().parse(args);

        if (cli.isEmpty()) {
            System.err.println("Error: Invalid arguments");
            return;
        }
        Parser.Arguments arguments = cli.get();

        System.out.println("Arguments: " + arguments);
    }
}