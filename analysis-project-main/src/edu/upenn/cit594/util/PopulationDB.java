package edu.upenn.cit594.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class PopulationDB {

    private Map<String, Integer> data;

    public PopulationDB() {
        data = new TreeMap<>();
    }

    public void insertRecord(String zipcode, int populationCount){

        data.put(zipcode, populationCount);

    }

    public int queryPopCount(String zipcode){
        return data.get(zipcode);
    }

    public boolean hasZipcode(String zipcode){
        return data.containsKey(zipcode);
    }

    public Set<String> getAllZipcodes(){
        return data.keySet();
    }

    public List<Integer> getAllPopCounts(){
        return data.values().stream().toList();
    }
}
