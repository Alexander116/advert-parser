package clientcli;

import api.AdParseException;
import api.SettingsParam;
import api.interfaces.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Init default settings and load config file
 */
public class ClientSettings extends HashMap<String, String> implements Settings {

    private static Logger logger = LoggerFactory.getLogger(ClientSettings.class);

    public ClientSettings() {
        setDefaultSettings();
    }

    public Stream<String> getObserversFromSettings() {
        String observersStr = this.get(SettingsParam.OBSERVERS.toString());
        String[] observers = observersStr.split("/");
        return Arrays.stream(observers);
    }

    private void setDefaultSettings() {
        this.put(SettingsParam.STORAGE.toString(), "Pg");
        this.put(SettingsParam.DB_PASSWORD.toString(), "postgres");
        this.put(SettingsParam.DB_LOGIN.toString(), "");
        this.put(SettingsParam.OBSERVERS.toString(), "Logger");
        this.put(SettingsParam.TELEGRAM_BOT_TOKEN.toString(), "");
        this.put(SettingsParam.TELEGRAM_CHAT_ID.toString(), "");
    }

    public void setSettingsFromCli(String settingsFilePath) {
        try {
            setSettingsFromFile(settingsFilePath);
        } catch (AdParseException e) {
            logger.warn("config don't load.");
        }
    }

    /**
     * Read config and put properties
     *
     * @throws AdParseException
     */
    private void setSettingsFromFile(String settingsFilePath) throws AdParseException {

        Properties properties = getProperties(settingsFilePath);
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            putPropertyToSettings(propertyNames, properties);
        }
    }

    private Properties getProperties(String settingsFilePath) throws AdParseException {
        try (FileInputStream fileStream = new FileInputStream(settingsFilePath)) {
            Properties properties = new Properties();
            properties.load(fileStream);
            return properties;
        } catch (Exception e) {
            throw new AdParseException("get properties error.", e);
        }
    }

    private void putPropertyToSettings(Enumeration<?> names, Properties property) {
        String propName = names.nextElement().toString();
        String value = property.getProperty(propName);
        this.put(propName, value);
    }

}
