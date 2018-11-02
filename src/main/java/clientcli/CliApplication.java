package clientcli;

import api.*;
import api.factories.ObserverFactory;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CliApplication implements Runnable{

    private static Logger logger= LoggerFactory.getLogger(CliApplication.class);

    private AdsParserFacade adsParserFacade;
    private CommandLine cmd;
    private CliParser cliParser;
    private ClientSettings settings;


    CliApplication(ClientSettings settings, String[] args) throws ParseException {
        this.cliParser = new CliParser(args);
        this.cmd = cliParser.parse();
        this.settings = settings;
        if(cmd.hasOption("c")){
            settings.setSettingsFromCli(cmd.getOptionValue("c", ""));
        }
        try {
            adsParserFacade = new AdsParserFacade(settings);
        } catch (AdParseException e) {
            logger.error("Facade init error", e);
        }
        addObservers();
    }

    @Override
    public void run() {

         if(cmd.hasOption("h")) {
             cliParser.printHelp();
         }else if(cmd.hasOption("r")){
             runParsing();
         }else if(cmd.hasOption("a")){
             tryAddProject();
         }else if(cmd.hasOption("l")){
             tryListProjects();
         }else if(cmd.hasOption("d")){
             tryDeleteProject();
         }
    }


    /**
     * run parse of all projects
     */
    private void runParsing(){
        try {
            adsParserFacade.runParsing();
        }catch (AdParseException e){
            logger.warn("Parsing error. ",e);
        }
    }

    private void tryListProjects() {
        try{
            listProjects();
        }catch (AdParseException e){
            logger.warn("List project error. ",e);
        }
    }

    private void tryAddProject() {
        try{
            boolean isCreate = addProject();
            String info = isCreate ? "project created" : "project already exist";
            logger.info(info);
        }catch (AdParseException e){
            logger.warn("Add project error. ",e);
        }

    }

    private void tryDeleteProject(){
        try {
            String[] arguments = cmd.getOptionValues("d");
            int id = Integer.parseInt(arguments[0]);
            adsParserFacade.deleteProject(id);
            logger.info("Project successfully delete");
        } catch (Exception e) {
            logger.warn("Delete error. ",e);
        }
    }

    private boolean addProject() throws AdParseException {
        String[] arguments = cmd.getOptionValues("a");
        String url = arguments[0].trim();
        String name = arguments[1].trim();
        Project project = new Project(0, name, url, null);
        return adsParserFacade.createProject(project);
    }

    /**
     * list all projects on screen
     * @throws AdParseException
     */
    private void listProjects() throws AdParseException {
        adsParserFacade.listProjects()
                .forEach(proj -> logger.info(proj.toString()));
    }

    private void addObservers(){
        settings.getObserversFromSettings()
                .forEach(this::addObserver);
    }

    private void addObserver(String observerName){
        try {
            adsParserFacade.addObserver(ObserverFactory.create(settings, observerName));
        } catch (AdParseException e) {
            logger.warn("Observer create error. ",e);
        }
    }
}
