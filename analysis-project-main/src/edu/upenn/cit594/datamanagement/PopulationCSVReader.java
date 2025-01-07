package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.datamanagement.csv.CSVFormatException;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PopulationDB;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PopulationCSVReader extends CSVReader<PopulationDB> implements DataValidator{

    private final Pattern zipcodePattern = Pattern.compile("^\\d{5}$");

    private final Pattern popPattern = Pattern.compile("^\\d+$");

    public PopulationCSVReader(String filename) throws IOException {
        super(filename);
    }

    public PopulationDB readRecords() throws CSVFormatException, IOException {

        PopulationDB db  = new PopulationDB();

        Logger logger = Logger.getInstance();
        logger.log(filename);

        String[] row;
        Map<String, Integer> columnValueMapper = new HashMap<>();

        readHeader();
        int zipcodeIndex = columnNameMapper.get("zip_code");
        int popIndex = columnNameMapper.get("population");

        while ((row = CSVParser.readRow()) != null) {

            String zipcode = validateZipcode(row[zipcodeIndex]);
            Integer popCount = validateNullableInt(row[popIndex]);
            if (zipcode == null || popCount == null)
                continue;

            // insert
            db.insertRecord(zipcode, popCount);
        }

        return db;
    }
}
