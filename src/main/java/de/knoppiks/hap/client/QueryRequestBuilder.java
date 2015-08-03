package de.knoppiks.hap.client;

import com.cognitect.transit.Keyword;
import com.cognitect.transit.TransitFactory;
import com.google.common.collect.ImmutableMap;
import de.knoppiks.hap.client.model.Query;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.Map;

import static com.cognitect.transit.TransitFactory.writer;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class QueryRequestBuilder extends RequestBuilder<Query> {

    private final Query query;
    private final ImmutableMap<Keyword, Object> params;

    QueryRequestBuilder(Query query, ImmutableMap<Keyword, Object> params) {
        this.query = query;
        this.params = params;
    }

    @Override
    public QueryRequestBuilder put(Keyword param, Object value) {
        return new QueryRequestBuilder(query,
                ImmutableMap.<Keyword, Object>builder().putAll(params).put(param, value).build());
    }

    @Override
    HttpUriRequest build(TransitFactory.Format format) {
        URIBuilder uriBuilder = new URIBuilder(query.getTarget());
        for (Map.Entry<? extends Keyword, ?> field : params.entrySet()) {
            uriBuilder.addParameter(field.getKey().getName(),
                    writeParam(field.getValue().toString(), format));
        }
        try {
            return new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unexpected URISyntaxException while building the GEt request URI: "
                            + e.getMessage(), e);
        }
    }

    private String writeParam(Object params, TransitFactory.Format format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer(format, out).write(params);
        return out.toString();
    }
}
