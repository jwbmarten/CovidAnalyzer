package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyDB;

import java.util.List;

public interface AverageStrategy {

    List<Double> selectData(String zipcode, PropertyDB propertyDB);

    default int calculateAverage(String zipcode, PropertyDB propertyDB, AverageStrategy strategy){

        // load whichever list will be averaged
        List<Double> listValues = strategy.selectData(zipcode, propertyDB);

        // if the returned list is empty, indicates the zip code is not in the database, return 0 to be printed by console as per instructions
        if (listValues.isEmpty()){
            return 0;
        }

        // if the list is not empty, initialize a Double to track the running sum, and a int to track the number of valid entries
        Double runningSum = 0.0;
        int validCount = 0;

        // iterate through all values and add to running sum
        for (Double value : listValues){
            if (value != null) {
                runningSum += value;
                validCount ++;
            }
        }

        // if valid count is 0, return 0 to avoid dividing by 0, otherwise divide sum by size of list and return the average cast to an int to truncate the decimal.
        if (validCount == 0){
            return 0;
        } else {
        return (int) (runningSum/validCount); }

    }

    public String getStrategyType();

}
