package de.knoppiks.hap.client;

import com.cognitect.transit.Keyword;
import com.google.common.collect.ImmutableMap;
import de.knoppiks.hap.client.model.Form;
import de.knoppiks.hap.client.model.Query;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public final class RequestBuilders {

    private RequestBuilders() {
    }

    public static CreateRequestBuilder create(final Form form) {
        return new CreateRequestBuilder(form, ImmutableMap.<Keyword, Object>of());
    }

    public static QueryRequestBuilder query(final Query query) {
        return new QueryRequestBuilder(query, ImmutableMap.<Keyword, Object>of());
    }
}
