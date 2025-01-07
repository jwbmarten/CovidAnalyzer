package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidDB;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class CovidJSONReader extends Reader<CovidDB> implements DataValidator{

    private final JSONParser jsonParser = new JSONParser();

    public CovidJSONReader(String filename) throws IOException {
        super(filename);
    }

    public CovidDB readRecords() throws IOException {

        CovidDB db = new CovidDB();

        Logger logger = Logger.getInstance();
        logger.log(filename);

        try (BufferedReader fileReader = Files.newBufferedReader(Paths.get(this.filename))) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(fileReader);

            // TODO: ignore null date and or zipcode
            for (Object o : jsonArray){
                JSONObject record = (JSONObject) o;

                String date = validateDate((String) record.get("etl_timestamp"));
                String zipcode = validateZipcode(Long.toString( (long) record.get("zip_code")));

                if (date == null || zipcode == null)
                    continue;

                int partialVaxCount = (record.get("partially_vaccinated") == null) ? 0 :
                        Long.valueOf((long) record.get("partially_vaccinated")).intValue();

                int fullVaxCount = (record.get("fully_vaccinated") == null) ? 0 :
                        Long.valueOf((long) record.get("fully_vaccinated")).intValue();

                db.insertRecord(date, zipcode, partialVaxCount, fullVaxCount);
            }
            return db;

        } catch (NoSuchFileException e){
            throw new NoSuchFileException("File '" + filename + "' does not exist.");
        } catch (IOException e){
            throw new IOException("File  '" + filename + "' could not be read.");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
