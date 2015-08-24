package de.knoppiks.hap.client.parser;

import com.cognitect.transit.Keyword;
import com.google.common.base.Optional;
import de.knoppiks.hap.client.model.Param;
import de.knoppiks.hap.client.model.Query;

import java.net.URI;
import java.util.Map;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static de.knoppiks.hap.client.parser.MapTransformers.keywordMapOf;
import static de.knoppiks.hap.client.parser.Transformers.URITransformer;
import static de.knoppiks.hap.client.parser.Transformers.castToMap;
import static de.knoppiks.hap.client.parser.Transformers.stringTransformer;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class QueryTransformer extends Transformer<Query> {

    private final Transformer<URI> uriTransformer;

    public QueryTransformer(URI baseUri) {
        this.uriTransformer = URITransformer(checkNotNull(baseUri));
    }

    @Override
    public Query transform(Object obj) throws TransformException {
        Map map = castToMap(obj);

        if (!map.containsKey(Query.TARGET)) {
            throw new TransformException("Missing :href.");
        }
        if (!map.containsKey(Query.PARAMS)) {
            throw new TransformException("Missing :params.");
        }

        URI target = uriTransformer.transform(map.get(Query.TARGET));
        Map<Keyword, Param> params = keywordMapOf(new ParamTransformer()).transform(map.get(Query.PARAMS));
        Optional<String> title = fromNullable(map.get(Query.LABEL)).transform(stringTransformer());

        return new Query(target, params, title);
    }
}
