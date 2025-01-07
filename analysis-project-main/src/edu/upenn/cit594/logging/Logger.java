package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//A logger is a global ledger available for use by all parts of an application, for sequentially recording
//selected program events (typically debugging and monitoring information) separately from the
//primary program output. The logger is a self-contained companion facility to the rest of the
//application and is not governed by the main application design. It simply sits off to the side and
//records events at the request of any other application component.

//In general, there is one and only one logger instance, which is used by every part of the code at all
//times. The logger should be initialized only once, before or at the first possible loggable event, and
//any subsequent request for a logger, by any function, should return this same instance. In short,
//a logger is a perfect example of the Singleton design pattern, and your logger for this assignment
//must implement that pattern.

//In general, there is one and only one logger instance, which is used by every part of the code at all
//times. The logger should be initialized only once, before or at the first possible loggable event, and
//any subsequent request for a logger, by any function, should return this same instance. In short,
//a logger is a perfect example of the Singleton design pattern, and your logger for this assignment
//must implement that pattern.

//The logger class must have instance methods (non-static methods) to:
//     • Log an event. This method must have a single parameter of type String.
//     • Set or change the output destination. This method must take a single String, the name of
//       the file to write.

//You may choose how to handle events sent to the logger prior to setting an output file (this should
//not come up during normal operations). For the purpose of this assignment, you are free to just
//drop these events and not record them anywhere.

//The method that configures the output must support changing the output to a different file. That
//means it must be careful to close the current output, if one is set, before opening the requested
//destination. Logged events should appear in and only in the output file that is current when they
//are logged.

//Log files should be created if necessary and always opened with the append option, not overwritten.
//See the FileWriter documentation for details.

//Hint: The specified behaviors suggest settings to use when opening the output file and should not
//require additional logic in your code.

//Be mindful, if you want to use the Singleton pattern in other places in the code (this is neither
//recommended nor forbidden), it is not just about classes that you only instantiate once. A Singleton
//is global state that persists across much of the life of the application, so you wouldn’t want too
//many of these floating around, unable to be garbage-collected.

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