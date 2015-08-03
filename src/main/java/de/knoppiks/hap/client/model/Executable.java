package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.google.common.base.Optional;

import java.net.URI;
import java.util.Map;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.collect.ImmutableMap.copyOf;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
final class Executable {

    private final URI target;
    private final Map<Keyword, Param> params;
    private final Optional<String> title;

    public Executable(URI target, Map<Keyword, Param> params, Optional<String> title) {
        this.target = target;
        this.params = params;
        this.title = title;
    }

    public URI getTarget() {
        return target;
    }

    public Map<Keyword, Param> getParams() {
        return copyOf(params);
    }

    public Optional<String> getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Executable that = (Executable) o;

        if (!target.equals(that.target)) return false;
        if (!params.equals(that.params)) return false;
        return title.equals(that.title);

    }

    @Override
    public int hashCode() {
        int result = target.hashCode();
        result = 31 * result + params.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("target", target)
                .add("params", params)
                .add("title", title)
                .toString();
    }
}
