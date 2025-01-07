package edu.upenn.cit594.ui;

import java.util.Map;

public class ConsolePrinting {



    static public void printHomeMenu(){

        System.out.println("0. Exit the program.");
        System.out.println("1. Show the available actions");
        System.out.println("2. Show the total population for all ZIP Codes");
        System.out.println("3. Show the total vaccinations per capita for each ZIP Code for a specified date");
        System.out.println("4. Show the average market value for properties in a specified ZIP Code");
        System.out.println("5. Show the average total livable area for properties in a specified ZIP Code");
        System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code");
        System.out.println("7. Show the correlation between market value (per capita) " +
                " and covid vax rate (per capita) for a specified date");
    }

    static public void printFatalErrorMessage(String text){
        System.err.println("ERROR : " + text);
        System.err.println("Terminating Program...");
    }

    static public void printErrorMessage(String text){
        System.out.println("ERROR : " + text);
    }

    static public void printOutput(String output){
        System.out.println("\nBEGIN OUTPUT");
        System.out.println(output);
        System.out.println("END OUTPUT");
    }

    static public void printOutput(int output){
        System.out.println("\nBEGIN OUTPUT");
        System.out.println(output);
        System.out.println("END OUTPUT");
    }

    static public void printOutput(Map<String, Double> vaxData){
        System.out.println("\nBEGIN OUTPUT");
        if(vaxData.isEmpty()){     /// if the input map is empty, just print 0 as per instructions
            System.out.println(0);
        } else {                   /// else iterate through each entry and print the zipcode followed by a single space followed by the value
            for (Map.Entry<String, Double> entry : vaxData.entrySet()) {

                if (entry.getValue() != 0){  /// ensure that any entries with a value of 0 are not printed
                System.out.println(entry.getKey() + " " + String.format("%.4f", entry.getValue()));}
            }
        }
        System.out.println("END OUTPUT");
    }

    static public void printOutput(double output){
        System.out.println("\nBEGIN OUTPUT");
        System.out.println(String.format("%.4f", output));
        System.out.println("END OUTPUT");
    }

}
