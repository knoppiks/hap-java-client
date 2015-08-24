package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.google.common.base.Optional;

import java.net.URI;
import java.util.Map;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class Query {

    public static final Keyword TARGET = Executable.TARGET;
    public static final Keyword PARAMS = Executable.PARAMS;
    public static final Keyword LABEL = Executable.LABEL;

    private final Executable executable;

    public Query(URI target, Map<Keyword, Param> params, Optional<String> label) {
        this.executable = new Executable(target, params, label);
    }

    public URI getTarget() {
        return executable.getTarget();
    }

    public Map<Keyword, Param> getParams() {
        return executable.getParams();
    }

    public Optional<String> getLabel() {
        return executable.getLabel();
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
