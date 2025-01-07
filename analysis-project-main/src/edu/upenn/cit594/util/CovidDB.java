package edu.upenn.cit594.util;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

public class CovidDB {

    // follows Map < date, Map <zipcode, Map < vaxCountName, vaxCountValue >>>
    private Map<String, Map<String, Map<String, Integer>>> data;

    public CovidDB(){
         data = new HashMap<>();
    }

    public void insertRecord(String date, String zipcode, int partialVaxCount, int fullVaxCount){


        Map<String, Map<String, Integer>> singleDateMap = data.computeIfAbsent(date, k -> new HashMap<>());

        Map<String, Integer> singleZipcodeMap = singleDateMap.computeIfAbsent(zipcode, k -> new TreeMap<>());

        singleZipcodeMap.put("partial", partialVaxCount);
        singleZipcodeMap.put("full", fullVaxCount);

    }

    private Map<String, Map<String, Integer>> getAllZipcodeDataForDate(String date){
        return data.getOrDefault(date, new TreeMap<>());
    }

    private Map<String, Integer> getVaxCountDataForZipcode(String date, String zipcode){
        Map<String, Map<String, Integer>> singleDateMap = getAllZipcodeDataForDate(date);

        return singleDateMap.getOrDefault(zipcode, new HashMap<>());
    }

    public int queryVaxCount(String date, String zipcode, String vaxCountKind){

        if (!data.containsKey(date) || !data.get(date).containsKey(zipcode))
            return 0;

        return data.get(date).get(zipcode).get(vaxCountKind);
    }


}
