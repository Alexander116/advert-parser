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
 * Хранит клиентские настройки
 */
public class ClientSettings extends HashMap<String, String> implements Settings {

    private static Logger logger = LoggerFactory.getLogger(ClientSettings.class);
    private String settingsFilePath;

    /**
     * Устанавливает настройки по умолчанию
     *
     * @throws Exception
     */
    public ClientSettings() {
        setDefaultSettings();
        setSettingFromConfig();
    }

    public Stream<String> getObserversFromSettings() {
        String observersStr = this.get(SettingsParam.OBSERVERS.toString());
        String[] observers = observersStr.split("/");
        return Arrays.stream(observers);
    }

    private void setDefaultSettings() {
        this.put(SettingsParam.STORAGE.toString(), "Pg");
        this.put(SettingsParam.DB_PASSWORD.toString(), "123");
        this.put(SettingsParam.DB_LOGIN.toString(), "denisov");
        this.put(SettingsParam.PORT.toString(), "9999");
        this.put(SettingsParam.JDBC_URL.toString(), "jdbc:postgresql://localhost:5432/denisov");
        this.put(SettingsParam.OBSERVERS.toString(), "Logger");
        this.put(SettingsParam.TELEGRAM_BOT_TOKEN.toString(), "159475460:AAHp9AKJ2Lu3-s6G5KYzhRSl2kcEH93ZZyk");
        this.put(SettingsParam.TELEGRAM_CHAT_ID.toString(), "140801616");
    }

    /**
     * Ищет конфиг и записывает его настройки
     *
     * @throws AdParseException
     */
    private void setSettingFromConfig() {
        /*try {
            findSettingsPath();
            setSettingsFromFile();
        } catch (AdParseException e) {
            logger.warn("config don't load.");
        }*/
    }

    public void setSettingsFromCli(String value) {

        setSettingsFromStr(value);

    }

    private void findSettingsPath() {
        Map<String, String> env = System.getenv();
        settingsFilePath = env.get("AD_PARSER_CONFIG");
        if (settingsFilePath == null) {
            settingsFilePath = env.get("HOME") + "/AdParser.properties";
        }
    }

    /**
     * Получает настройки из строки вида (k=v, k1=v1, ...)
     *
     * @param str - строка с настройками
     */
    private void setSettingsFromStr(String str) {
        splitArgs(str, ",").stream()
                .filter(arg -> arg.contains("="))
                .forEach(this::putInSettings);
    }

    /**
     * Считывает файл настроек и записывает его параметры в настройки
     *
     * @throws AdParseException
     */
    private void setSettingsFromFile() throws AdParseException {

        Properties properties = getProperties();
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            putPropertyToSettings(propertyNames, properties);
        }
    }

    private Properties getProperties() throws AdParseException {
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

    /**
     * Разбивает строку на массив и чистит от пробелов
     *
     * @param str      строка
     * @param delimetr разделитель
     * @return
     */
    private List<String> splitArgs(String str, String delimetr) {
        return Arrays.stream(str.split(delimetr))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private void putInSettings(String arg) {
        List<String> tmpArg = splitArgs(arg, "=");
        this.put(tmpArg.get(0), tmpArg.get(1));
    }

}
