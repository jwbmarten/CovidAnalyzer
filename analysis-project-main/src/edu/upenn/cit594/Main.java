package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.Reader;
import edu.upenn.cit594.datamanagement.ReaderFactory;
import edu.upenn.cit594.datamanagement.csv.CSVFormatException;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;
import edu.upenn.cit594.ui.ConsolePrinting;
import edu.upenn.cit594.ui.UserInput;
import edu.upenn.cit594.util.CovidDB;
import edu.upenn.cit594.util.PopulationDB;
import edu.upenn.cit594.util.PropertyDB;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.regex.Matcher;

public class Main {

    public static <CovidReader> void main(String[] args) throws IOException, CSVFormatException {


        // Initialize Strings for each of the option file inputs. These will be assigned in this Main function if provided in the arguments.

        int userIntInput;

        boolean logFileLoaded = false;

        //// TODO : GENERAL PROGRAM CONDITIONS - program should display an error message and immediately terminate if:
        //   1) Any arguments to main do not match the form "--name=value" (e.g. --population=example_population_file.csv, --log=events.log, etc) (DONE)
        //   2) The name of an argument is not one of: covid, properties, population, or log (DONE)
        //   3) The name of an argument is used more than once (e.g. "--log=a.log   --log=a.log") (DONE)
        //   4) The logger cannot be correctly initialized (e.g. the given log file cannot be opened for writing.)
        //   5) The format of the COVID data file cannot be determined from the filename extension ("csv" or "json", case insensitive)
        //   6) The specified input files do not exist or cannot be opened for reading (e.g. because of file permissions)


        // IF there are no arguments, print an error message and terminate program (ED Thread #1605)
        if (args.length < 1) {
            System.out.println("ERROR: No arguments provided! Exiting Program...");
            return;
        }

        //////////////////////////////////////////////////////////////////////////////////////////////
        // If this code is reached, then there is at least one argument, instantiate logger and start parsing the argument(s)

        // initialize logger, will be set to write to System.err unless a valid log file is provided in arguments
        Logger logger = Logger.getInstance();

        //Regular expression for arguments as provided in project instructions
        String argumentRegex = "^--(?<name>.+?)=(?<value>.+)$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(argumentRegex);

        Reader<CovidDB> covidReader = null;
        Reader<PopulationDB> populationReader = null;
        Reader<PropertyDB> propertyReader = null;

        for (String argument : args) {

            Matcher matcher = pattern.matcher(argument);

            // IF the argument is valid, save the file type and file name as variables
            if (matcher.matches()) {
                String fileType = matcher.group("name").toLowerCase();
                String fileName = matcher.group("value");

                // Switch to handle each of the file types, each case starts with a check if that file type already has a file read in ( TODO SATISFIES GENERAL PROGRAM CONDITION 3)
                switch (fileType){
                    case "covid":
                        if (covidReader != null){
                            ConsolePrinting.printFatalErrorMessage("Existing COVID data file already provided when attempting to read : " + fileName);
                            return;
                        }


                        try {
                            covidReader = ReaderFactory.createCovidReader(fileName);

                        } catch (NoSuchFileException e) {
                            ConsolePrinting.printFatalErrorMessage("File " + fileName + " not found");
                            return;
                        } catch (IOException e) {
                            ConsolePrinting.printFatalErrorMessage("File " + fileName + " could not be read");
                            return;
                        }

                        break;
                    case "properties":
                        if (propertyReader != null){
                            ConsolePrinting.printFatalErrorMessage("Existing Property data file already provided when attempting to read : " + fileName);
                            return;
                        }


                        try {
                            propertyReader = ReaderFactory.createPropertyReader(fileName);

                        } catch (NoSuchFileException e) {
                            ConsolePrinting.printFatalErrorMessage("File " + fileName + " not found");
                            return;
                        } catch (IOException e) {
                            ConsolePrinting.printFatalErrorMessage("File " + fileName + " could not be read");
                            return;
                        }

                        break;
                    case "population":
                        if (populationReader != null){
                            ConsolePrinting.printFatalErrorMessage("Existing Population data file already provided when attempting to read : " + fileName);
                            return;
                        }

                        try {
                            populationReader = ReaderFactory.createPopulationReader(fileName);

                        } catch (NoSuchFileException e) {
                            ConsolePrinting.printFatalErrorMessage("File " + fileName + " not found");
                            return;
                        } catch (IOException e) {
                            ConsolePrinting.printFatalErrorMessage("File " + fileName + " could not be read");
                            return;
                        }

                        break;
                    case "log":
                        if (logFileLoaded){
                            ConsolePrinting.printFatalErrorMessage("Existing Log data file already provided when attempting to read : " + fileName);
                            return;
                        }

                        // try to set the log output to the provided file, catch IO error and inform user if fails
                        try {
                            logger.setOutput(fileName);
                        } catch (IOException e){
                            ConsolePrinting.printFatalErrorMessage("Logger could not be correctly initialized! : " + fileName);
                            return;
                        }

                        // if no log file previously loaded, set logFileLoaded to true
                        logFileLoaded = true;

                        break;
                    default: // (TODO SATISFIES GENERAL PROGRAM CONDITION 2)
                        ConsolePrinting.printFatalErrorMessage("Argument name is not valid! : " + fileType);
                        return;
                }

            }

            // ELSE IF the argument does not match the provided regex, notify the user and terminate the program
            // (TODO SATISFIES GENERAL PROGRAM CONDITION 1)
            else {
                System.out.println("ERROR: Unable to parse argument: " + argument);
                System.out.println("Terminating Program...");
                return;
            }


        } // END OF FOR LOOP WHICH ITERATES THROUGH ARGS ///////////////////////

        //write arguments to logger
        for(String argument: args){
            logger.log(argument + " ");
        }

        //Instantiate the UserInput class with a processor class
        UserInput userInput = new UserInput(new Processor(covidReader, populationReader, propertyReader));

        do {

            userIntInput = userInput.runHomeMenu();

            if (userInput.checkActionAvailability(userIntInput)) { // first check that the user can execute the selected action

                switch (userIntInput) {
                    case 0:  //0. Exit the program.
                        return;
                    case 1:  //1. Show the available actions
                        userInput.printAvailableActions();
                        break;
                    case 2:  //2. Show the total population for all ZIP Codes
                        userInput.getTotalPopulationAllZips();
                        break;
                    case 3:  //3. Show the total vaccinations per capita for each ZIP Code for a specified date
                        userInput.runVaccinationAnalysis();
                        break;
                    case 4:  //4. Show the average market value for properties in a specified ZIP Code
                        userInput.runAverageMarketValue();
                        break;
                    case 5:  //5. Show the average total livable area for properties in a specified ZIP Code
                        userInput.runAverageLivableArea();
                        break;
                    case 6:  //6. Show the total market value of properties, per capita, for a specified ZIP Code
                        userInput.runTotalMarketValuePerCapita();
                        break;
                    case 7:  //7. CUSTOM FUNCTION TBD
                        userInput.runCorrelateVaxRateAndMarketValue();
                        break;
                }

            }

        } while (userIntInput != 0);


    }
}