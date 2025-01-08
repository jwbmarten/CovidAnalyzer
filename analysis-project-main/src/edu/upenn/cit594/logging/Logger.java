package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//The logger class records the following:
//     • Log an input event.
//     • Setting or changing the output destination.

public class Logger {

    private static Logger instance;
    private PrintWriter writer;
    private boolean hasLogFile;

    // Private constructor to prevent multiple instances
    private Logger() {

        writer = new PrintWriter(System.err);
        hasLogFile = false;
    }

    // Method to return the single instance of the logger
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // Method for logging events
    public void log(String event) {
        if (writer != null) {
            long currentTimeMillis = System.currentTimeMillis();
            String logEntry = currentTimeMillis + " " + event;
            writer.println(logEntry);
            writer.flush();
        }
    }

    // Method to set or change the output file destination
    public void setOutput(String fileName) throws IOException{

        // Close the current writer if one is open and it's not System.err
        if ((writer != null) && (!hasLogFile)){
            writer.close();
        }

        if (fileName == null || fileName.isEmpty()) {
            // Reassign writer to standard error if no file name is provided
            writer = new PrintWriter(System.err);
        } else {
            // Set up the writer to append to the file without overwriting it
            FileWriter fileWriter = new FileWriter(fileName, true);
            writer = new PrintWriter(fileWriter);
            hasLogFile = true;
        }

    }

    // Close the writer
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }

}