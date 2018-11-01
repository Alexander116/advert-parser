package parser;


import api.Advert;
import api.Project;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Avito extends AbstractParser {

    @Override
    Stream<Element> getAdvertItems(Document site) {
        return site.select(".item").stream();
    }

    @Override
    Map<String, String> parseItem(Element advert){
        Map<String, String> resAdvert = new HashMap<>();
        resAdvert.put(HASH_MAP_KEY_TITLE, advert.select(".description h3").text());
        resAdvert.put(HASH_MAP_KEY_DESC, advert.select(".description .about .param").text());
        advert.select(".description .about .param").remove();
        resAdvert.put(HASH_MAP_KEY_SUM, advert.select(".description .about").text());
        resAdvert.put(HASH_MAP_KEY_DATE, advert.select(".description .data .date").text());
        resAdvert.put(HASH_MAP_KEY_URL, advert.select(".description h3 a").attr("href"));
        return resAdvert;
    }

    @Override
    Advert refactorAdvert(Map<String, String> advert, Project project){

        Advert refactorAdvert = super.refactorAdvert(advert, project);
        String url = "https://www.avito.ru" +refactorAdvert.getUrl();
        refactorAdvert.setUrl(url);
        return refactorAdvert;
    }
}
