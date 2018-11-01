package api;

import junit.framework.TestCase;


public class AdvertTest extends TestCase {

    public void testAdvertEquals(){
        Project project = new Project(1,"1","avito.ru",true);
        Advert adv1 = new Advert(1,"title",2132131,123456,"avito.ru/avto","desc","",project);
        Advert adv2 = new Advert(0,"title", 2132131,123456,"avito.ru/avto","desc","",null);
        Advert adv3 = new Advert(0,"title1", 2132131,123456,"avito.ru/avto","desc","",null);
        Advert adv4 = new Advert(0,"title", 2132131,123455,"avito.ru/avto","desc","",null);

        assertTrue(adv1.equals(adv2));
        assertFalse(adv1.equals(adv3));
        assertFalse(adv1.equals(adv4));
    }

}
