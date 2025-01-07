package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidDB;
import edu.upenn.cit594.util.PopulationDB;
import edu.upenn.cit594.util.PropertyDB;

import java.io.IOException;

public class ReaderFactory {

    public static Reader<CovidDB> createCovidReader(String filePath) throws IOException {
        if (matchExtension(filePath, "csv")) {
            return new CovidCSVReader(filePath);
        } else if (matchExtension(filePath, "json")) {
            return new CovidJSONReader(filePath);
        } else {
            throw new IllegalArgumentException("Unsupported file format for COVID data");
        }
    }

    public static Reader<PopulationDB> createPopulationReader(String filePath) throws IOException {
        if (matchExtension(filePath, "csv")) {
            return new PopulationCSVReader(filePath);
        }
        throw new IllegalArgumentException("Population data must be in CSV format");
    }

    public static Reader<PropertyDB> createPropertyReader(String filePath) throws IOException {
        if (matchExtension(filePath, "csv")) {
            return new PropertyCSVReader(filePath);
        }
        throw new IllegalArgumentException("Property data must be in CSV format");

    }

    private static boolean matchExtension(String filePath, String ext){
        return filePath.toLowerCase().endsWith("." + ext);
    }
}
