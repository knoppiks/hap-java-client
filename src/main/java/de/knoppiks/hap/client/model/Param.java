package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

import static com.cognitect.transit.TransitFactory.keyword;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class Param {

    public static final Keyword OPTIONAL = keyword("optional");
    public static final Keyword DESCRIPTION = keyword("description");
    public static final Keyword TYPE = keyword("type");

    private final Optional<String> description;
    private final String type;
    private boolean optional;

    public Param(String type, Optional<String> description, boolean optional) {
        this.description = description;
        this.type = type;
        this.optional = optional;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("description", description)
                .add("type", type)
                .toString();
    }
}
