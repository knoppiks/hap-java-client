package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.cognitect.transit.TransitFactory.Format;
import com.google.common.base.Optional;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.URI;
import java.util.Map;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.collect.ImmutableMap.copyOf;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public abstract class AbstractExecutable {

    private final URI target;
    private final Map<Keyword, Param> params;
    private final Optional<String> title;

    public AbstractExecutable(URI target, Map<Keyword, Param> params, Optional<String> title) {
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
    public String toString() {
        return toStringHelper(this)
                .add("target", target)
                .add("params", params)
                .add("title", title)
                .toString();
    }
}
