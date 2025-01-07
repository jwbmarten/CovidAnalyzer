package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.datamanagement.csv.CSVFormatException;
import edu.upenn.cit594.datamanagement.csv.CSVParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class CSVReader<T> extends Reader<T> {

    protected final CSVParser CSVParser;

    protected final Map<String, Integer> columnNameMapper = new HashMap<>();

    public CSVReader(String filename) throws IOException {
        super(filename);
        this.CSVParser = new CSVParser(filename);
    }

    protected void readHeader() throws IOException, CSVFormatException {
        String[] row;

        if ((row = CSVParser.readRow()) != null) {
            for (int i = 0; i < row.length; i++){

                String currentColumnName = row[i];
                columnNameMapper.put(currentColumnName, i);
            }
        }
    }

}
