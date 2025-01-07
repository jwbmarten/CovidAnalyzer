package edu.upenn.cit594.datamanagement.csv;
/*
 * I attest that the code in this file is entirely my own except for the starter
 * code provided with the assignment and the following exceptions:
 * <Enter all external resources and collaborations here. Note external code may
 * reduce your score but appropriate citation is required to avoid academic
 * integrity violations. Please see the Course Syllabus as well as the
 * university code of academic integrity:
 *  https://catalog.upenn.edu/pennbook/code-of-academic-integrity/ >
 * Signed,
 * Author: Virgilio Gonzenbach
 * Penn email: <vgonzenb@seas.upenn.edu>
 * Date: 2024-03-10
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code csv.CSVReader} provides a stateful API for streaming individual CSV rows
 * as arrays of strings that have been read from a given CSV file.
 *
 * @author Virgilio Gonzenbach
 */
public class CSVParser {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 5130409650040L;
    private final CharacterReader reader;
    private int currentLine = 0;
    private int currentColumn = 0;
    private int currentRow = 0;
    private int currentField = 0;



    public CSVParser(CharacterReader reader) {
        this.reader = reader;
    }

    public CSVParser(String filename) throws IOException{
            reader = new CharacterReader(filename);
    }

    private enum State {
        START, // indicates the start of a line
        FIELD, // indicates a character that is a part of a field was read last
        QUOTED_FIELD, // indicates that were are currently reading a double quote field",
        DQUOTE, // indicates a " that is part of double-quoted field was read last
        COMMA, // indicates a comma that is not part of a field was read last
        CR, // indicates a CR in an unquoted field, which may be followed by a LF (valid) or not (invalid)
        EOF // the end of the file
    }

    private State state = State.START;

    /**
     * This method uses the class's {@code util.CharacterReader} to read in just enough
     * characters to process a single valid CSV row, represented as an array of
     * strings where each element of the array is a field of the row. If formatting
     * errors are encountered during reading, this method throws a
     * {@code util.CSVFormatException} that specifies the exact point at which the error
     * occurred.
     *
     * @return a single row of CSV represented as a string array, where each
     *         element of the array is a field of the row; or {@code null} when
     *         there are no more rows left to be read.
     * @throws IOException when the underlying reader encountered an error
     * @throws CSVFormatException when the CSV file is formatted incorrectly
     */
    public String[] readRow() throws IOException, CSVFormatException {

        List<String> row = new ArrayList<>();
        StringBuilder currentFieldBuilder = new StringBuilder();
        currentField = 0; // Reset field counter for the new row

        while (state != State.EOF) {
            int ch = reader.read();

            switch (state) {
                case START -> {
                    switch (ch) {
                        case ',' -> {  // add an empty field
                            row.add(currentFieldBuilder.toString());
                            state = State.COMMA;
                            currentColumn++;
                        }
                        case '"' -> {
                            state = State.QUOTED_FIELD;
                            currentField++;
                            currentColumn++;
                        }
                        case '\n' -> {
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            // state = State.START;
                            currentField++;
                            currentRow++;
                            currentLine++;
                            currentColumn = 0;
                            return row.toArray(new String[0]);
                        }
                        case '\r' -> {
                            state = State.CR;
                            currentLine++;
                            currentColumn = 0;
                        }
                        case -1 -> {
                            state = State.EOF;
                        }
                        default -> {
                            currentFieldBuilder.append((char) ch);
                            state = State.FIELD;
                            currentField++;
                            currentColumn++;
                        }
                    }
                }

                case FIELD -> {
                    switch (ch) {
                        case ',' -> {
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            state = State.COMMA;
                            currentColumn++;
                        }
                        case '"' -> {
                            throw new CSVFormatException("\" encountered during unquoted field",
                                    currentLine, currentColumn, currentRow, currentField);
                        }
                        case '\n' -> {
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            state = State.START;
                            currentRow++;
                            currentLine++;
                            currentColumn = 0;
                            return row.toArray(new String[0]);
                        }
                        case '\r' -> {
                            state = State.CR;
                            currentLine++;
                            currentColumn = 0;
                        }
                        case -1 -> {
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            state = State.EOF;
                            return row.toArray(new String[0]);
                        }
                        default -> {
                            currentFieldBuilder.append((char) ch);
                            currentColumn++;
                        }
                    }

                }

                case QUOTED_FIELD -> {
                    switch (ch) {
                        case '"' -> {
                            state = State.DQUOTE;
                            currentColumn++;
                        }
                        case '\n', '\r' -> {
                            currentFieldBuilder.append((char) ch);
                            // state = State.QUOTED_FIELD;
                            currentLine++;
                            currentColumn = 0;
                        }
                        case -1 -> {
                            state = State.EOF;
                            throw new CSVFormatException("EOF reached during a quoted field",
                                    currentLine, currentColumn, currentRow, currentField);
                        }
                        default -> {
                            currentFieldBuilder.append((char) ch);
                            currentColumn++;
                        }
                    }
                }

                case DQUOTE -> {
                    switch (ch) {
                        case ',' -> {
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            state = State.COMMA;
                            currentColumn++;
                        }
                        case '"' -> {
                            currentFieldBuilder.append((char) ch);
                            state = State.QUOTED_FIELD;
                            currentColumn++;
                        }
                        case '\n' -> {
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            state = State.START;
                            currentRow++;
                            currentLine++;
                            currentField++;
                            currentColumn = 0;
                            return row.toArray(new String[0]);
                        }
                        case '\r' -> {
                            state = State.CR;
                            currentLine++;
                            currentColumn = 0;
                        }
                        case -1 -> {
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            state = State.EOF;
                            return row.toArray(new String[0]);
                        }
                        default -> {
                            currentColumn++;
                            throw new CSVFormatException("textdata " + ch + " encountered immediately after \" within quoted field",
                                    currentLine, currentColumn, currentRow, currentField);
                        }
                    }
                }

                case CR -> {
                    switch (ch) {
                        case '\n' -> {
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            state = State.START;
                            currentRow++;
                            currentLine++;
                            currentColumn = 0;
                            return row.toArray(new String[0]);
                        }
                        default -> {
                            throw new CSVFormatException("\\r encountered during unquoted field and not followed by \\n",
                                    currentLine, currentColumn, currentRow, currentField);
                        }
                    }
                }

                case COMMA -> {
                    switch (ch) {
                        case ',' -> {  // add an empty field
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            //state = State.COMMA;
                            currentField++;
                            currentColumn++;
                        }
                        case '"' -> {  // Handle escaped quotes
                            state = State.QUOTED_FIELD;
                            currentField++;
                            currentColumn++;
                        }
                        case '\n' -> {  // end of line with empty field
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            state = State.START;
                            currentField++;
                            currentRow++;
                            currentLine++;
                            currentColumn = 0;
                            return row.toArray(new String[0]);
                        }
                        case '\r' -> {
                            state = State.CR;
                            currentLine++;
                            currentColumn = 0;
                        }
                        case -1 -> {
                            row.add(currentFieldBuilder.toString());
                            currentFieldBuilder.setLength(0);
                            state = State.EOF;
                            return row.toArray(new String[0]);
                        }
                        default -> {
                            currentFieldBuilder.append((char) ch);
                            state = State.FIELD;
                            currentField++;
                            currentColumn++;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Feel free to edit this method for your own testing purposes. As given, it
     * simply processes the CSV file supplied on the command line and prints each
     * resulting array of strings to standard out. Any reading or formatting errors
     * are printed to standard error.
     *
     * @param args command line arguments (1 expected)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("usage: util.CSVReader <filename.csv>");
            return;
        }

        /*
         * This block of code demonstrates basic usage of util.CSVReader's row-oriented API:
         * initialize the reader inside try-with-resources, initialize the util.CSVReader
         * using the reader, and repeatedly call readRow() until null is encountered. Since
         * util.CharacterReader implements AutoCloseable, the reader will be automatically
         * closed once the try block is exited.
         */
        var filename = args[0];
        try (var reader = new CharacterReader(filename)) {
            var csvReader = new CSVParser(reader);
            String[] row;
            while ((row = csvReader.readRow()) != null) {
                System.out.println(Arrays.toString(row));
            }
        } catch (IOException | CSVFormatException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
