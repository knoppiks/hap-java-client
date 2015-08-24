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
    public static final Keyword LABEL = keyword("label");
    public static final Keyword TYPE = keyword("type");

    private final Optional<String> label;
    private final String type;
    private boolean optional;

    public Param(String type, Optional<String> label, boolean optional) {
        this.label = label;
        this.type = type;
        this.optional = optional;
    }

    public Optional<String> getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Param param = (Param) o;

        if (optional != param.optional) return false;
        if (!label.equals(param.label)) return false;
        return type.equals(param.type);

    }

    @Override
    public int hashCode() {
        int result = label.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (optional ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("label", label)
                .add("type", type)
                .toString();
    }
}
