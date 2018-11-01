package parser;

import api.AdParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.dateconverter.DateConverter;
import parser.dateconverter.Factory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Конвертирует стандартные данные объявления
 */
class ConvertHelper {
    private Logger logger= LoggerFactory.getLogger(ConvertHelper.class);

    /**
     * Переводит дату из строкового формата в миллисекунды
     * @param projectUrl ссылка на проект
     * @param date дата
     * @return
     */
    long getDateInMillis(String projectUrl,String date){
        long dateMillis = 0;
        try{
            DateConverter dateConverter = Factory.create(projectUrl, date);
            dateMillis = dateConverter.getDateInMillis();
        } catch (AdParseException e){
            logger.warn("DateFactory error.",e);
        }
        return dateMillis;
    }

    /**
     * переводит строку суммы вида( 111 руб.) в число
     * @param sum сумма
     * @return
     */
    float convertSum(String sum){
        float res = 0;
        try{
            //Удаление пробела 2 раза сделано не случайно,
            // это какой-то не обычный пробел который приходит с irr
            String clearSum = sum.replace("руб.","").replaceAll(" ","").replaceAll(" ","");
            res = Float.parseFloat(clearSum);
        }catch(Exception e){
            logger.warn("Sum convert error.",e);
        }
        return res;
    }
}
