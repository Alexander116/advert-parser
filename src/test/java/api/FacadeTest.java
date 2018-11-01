package api;

import api.factories.ParserFactory;
import api.factories.StorageFactory;
import api.interfaces.Parser;
import api.interfaces.Settings;
import api.interfaces.Storage;
import clientcli.ClientSettings;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ StorageFactory.class, ParserFactory.class })
public class FacadeTest extends TestCase {


    private Project project;
    private Advert advert;
    private Advert findAdvert;
    private List<Project> projects = new ArrayList<>();
    private List<Advert> adverts = new ArrayList<>();
    private Settings settings;
    private Storage storage;
    private Parser parser;

    /**
     * Проверяет сохраненяется ли новое объявление
     */
    public void testSave() throws AdParseException {
        findAdvert = null;
        startTest(0,1);
    }


    /**
     * Проверяет обновляется ли модифицированное объявление
     */
    public void testUpdate() throws AdParseException {
        findAdvert = new Advert(1,"title",12121212,7777,"avito.ru/moskow","desc","",project);
        startTest(1,0);
    }

    /**
     * Проверяет, что уже существующее объявление не сохраняется и не обновляется
     * @throws AdParseException
     */
    public void testOldAdvert() throws AdParseException {
        findAdvert = new Advert(1,"title",12121212,12000,"avito.ru/moskow","desc", "",project);
        startTest(0,0);
    }


    private void startTest(int updates, int saves) throws AdParseException {
        initObjects();
        mockObjects();
        AdsParserFacade api = new AdsParserFacade(settings);

        when(parser.getAdverts(project)).thenReturn(adverts);
        when(storage.listAllProjects()).thenReturn(projects);
        when(storage.findAdvertByUrl(advert)).thenReturn(findAdvert);
        api.runParsing();
        verify(storage, times(updates)).update(advert);
        verify(storage, times(saves)).save(advert);
    }

    private void initObjects()  {
        project = new Project(1,"test","test",true);
        advert = new Advert(1, "title", 12121212, 12000, "avito.ru/moskow", "desc", "",project);
        adverts.add(advert);
        projects.add(project);
        settings = new ClientSettings();

    }

    private void mockObjects() throws AdParseException {
        parser = mock(Parser.class);
        storage = mock(Storage.class);
        PowerMockito.mockStatic(ParserFactory.class);
        PowerMockito.when(ParserFactory.create(project.getUrl())).thenReturn(parser);
        PowerMockito.mockStatic(StorageFactory.class);
        PowerMockito.when(StorageFactory.create(settings)).thenReturn(storage);
    }
}
