package ar.edu.itba.cripto.arguments;


import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public enum Actions {

    EMBED,
    EXTRACT;

    static OptionGroup getOptions() {
        OptionGroup options = new OptionGroup();
        options.addOption(new Option("embed", false, "Embed a message into an image"));
        options.addOption(new Option("extract", false, "Extract a message from an image"));
        return options;
    }
}
