package edu.upenn.cit594.datamanagement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface DataValidator {

    default String validateDate(String date){
        Pattern p = Pattern.compile("^(?<date>\\d{4}-[01]\\d-[0-3]\\d) (?<time>[0-2]\\d:[0-5]\\d:[0-5]\\d)$");
        Matcher m = p.matcher(date);
        if (!m.matches()){
            return null;
        }
        return m.group("date");
    }

    default String validateZipcode(String zipcode){
        Pattern p = Pattern.compile("^(?<zipcode>\\d{5}).*");
        Matcher m = p.matcher(zipcode);
        if (!m.matches()){
            return null;
        }
        return m.group("zipcode");
    }

    default Integer validateNullableInt(String integerValuedString){
        try {
            return Integer.parseInt(integerValuedString);
        } catch (NumberFormatException e){
            return null;
        }
    }

    default int validateNonNullableInt(String integerValuedString){
        if ("".equals(integerValuedString)){
            return 0;
        }
        return Integer.parseInt(integerValuedString);
    }

    default Double validateNullableDouble(String value){

        try {
            return Double.parseDouble(value);

        } catch (NumberFormatException e){
            return null;
        }
    }


}
