package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

import java.net.URI;

import static com.cognitect.transit.TransitFactory.keyword;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class Link {

    public static final Keyword HREF = keyword("href");
    public static final Keyword LABEL = keyword("label");

    private final URI href;
    private final Optional<String> label;

    public Link(URI href, Optional<String> label) {

        this.href = href;
        this.label = label;
    }

    public URI getHref() {
        return href;
    }

    public Optional<String> getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("href", href)
                .add("label", label)
                .toString();
    }
}
