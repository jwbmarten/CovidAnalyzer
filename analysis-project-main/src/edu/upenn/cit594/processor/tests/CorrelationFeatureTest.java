package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.Reader;
import edu.upenn.cit594.datamanagement.ReaderFactory;
import edu.upenn.cit594.datamanagement.csv.CSVFormatException;
import edu.upenn.cit594.util.CovidDB;
import edu.upenn.cit594.util.PopulationDB;
import edu.upenn.cit594.util.PropertyDB;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CorrelationFeatureTest {

    @Test
    public void testExistentDatePartial() throws IOException, CSVFormatException {

        Processor processor = new Processor(null, null, null);

        processor.covidDB = new CovidDB();
        processor.covidDB.insertRecord("2023-03-25", "19100", 200, 200);
        processor.covidDB.insertRecord("2023-03-25", "19101", 300, 300);

        processor.populationDB = new PopulationDB();
        processor.populationDB.insertRecord("19100", 10000);
        processor.populationDB.insertRecord("19101", 10000);

        processor.propertyDB = new PropertyDB();
        processor.propertyDB.insertRecord("19100", 100.0, 20000.0);
        processor.propertyDB.insertRecord("19101", 100.0, 30000.0);


        double result = processor.correlateVaxRateAndMarketValue("partial", "2023-03-25");

        assertEquals(Double.NaN, result);
    }

    @Test
    public void testExistentDateFull(){

    }

    @Test
    public void testNonExistentDatePartial() throws CSVFormatException, IOException {

        Processor processor = new Processor(null, null, null);

        processor.covidDB = new CovidDB();
        processor.populationDB = new PopulationDB();
        processor.propertyDB = new PropertyDB();

        double result = processor.correlateVaxRateAndMarketValue("partial", "1999-99-99");
        System.out.println(result);
    }

    @Test
    public void testNonExistentDateFull(){

    }

    @Test
    public void testAllDataPartial(){

        try {

            Reader<CovidDB> covidReader = ReaderFactory.createCovidReader("covid_data.csv");
            Reader<PopulationDB> populationReader = ReaderFactory.createPopulationReader("population.csv");
            Reader<PropertyDB> propertyReader = ReaderFactory.createPropertyReader("downsampled_properties.csv");

            Processor processor = new Processor(covidReader, populationReader, propertyReader);


            double result = processor.correlateVaxRateAndMarketValue("2021-03-26", "partial");

            System.out.println(result);

        } catch (IOException e){
            e.printStackTrace();
        } catch (CSVFormatException e) {
            throw new RuntimeException(e);
        }

    }
}