package edu.upenn.cit594.ui;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.LivableAreaAverageStrategy;
import edu.upenn.cit594.processor.MarketValueAverageStrategy;
import edu.upenn.cit594.processor.Processor;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInput {

    private final Scanner userInput;

    private Processor processor;

    Logger logger = Logger.getInstance();


    public UserInput(Processor processor){

        this.userInput = new Scanner(System.in);
        this.processor = processor;
    }

    public int runHomeMenu(){

        ConsolePrinting.printHomeMenu();

        System.out.println("Please Enter an option from the above menu and press return");
        int userSelection = getUserIntChoice(7);

        return userSelection;

    }

//    public int getUserIntChoice(int maxIntChoice){
//
//        Scanner userInput = new Scanner(System.in);
//        int userSelection = -1;
//
//        System.out.print("> ");
//        System.out.flush();
//
//        // do-while loop will continue to execute until the user provides an int between 0 and the max possible selection
//        do {
//
//            if (userInput.hasNext()) {
//                // If the user inputs a non-integer value, notify the user and re-prompt
//                while (!userInput.hasNextInt()) {
//                    System.out.println("Input invalid! Please enter an integer between 0 and " + maxIntChoice);
//                    System.out.print("> ");
//                    System.out.flush();
//                    userInput.next(); // this will consume the non-valid input, allowing the user's next input to be checked by the loop condition
//                }
//
//                userSelection = userInput.nextInt();
//
//                if ((userSelection < 0) || (userSelection > 7)) {
//                    System.out.println("Input invalid! Please enter an integer between 0 and " + maxIntChoice);
//                    System.out.print("> ");
//                    System.out.flush();
//                }
//            }
//        } while ((userSelection < 0) || (userSelection > maxIntChoice)); // if user selection is less than 0 or greater than the maz possible selection, start loop over
//
//        // if the do-while loop has been exited, user has provided a valid int between the prescribed range, return the selection
//        return userSelection;
//    }

    public int getUserIntChoice(int maxIntChoice) {
//        Scanner userInput = new Scanner(System.in);
        int userSelection = 0;

        System.out.print("> ");
        System.out.flush();

        while (true) {
            if (userInput.hasNext()) {

                String input = userInput.next();
                logger.log(input);                        ///// LOG USER RESPONSE

                if (input.matches("\\d+")) {
                    userSelection = Integer.parseInt(input);

                    // Validate the range of the user input
                    if (userSelection >= 0 && userSelection <= maxIntChoice) {
                        break; // Valid input, exit loop
                    } else {
                        System.out.println("Input invalid! Please enter an integer between 0 and " + maxIntChoice);;
                    }
                } else {
                    System.out.println("Input invalid! Please enter an integer between 0 and " + maxIntChoice);
                }

                // Re-prompt the user for input
                System.out.print("> ");
                System.out.flush();
            } else {
                break;
            }

//                while (!userInput.hasNextInt()) {
//                    System.out.println("Input invalid! Please enter an integer between 0 and " + maxIntChoice);
//                    System.out.print("> ");
//                    System.out.flush();
//                    userInput.next(); // Consume non-integer input
//                }
//                userSelection = userInput.nextInt();
//
//                if (userSelection >= 0 && userSelection <= maxIntChoice) {
//                    break; // Valid input, exit loop
//                } else {
//                    System.out.println("Input invalid! Please enter an integer between 0 and " + maxIntChoice);
//                    System.out.print("> ");
//                    System.out.flush();
//                }
//            } else {
//                // This branch will execute if there's no more input (e.g., end of stream)
//                System.out.println("No more input available.");
//                break;
//            }
        }

        return userSelection;
    }

    public String getUserDateInput(){

//        Scanner userInput = new Scanner(System.in);
        String date = "";
        boolean inputValid = false;

        String argumentRegex = "^(?<year>\\d{4})-(?<month>0[1-9]|1[0-2])-(?<day>0[1-9]|[12][0-9]|3[01])$";
        Pattern pattern = Pattern.compile(argumentRegex);

        System.out.println("Please enter a date in the format: YYYY-MM-DD.");

        System.out.print("> ");
        System.out.flush();

        // do-while loop will continue to execute until the user provides a valid date entry YYYY-MM-DD
//        do {
//
//            String argument = userInput.next();
//            Matcher matcher = pattern.matcher(argument);
//
//            // IF the provided string does not match the regex, notify the user and reprompt
//            if (!matcher.matches()){
//                System.out.println("Input invalid! Please enter a date in the format: YYYY-MM-DD!");
//                System.out.print("> ");
//                System.out.flush();
//            } else {  // ELSE IF the provided string does match the correct format, store the date in the date array and set input Valid to true
//                date = argument;
//                inputValid = true;
//            }
//
//        } while (!inputValid); // This loop can only be exited once the user provides a correctly formatted date

        while (true) {
            if (userInput.hasNext()) {

                String argument = userInput.next();
                logger.log(argument);                                    //// LOG USER RESPONSE
                Matcher matcher = pattern.matcher(argument);

                if (!matcher.matches()) {
                    System.out.println("Input invalid! Please enter a date in the format: YYYY-MM-DD!");
                    System.out.print("> ");
                    System.out.flush();
                } else {
                    date = argument;
                    break;
                }

            } else {
                // This branch will execute if there's no more input (e.g., end of stream)
//                System.out.println("No more input available.");
                break;
            }
        }

        return date;

    }

    public String getUserZIPInput(){

//        Scanner userInput = new Scanner(System.in);
        String zipCode = "";
        boolean inputValid = false;

        String argumentRegex = "^\\d{5}$";
        Pattern pattern = Pattern.compile(argumentRegex);

        System.out.println("Please enter a 5 digit Zip Code.");
        System.out.print("> ");
        System.out.flush();

//        while (!inputValid) {
//            zipCode = userInput.next(); // Reads the user input
//            Matcher matcher = pattern.matcher(zipCode);
//
//            if (matcher.matches()) { // Check if input matches the 5-digit pattern, if so inputValid is set to true so the do-while loop can be escaped.
//                inputValid = true;
//            } else {
//                System.out.println("Invalid input! Please enter exactly 5 digits.");
//                System.out.print("> ");
//                System.out.flush();
//            }
//        }

        while (true) {
            if (userInput.hasNext()) {

                String argument = userInput.next();
                logger.log(argument);                                    //// LOG USER RESPONSE
                Matcher matcher = pattern.matcher(argument);

                if (!matcher.matches()) {
                    System.out.println("Invalid input! Please enter exactly 5 digits.");
                    System.out.print("> ");
                    System.out.flush();
                } else {
                    zipCode = argument;
                    break;
                }

            } else {
                // This branch will execute if there's no more input (e.g., end of stream)
//                System.out.println("No more input available.");
                break;
            }
        }

        return zipCode;

    }

    public String getUserVaccinationType(){

//        Scanner userInput = new Scanner(System.in);
        String userResponse = "";
        boolean inputValid = false;

        System.out.println("Please enter desired vaccination status, partial or full");
        System.out.print("> ");
        System.out.flush();

//        while (!inputValid) {
//            userResponse = userInput.next().toLowerCase();
//
//            if ((userResponse.equals("partial")) || (userResponse.equals("full"))){
//                inputValid = true;
//            } else {
//                System.out.println("Invalid input! Please enter partial or full");
//                System.out.print("> ");
//                System.out.flush();
//            }
//        }

        while (true) {
            if (userInput.hasNext()) {

                userResponse = userInput.next().toLowerCase();
                logger.log(userResponse);                                    //// LOG USER RESPONSE


                if ((userResponse.equals("partial")) || (userResponse.equals("full"))) {
                    break;
                } else {
                    System.out.println("Invalid input! Please enter partial or full");
                    System.out.print("> ");
                    System.out.flush();
                }

            } else {
                // This branch will execute if there's no more input (e.g., end of stream)
//                System.out.println("No more input available.");
                break;
            }
        }

        return userResponse;
    }

    public void printAvailableActions(){

        // boolean array signifies which files have been loaded
        boolean covidFileLoaded = processor.covidFileIsLoaded();
        boolean propertiesFileLoaded = processor.propertyFileIsLoaded();
        boolean populationFileLoaded = processor.populationFileIsLoaded();

        System.out.println("\nBEGIN OUTPUT");

        // Option 0 and 1 will always be available
        System.out.println(0);
        System.out.println(1);

        // Option 2 is available if the population file was loaded
        if (populationFileLoaded){
            System.out.println(2);
        }

        // Option 3 is available if both covid and population file loaded
        if (covidFileLoaded && populationFileLoaded){
            System.out.println(3);
        }

        // Option 4 and 5 available if properties file loaded
        if (propertiesFileLoaded){
            System.out.println(4);
            System.out.println(5);
        }

        // Option 6 is available if both properties and population file loaded
        if (propertiesFileLoaded && populationFileLoaded){
            System.out.println(6);
        }

        /// Option 7 must use all 3 files, so check for all
        if (covidFileLoaded && propertiesFileLoaded && populationFileLoaded){
            System.out.println(7);
        }


        System.out.println("END OUTPUT");


    }

    public void getTotalPopulationAllZips(){
        ConsolePrinting.printOutput(processor.calculateTotalPopulation());
    }

    public void runVaccinationAnalysis(){

        // prompt user for vaccination status
        String vaxStatus = getUserVaccinationType();

        // prompt user for date
        String dateChosen = getUserDateInput();

        // print resulting map from calculate total vax rate per capita method
        ConsolePrinting.printOutput(processor.calculateTotalVaxRatePerCapita(vaxStatus, dateChosen));
    }

    ///////////////////////////////////////////////////////////////////////////
    ////  Handles functionality as outlined in 3.4 leveraging Strategy Pattern
    public void runAverageMarketValue(){

        // prompt user for the desired zipcode
        String zipcode = getUserZIPInput();

        // call Averaging Strategy Pattern with market value strategy
        int average = processor.calculateAverageValue(zipcode, new MarketValueAverageStrategy());

        ConsolePrinting.printOutput(average);
    }

    ///////////////////////////////////////////////////////////////////////////
    ////  Handles functionality as outlined in 3.5 leveraging Strategy Pattern
    public void runAverageLivableArea(){

        // prompt user for the desired zipcode
        String zipcode = getUserZIPInput();

        // call Averaging Strategy Pattern with market value strategy
        int average = processor.calculateAverageValue(zipcode, new LivableAreaAverageStrategy());

        ConsolePrinting.printOutput(average);
    }

    public boolean checkActionAvailability(int userSelection){

        // boolean array signifies which files have been loaded
        boolean covidFileLoaded = processor.covidFileIsLoaded();
        boolean propertiesFileLoaded = processor.propertyFileIsLoaded();
        boolean populationFileLoaded = processor.populationFileIsLoaded();

        switch (userSelection){
            case 0, 1:
                return true; // actions 0 and 1 from the menu should always be available
            case 2:
                if (!populationFileLoaded){ // if the population file is not loaded, notify user and return false
                    ConsolePrinting.printErrorMessage("That functionality requires a population file to be loaded!");
                    return false;
                }
                break;
            case 3:
                if (!covidFileLoaded || !populationFileLoaded){ // if either the covid or the population file is not loaded, notify user and return false
                    ConsolePrinting.printErrorMessage("That functionality requires both a covid and a population file to be loaded!");
                    return false;
                }
                break;
            case 4, 5:
                if (!propertiesFileLoaded){ // if the properties file is not loaded, notify user and return false
                    ConsolePrinting.printErrorMessage("That functionality requires a properties file to be loaded!");
                    return false;
                }
                break;
            case 6:
                if (!propertiesFileLoaded || !populationFileLoaded){ // if either the properties or the population file is not loaded, notify user and return false
                    ConsolePrinting.printErrorMessage("That functionality requires both a properties and a population file to be loaded!");
                    return false;
                }
                break;
            case 7:
                if (!covidFileLoaded || !propertiesFileLoaded || !populationFileLoaded){ // if any of the files are not loaded, notify user and return false
                    ConsolePrinting.printErrorMessage("That functionality requires a covid, a properties, and a population file to be loaded!");
                    return false;
                }
                break;
        }

        // if false not already returned, user has the appropriate files
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////
    ////  Handles functionality as outlined in 3.6
    public void runTotalMarketValuePerCapita() {

        String zipcode = getUserZIPInput();
        int marketValuePerCapita = processor.calculateTotalMarketValuePerCapita(zipcode);
        ConsolePrinting.printOutput(marketValuePerCapita);

    }


    public void runCorrelateVaxRateAndMarketValue() {

        String date = getUserDateInput();
        String vaxCountKind = getUserVaccinationType();
        double correlation = processor.correlateVaxRateAndMarketValue(date, vaxCountKind);
        ConsolePrinting.printOutput(correlation);
    }
}
