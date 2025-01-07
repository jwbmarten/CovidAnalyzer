package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyDB;

import java.util.List;

public class MarketValueAverageStrategy implements AverageStrategy{

    @Override
    public List<Double> selectData(String zipcode, PropertyDB propertyDB) {

        return propertyDB.getMarketValues(zipcode);
    }

    @Override
    public String getStrategyType() {
        return "market";
    }
}
