package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;

import static com.cognitect.transit.TransitFactory.keyword;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public final class Operation {

    public static final Operation DELETE = new Operation("delete");
    public static final Operation UPDATE = new Operation("update");

    private final Keyword keyword;

    private Operation(String keyword) {
        this.keyword = keyword(keyword);
    }

    public Keyword getKeyword() {
        return keyword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Operation operation = (Operation) o;

        return keyword.equals(operation.keyword);
    }

    @Override
    public int hashCode() {
        return keyword.hashCode();
    }

    @Override
    public String toString() {
        return keyword.toString();
    }
}
