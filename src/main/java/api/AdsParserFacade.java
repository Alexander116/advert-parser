package api;

import api.factories.ParserFactory;
import api.factories.StorageFactory;
import api.interfaces.Observer;
import api.interfaces.Parser;
import api.interfaces.Settings;
import api.interfaces.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * реализует фасад для работы с приложением
 */
public class AdsParserFacade {

    private Storage storage;
    private List<Observer> observers = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(AdsParserFacade.class);
    private List<Advert> updateAdverts = new ArrayList<>();
    private List<Advert> newAdverts = new ArrayList<>();

    /**
     * Создание фасада через настройки
     * @param settings - настройки
     * @throws AdParseException
     */
    public AdsParserFacade(Settings settings) throws AdParseException {
        this.storage = StorageFactory.create(settings);
    }


    /**
     * Добавление наблюдателя
     *
     * @param o - observer
     */
    public void addObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Создает проект для отслеживания если такой ссылки еще не существует
     *
     * @param project - проект
     * @return true - если проект создан, иначе false
     */
    public boolean createProject(Project project) throws AdParseException {
        return storage.createProject(project) != null;
    }

    /**
     * проверяет все проекты на обновления и новые объявления
     */
    public void runParsing() throws AdParseException {
        listProjects().forEach(this::handleAdvertNotifyObservers);
    }


    private void handleAdvertNotifyObservers(Project proj){
        getAdsByProject(proj).forEach(this::saveOrUpdateAdvert);
        notifyObservers();
    }

    /**
     * Список всех проектов
     *
     * @return поток проектов
     * @throws AdParseException
     */
    public List<Project> listProjects() throws AdParseException {
        return storage.listAllProjects();
    }


    /**
     * Удаление проекта
     * @param id
     * @throws AdParseException
     */
    public void deleteProject(int id) throws AdParseException {
        storage.deleteProjectWithAdverts(id);
    }

    /**
     * возвращает последние объявления по ссылке проекта
     *
     * @param proj - проект
     * @return
     */
    private List<Advert> getAdsByProject(Project proj) {
        List<Advert> result = new ArrayList<>();
        try {
            Parser parser = ParserFactory.create(proj.getUrl());
            result = parser.getAdverts(proj);
        } catch (AdParseException e) {
            logger.warn("Get adverts error", e);
        }
        return result;
    }

    /**
     * Сохраняет объявление если оно новое,
     * обновляет если оно обновилось и существует, иначе ничего не делает.
     *
     * @param advert - объявление
     * @throws AdParseException
     */
    private void saveOrUpdateAdvert(Advert advert) {
        try {
            Advert oldAdvert = storage.findAdvertByUrl(advert);
            if (!isAdvertExist(oldAdvert)) {
                storage.save(advert);
                newAdverts.add(advert);
            } else if (isAdvertUpdate(advert, oldAdvert)) {
                advert.setId(oldAdvert.getId());
                storage.update(advert);
                updateAdverts.add(advert);
            }
        } catch (AdParseException e) {
            logger.warn("Storage error", e);
        }
    }


    private void notifyObservers() {
        observers.forEach(obs -> obs.update(newAdverts, AdvertEvent.ADVERT_CREATED));
        observers.forEach(obs -> obs.update(updateAdverts, AdvertEvent.ADVERT_UPDATED));
        newAdverts.clear();
        updateAdverts.clear();
    }


    private boolean isAdvertExist(Advert advert) {
        return advert != null;
    }

    private boolean isAdvertUpdate(Advert newAdvert, Advert oldAdvert) {
        return !newAdvert.equals(oldAdvert);
    }

}
