package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class Irr extends AbstractParser {


    @Override
    Stream<Element> getAdvertItems(Document site) {
        return site.select(".listing__item.js-productBlock").stream();
    }

    @Override
    Map<String, String> parseItem(Element advert){
        Map<String, String> resAdvert = new HashMap<>();

        resAdvert.put(HASH_MAP_KEY_TITLE, advert.select(".js-productListingProductName").text());
        resAdvert.put(HASH_MAP_KEY_DESC, advert.select(".listing__itemParameter_subTitle").text());
        resAdvert.put(HASH_MAP_KEY_SUM, advert.select(".listing__itemPrice").text());
        advert.select(".updateProduct__wrapper_listing").remove();
        resAdvert.put(HASH_MAP_KEY_DATE, advert.select(".listing__itemDate").text());
        resAdvert.put(HASH_MAP_KEY_URL, advert.select(".listing__itemTitle").attr("href"));
        resAdvert.put(HASH_MAP_KEY_IMG, advert.select(".listing__image").attr("data-src"));
        return resAdvert;
    }
}
