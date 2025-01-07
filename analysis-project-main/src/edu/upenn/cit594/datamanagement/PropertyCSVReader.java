package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.datamanagement.csv.CSVFormatException;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PropertyDB;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PropertyCSVReader extends CSVReader<PropertyDB> implements DataValidator{

    public PropertyCSVReader(String filename) throws IOException {
        super(filename);
    }

    public PropertyDB readRecords() throws CSVFormatException, IOException {

        PropertyDB db = new PropertyDB();
        Logger logger = Logger.getInstance();
        logger.log(filename);                        //// LOG WHEN READING IN A FILE

        String[] row;
        Map<String, Integer> columnValueMapper = new HashMap<>();

        readHeader();
        int zipcodeIndex = columnNameMapper.get("zip_code");
        int areaIndex = columnNameMapper.get("total_livable_area");
        int marketValueIndex = columnNameMapper.get("market_value");

        while ((row = CSVParser.readRow()) != null) {

            String zipcode = validateZipcode(row[zipcodeIndex]);

            if (zipcode == null)
                continue;

            Double area = validateNullableDouble(row[areaIndex]);
            Double marketValue = validateNullableDouble(row[marketValueIndex]);

            // insert
            db.insertRecord(zipcode, area, marketValue);
        }
        return db;
    }
}
