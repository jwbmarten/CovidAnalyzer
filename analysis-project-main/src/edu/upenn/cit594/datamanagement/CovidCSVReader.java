package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.datamanagement.csv.CSVFormatException;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidDB;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class CovidCSVReader extends CSVReader<CovidDB> implements DataValidator{


    public CovidCSVReader(String filename) throws IOException {
        super(filename);
    }

    @Override
    public CovidDB readRecords() throws IOException, CSVFormatException {

        CovidDB db = new CovidDB();

        Logger logger = Logger.getInstance();
        logger.log(filename);

        String[] row;
        Map<String, Integer> columnValueMapper = new HashMap<>();

        readHeader();
        int dateIndex = columnNameMapper.get("etl_timestamp");
        int zipcodeIndex = columnNameMapper.get("zip_code");
        int partialVaxIndex = columnNameMapper.get("partially_vaccinated");
        int fullVaxIndex = columnNameMapper.get("fully_vaccinated");

        while ((row = CSVParser.readRow()) != null) {

            String date = validateDate(row[dateIndex]);
            String zipcode = validateZipcode(row[zipcodeIndex]);
            int partialVaxCount = validateNonNullableInt(row[partialVaxIndex]);
            int fullVaxCount = validateNonNullableInt(row[fullVaxIndex]);

            if (date == null || zipcode == null)
                continue;

            // insert
            db.insertRecord(date, zipcode, partialVaxCount, fullVaxCount);
        }
        return db;
    }
}
