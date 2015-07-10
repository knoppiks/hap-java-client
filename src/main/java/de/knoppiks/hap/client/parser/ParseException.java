package de.knoppiks.hap.client.parser;

/**
 * @author <a href="mailto:alexanderkiel@gmx.net">Alexander Kiel</a>
 */
public class ParseException extends Exception {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
