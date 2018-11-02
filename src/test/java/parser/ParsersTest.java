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
                0, "Audi Q7", 0, 1095000,
                "https://irr.ru/cars/passenger/used/audi-q7-krossover-2009-g-v-probeg-165000-km-advert701015175.html",
                "2.967 АТ (233 л.с.) кроссовер, дизель, полный","", null
        );

        assertEqualsAdverts(advert,myAdvert);
        assertEquals(adverts.size(), 30);

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
