package api.factories;

import api.AdParseException;
import api.UrlParser;
import api.interfaces.Parser;
import parser.Avito;
import parser.Drom;
import parser.Irr;

/**
 * Create parsers
 */
public class ParserFactory {

    public static Parser create(String domain) throws AdParseException {
        if(domain.contains("avito"))
            return new Avito();
        if(domain.contains("irr"))
            return new Irr();
        if(domain.contains("drom"))
            return new Drom();

        throw new AdParseException("no find parser");
    }
}
