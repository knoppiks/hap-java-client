package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.google.common.base.Optional;

import java.net.URI;
import java.util.Map;

import static com.cognitect.transit.TransitFactory.keyword;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class Query {

    public static final Keyword TARGET = keyword("href");
    public static final Keyword PARAMS = keyword("params");
    public static final Keyword TITLE = keyword("title");

    private final Executable executable;

    public Query(URI target, Map<Keyword, Param> params, Optional<String> title) {
        this.executable = new Executable(target, params, title);
    }

    public URI getTarget() {
        return executable.getTarget();
    }

    public Map<Keyword, Param> getParams() {
        return executable.getParams();
    }

    public Optional<String> getTitle() {
        return executable.getTitle();
    }

    @Override
    public boolean equals(Object o) {
        return executable.equals(o);
    }

    @Override
    public int hashCode() {
        return executable.hashCode();
    }

    @Override
    public String toString() {
        return executable.toString();
    }
}
