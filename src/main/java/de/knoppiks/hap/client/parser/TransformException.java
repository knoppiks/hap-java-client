package de.knoppiks.hap.client.parser;

/**
 * @author <a href="mailto:alexanderkiel@gmx.net">Alexander Kiel</a>
 */
public class TransformException extends Exception {

    private static final long serialVersionUID = 6609484176578938096L;

    public TransformException(String message) {
        super(message);
    }

    public TransformException(String message, Throwable cause) {
        super(message, cause);
    }
}
