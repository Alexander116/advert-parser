package parser;

import api.Advert;
import junit.framework.TestCase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Тестирование непосредственно парсинга
 */
public class ParsersTest extends TestCase{

    public void testAvito() throws IOException {

        Document site = getSiteFromFile("src/test/recources/avito_test.html", "UTF-8");
        Avito avito = new Avito();
        List<Map<String, String>> adverts = avito.getAdsFromDoc(site);
        Map<String, String> advert = adverts.get(0);

        Advert myAdvert = new Advert(
                0, "Ford Focus, 2012", 0, 0,
                "https://www.avito.ru/kazan/avtomobili/ford_focus_2012_872353708",
                "", "",null
        );

        assertEqualsAdverts(advert,myAdvert);
        assertEquals(adverts.size(), 29);
    }



    public void testDrom() throws IOException {
        Document site = getSiteFromFile("src/test/recources/drom_test.html", "Windows-1251");
        Drom avitoParser = new Drom();
        List<Map<String, String>> adverts = avitoParser.getAdsFromDoc(site);
        Map<String, String> advert = adverts.get(0);

        Advert myAdvert = new Advert(
                0, "Лада 2104 1991", 0, 0,
                "http://chita.drom.ru/lada/2104/24621784.html",
                "","", null
        );

        assertEqualsAdverts(advert,myAdvert);
        assertEquals(adverts.size(), 20);

    }

    public void testIrr() throws IOException {
        Document site = getSiteFromFile("src/test/recources/irr_test.html", "UTF-8");
        Irr avitoParser = new Irr();
        List<Map<String, String>> adverts = avitoParser.getAdsFromDoc(site);
        Map<String, String> advert = adverts.get(0);

        Advert myAdvert = new Advert(
                0, "BMW X6", 0, 0,
                "http://irr.ru/cars/passenger/used/bmw-x6-krossover-2011-g-v-probeg-120000-km-avtomat-advert618740155.html",
                "2.99 АТ (286 л.с.) кроссовер, дизель, полный","", null
        );

        assertEqualsAdverts(advert,myAdvert);
        assertEquals(adverts.size(), 31);

    }


    private void assertEqualsAdverts(Map<String, String> advert, Advert myAdvert) {
        assertEquals(advert.get(AbstractParser.HASH_MAP_KEY_TITLE), myAdvert.getTitle());
        assertEquals(advert.get(AbstractParser.HASH_MAP_KEY_URL), myAdvert.getUrl());
    }

    private Document getSiteFromFile(String filePath, String encoding) throws IOException {
        File file = new File(filePath);
        return Jsoup.parse(file,encoding);
    }


}
