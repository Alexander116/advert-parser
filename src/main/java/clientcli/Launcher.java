package clientcli;

import api.AdParseException;
import api.AdsParserFacade;
import api.factories.ObserverFactory;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * launch parser with cli
 */
public class Launcher {

    private static Logger logger = LoggerFactory.getLogger(Launcher.class);
    private static ClientSettings settings;
    private static String[] cliArgs;

    public static void main(String[] args) {
            cliArgs = args;
            startSingleInstanceCliApp();
    }

    private static void startSingleInstanceCliApp() {
        settings = new ClientSettings();
        tryStartCliApp();
    }

    private static void tryStartCliApp() {
        try {
            Runnable app = new CliApplication(settings, cliArgs);
            app.run();
        } catch (ParseException e) {
            logger.info("This option is not existing try -h");
        }
    }

}
