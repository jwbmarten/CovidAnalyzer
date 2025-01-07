package edu.upenn.cit594.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyDB {

    private Map<String, Map<String, List<Double>>> data;

    public PropertyDB(){
        data = new HashMap<>();
    }


    public void insertRecord(String zipcode, Double area, Double marketValue){

        Map<String, List<Double>> zipMap = data.computeIfAbsent(zipcode, k -> new HashMap<>());

        List<Double> areas = zipMap.computeIfAbsent("total_livable_area", k -> new ArrayList<>());
        areas.add(area);

        List<Double> marketValues = zipMap.computeIfAbsent("market_value", k -> new ArrayList<>());
        marketValues.add(marketValue);
    }

    public List<Double> getTotalLivableAreas(String zipcode){
        if (data.containsKey(zipcode)){
            return data.get(zipcode).getOrDefault("total_livable_area", new ArrayList<>());
        }
        return new ArrayList<>();
    }

    public List<Double> getMarketValues(String zipcode){
        if (data.containsKey(zipcode)){
            return data.get(zipcode).getOrDefault("market_value", new ArrayList<>());
        }
        return new ArrayList<>();
    }

}
