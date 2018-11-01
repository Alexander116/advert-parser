package parser;


import api.AdParseException;
import api.Advert;
import api.interfaces.Parser;

import api.Project;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Забирает объявления с иинтернет страниц
 */
abstract class AbstractParser implements Parser{

    private Logger logger= LoggerFactory.getLogger(AbstractParser.class);

    static final String HASH_MAP_KEY_SUM = "sum";
    static final String HASH_MAP_KEY_DATE = "date";
    static final String HASH_MAP_KEY_DESC = "desc";
    static final String HASH_MAP_KEY_URL = "url";
    static final String HASH_MAP_KEY_TITLE = "title";
    static final String HASH_MAP_KEY_IMG = "img";

    @Override
    public List<Advert> getAdverts(Project project) throws AdParseException {
        return getAdvertsByUrl(project.getUrl())
                .map(v -> refactorAdvert(v, project))
                .collect(Collectors.toList());
    }


    /**
     * парсит объявления по ссылке
     * @return поток объявлений
     */
    private Stream<Map<String, String>> getAdvertsByUrl(String url ) throws AdParseException {
        List<Map<String, String>> adverts;
        try {
            logger.info("Идет загрузка...");
            Document site = Jsoup.connect(url).get();
            adverts = getAdsFromDoc(site);
        } catch (Exception e) {
            throw new AdParseException(e);
        }
        return adverts.stream();
    }

    /**
     * парсит объявления с сайта и записывает результат в список
     */
    List<Map<String, String>> getAdsFromDoc(Document site){
        return getAdvertItems(site)
                .map(this::parseItem)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает поток блоков с объявлением, каждый блок содержит
     * всю необходимую информацию для дальнейшего парсинга
     * @param site
     * @return
     */
    abstract Stream<Element> getAdvertItems(Document site);

    /**
     * Парсинг блока объявления,
     * тут происходит непосредственно парсинг
     * самого объявления с переносом в мапу
     * @param advert
     * @return
     */
    abstract Map<String, String> parseItem(Element advert);

    /**
     * Данный метод требуется для того чтобы дополнительно обработать информацию поступающую с сайта,
     *  и создать из этих данных объявление (Advert).
     * Например если нужно привести дату, заголовок или ссылку к какому-то определенному виду это
     * нужно выполнить в данном методе.
     * @param advert - объявление
     * @return - объявление нужного формата
     */
    Advert refactorAdvert(Map<String, String> advert, Project project) {
        ConvertHelper converter = new ConvertHelper();
        long date = converter.getDateInMillis(project.getUrl(), advert.get(HASH_MAP_KEY_DATE));
        float sum = converter.convertSum(advert.get(HASH_MAP_KEY_SUM));

        return new Advert(
                0,
                advert.get(HASH_MAP_KEY_TITLE),
                date,
                sum,
                advert.get(HASH_MAP_KEY_URL) ,
                advert.get(HASH_MAP_KEY_DESC),
                advert.getOrDefault(HASH_MAP_KEY_IMG,""),
                project
        );
    }
}
