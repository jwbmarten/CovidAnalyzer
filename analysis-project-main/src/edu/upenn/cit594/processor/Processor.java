package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.Reader;
import edu.upenn.cit594.datamanagement.csv.CSVFormatException;
import edu.upenn.cit594.util.CovidDB;
import edu.upenn.cit594.util.PopulationDB;
import edu.upenn.cit594.util.PropertyDB;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.util.*;

public class Processor {

    int totalPopulationCount = -1;

    private Map<String, Integer> avgMarketValues = new HashMap<>();

    private Map<String, Integer> avgLivableAreas = new HashMap<>();

    private Map<String, Map<String, Double>> vaxRateByDate = new HashMap<>();

    private Map<String, Integer> marketValuesByCapita = new HashMap<>();

    private Map<String, Double> correlationResults = new HashMap<>();

    protected PopulationDB populationDB;

    protected CovidDB covidDB;

    protected PropertyDB propertyDB;

    public Processor(Reader<CovidDB> covidReader, Reader<PopulationDB> populationReader, Reader<PropertyDB> propertyReader) throws CSVFormatException, IOException {

        if (covidReader != null)
            this.covidDB = covidReader.readRecords();
        if (populationReader != null)
            this.populationDB = populationReader.readRecords();
        if (propertyReader != null)
            this.propertyDB = propertyReader.readRecords();
    }

    public boolean covidFileIsLoaded(){
        return covidDB != null;
    }

    public boolean populationFileIsLoaded(){
        return populationDB != null;
    }

    public boolean propertyFileIsLoaded(){
        return propertyDB != null;
    }

    public int calculateTotalPopulation(){

        //first check if a value has been stored from this method already (memoization)
        if (totalPopulationCount >= 0){
            return totalPopulationCount;
        }

        int result;

        result = populationDB.getAllPopCounts()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        //store the result (memoization)
        totalPopulationCount = result;

        return result;
    }


    public Map<String, Double> calculateTotalVaxRatePerCapita (String vaxCountKind, String date){

        String cacheKey = date + "_" + vaxCountKind;

        // First, check if these parameters have already been tested & stored (memoization)

        if(vaxRateByDate.containsKey(cacheKey)){
            return vaxRateByDate.get(cacheKey);
        }


        // init output
        Map<String, Double> result = new TreeMap<>();

        // load both database

        // Map<String, Map<String, Integer>> zipcodeData = covidDB.getAllZipcodeDataForDate(date);

        // iterate through the zipcodes in population
        for (String zipcode: populationDB.getAllZipcodes()){

            double popCount = (double) populationDB.queryPopCount(zipcode);
            double vaxCount = (double) covidDB.queryVaxCount(date, zipcode, vaxCountKind);

            if (popCount == 0.0 || vaxCount == 0.0)
                continue;
            double rate = vaxCount / popCount;

            result.put(zipcode, rate);
        }

        // store result in the appropriate Map (memoization)
        vaxRateByDate.put(cacheKey, result);


        return result;
    }

    //////////////////////////////////
    ////  Processing for 3.4 and 3.5
    public int calculateAverageValue(String zipcode, AverageStrategy averageStrategy){

        // First, check if these parameters have already been tested & stored (memoization)
        if (averageStrategy.getStrategyType().equals("live")){
            if (avgLivableAreas.containsKey(zipcode)){
                return avgLivableAreas.get(zipcode);
            }
        } else if (averageStrategy.getStrategyType().equals("market")) {
            if (avgMarketValues.containsKey(zipcode)){
                return avgMarketValues.get(zipcode);
            }
        }

        int result = averageStrategy.calculateAverage(zipcode, propertyDB, averageStrategy);

        // store result in the appropriate Map (memoization)
        if (averageStrategy.getStrategyType().equals("live")){
            avgLivableAreas.put(zipcode, result);
        } else if (averageStrategy.getStrategyType().equals("market")) {
            avgMarketValues.put(zipcode, result);
        }

        return result;

    }

    public int calculateTotalMarketValuePerCapita (String zipcode){

        // First, check if this zipcode has already been tested & stored (memoization)
        if(marketValuesByCapita.containsKey(zipcode)){
            return marketValuesByCapita.get(zipcode);
        }

        // load both database
        List<Double> marketValues = propertyDB.getMarketValues(zipcode);
        double population = (double) populationDB.queryPopCount(zipcode);

        if (population == 0){
            return 0;
        }

        int result = (int) (marketValues
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum() / population);

        // store result in the appropriate Map (memoization)
        marketValuesByCapita.put(zipcode, result);

        return result;
    }


    public double correlateVaxRateAndMarketValue (String date, String vaxCountKind){

        String cacheKey = date + "_" + vaxCountKind;

        // First, check if these parameters have already been tested & stored (memoization)
            if(correlationResults.containsKey(cacheKey)){
                return correlationResults.get(cacheKey);
            }

        // init
        Set<String> allZipcodes = populationDB.getAllZipcodes();
        Map<String, Double> totalVaxRatePerCapita = calculateTotalVaxRatePerCapita(vaxCountKind, date);

        // check if date is within range (totalVaxRatePerCapita would be empty)
        if (totalVaxRatePerCapita.isEmpty())
            return Double.NaN;

        //double[] X =  new double[allZipcodes.size()];
        //double[] Y =  new double[allZipcodes.size()];

        List<Double> X = new ArrayList<>();
        List<Double> Y = new ArrayList<>();

        for (String currentZipcode : allZipcodes){

            if (!totalVaxRatePerCapita.containsKey(currentZipcode))
                continue;

            double vaxRate = totalVaxRatePerCapita.get(currentZipcode);
            X.add(vaxRate);

            // TODO: what happens if this is undefined / if data does not exist?
            double marketValuePerCap = calculateTotalMarketValuePerCapita(currentZipcode);
            Y.add(marketValuePerCap);
        }

        Double result = calculateCorrelation(X, Y);

        // store result in the appropriate Map (memoization)
        correlationResults.put(cacheKey, result);

        return result;
    }



    private double calculateCorrelation(List<Double> X, List<Double> Y) {

        if (X.size() != Y.size()) {
            throw new IllegalArgumentException("Arrays must be of the same length.");
        }

        int n = X.size();
        double sumX = 0.0, sumY = 0.0, sumX2 = 0.0, sumY2 = 0.0, sumXY = 0.0;

        // calculate sums and sum of squares
        for (int i = 0; i < n; i++) {
            sumX += X.get(i);
            sumY += Y.get(i);
        }

        // calculate mean of X and Y
        double meanX = sumX / n;
        double meanY = sumY / n;

        // init covariance and variances
        double covariance = 0.0;
        double varianceX = 0.0;
        double varianceY = 0.0;

        for (int i = 0; i < n; i++) {

            double devX = X.get(i) - meanX;
            double devY = Y.get(i) - meanY;
            covariance += devX * devY;
            varianceX += devX * devX;
            varianceY += devY * devY;

        }

        // normalize covariance and variance by (n-1) for an unbiased estimate
        covariance /= (n - 1);
        varianceX /= (n - 1);
        varianceY /= (n - 1);

        // return correlation coefficient
        return covariance / Math.sqrt(varianceX * varianceY);
    }

}
