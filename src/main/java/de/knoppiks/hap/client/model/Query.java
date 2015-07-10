package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.google.common.base.Optional;

import java.net.URI;
import java.util.Map;

import static com.cognitect.transit.TransitFactory.keyword;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class Query extends AbstractExecutable {

    public static final Keyword TARGET = keyword("href");
    public static final Keyword PARAMS = keyword("params");
    public static final Keyword TITLE = keyword("title");

    public Query(URI target, Map<Keyword, Param> params, Optional<String> title) {
        super(target, params, title);
    }
}
