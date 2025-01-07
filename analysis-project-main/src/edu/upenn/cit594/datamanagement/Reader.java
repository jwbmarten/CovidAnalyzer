package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.datamanagement.csv.CSVFormatException;

import java.io.IOException;

public abstract class Reader<T> {

    String filename;

    public Reader(String filename) {
        this.filename = filename;
    }

    public abstract T readRecords() throws IOException, CSVFormatException;

}
