package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.csv.CSVFormatException;
import edu.upenn.cit594.processor.Processor;
import edu.upenn.cit594.util.CovidDB;
import edu.upenn.cit594.util.PopulationDB;
import edu.upenn.cit594.util.PropertyDB;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class vaxRatePerCapitaTest {


    @Test
    public void testExistentDatePartialOneZip() throws CSVFormatException, IOException {

        Processor processor = new Processor(null, null, null);

        processor.covidDB = new CovidDB();
        processor.covidDB.insertRecord("2023-03-25", "19100", 200, 200);

        processor.populationDB = new PopulationDB();
        processor.populationDB.insertRecord("19100", 200);


        Map<String, Double> result = processor.calculateTotalVaxRatePerCapita("partial", "2023-03-25");
        assertTrue(result.containsKey("19100"));
        assertEquals(1.0, result.get("19100"));
    }

    @Test
    public void testExistentDatePartialTwoZip() throws CSVFormatException, IOException {

        Processor processor = new Processor(null, null, null);

        processor.covidDB = new CovidDB();
        processor.covidDB.insertRecord("2023-03-25", "19100", 200, 200);
        processor.covidDB.insertRecord("2023-03-25", "19101", 300, 300);

        processor.populationDB = new PopulationDB();
        processor.populationDB.insertRecord("19100", 200);
        processor.populationDB.insertRecord("19101", 300);


        Map<String, Double> result = processor.calculateTotalVaxRatePerCapita("partial", "2023-03-25");
        assertTrue(result.containsKey("19100"));
        assertEquals(1.0, result.get("19100"));
    }

    @Test
    public void testExistentDateFull() throws CSVFormatException, IOException {
        /* set up */

        Processor processor = new Processor(null, null, null);

        processor.covidDB = new CovidDB();
        processor.covidDB.insertRecord("2023-03-25", "19100", 200, 200);

        processor.populationDB = new PopulationDB();
        processor.populationDB.insertRecord("19100", 200);

        /* test */
        Map<String, Double> result = processor.calculateTotalVaxRatePerCapita("full", "2023-03-25");
        assertTrue(result.containsKey("19100"));
        assertEquals(1, result.get("19100"));
    }

    @Test
    public void testNonExistentDatePartial() throws CSVFormatException, IOException {

        Processor processor = new Processor(null, null, null);

        processor.covidDB = new CovidDB();
        processor.covidDB.insertRecord("2023-03-25", "19100", 200, 200);

        processor.populationDB = new PopulationDB();
        processor.populationDB.insertRecord("19100", 200);


        Map<String, Double> result = processor.calculateTotalVaxRatePerCapita("partial", "1999-99-99");
        assertFalse(result.containsKey("19100"));
        assertNull(result.get("19100"));
    }

    @Test
    public void testNonExistentDateFull() throws CSVFormatException, IOException {

        /* set up */
        Processor processor = new Processor(null, null, null);

        processor.covidDB = new CovidDB();
        processor.covidDB.insertRecord("2023-03-25", "19100", 200, 200);

        /* test */
        processor.populationDB = new PopulationDB();
        processor.populationDB.insertRecord("19100", 200);
        Map<String, Double> result = processor.calculateTotalVaxRatePerCapita("full", "1999-99-99");
        assertFalse(result.containsKey("19100"));
        assertNull(result.get("19100"));

    }
}
