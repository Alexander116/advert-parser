package api.factories;


import api.AdParseException;
import api.interfaces.Observer;
import api.interfaces.Settings;
import api.observers.LoggerObserver;
import api.observers.TelegramObserver;

public class ObserverFactory {

    public static Observer create(Settings settings, String prefix) throws AdParseException {
        if("logger".equals(prefix))
            return new LoggerObserver(settings);
        if("telegram".equals(prefix))
            return new TelegramObserver(settings);
        return new LoggerObserver(settings);
    }
}
