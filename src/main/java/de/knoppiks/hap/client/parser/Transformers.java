package de.knoppiks.hap.client.parser;


import com.cognitect.transit.Keyword;

import java.net.URI;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
final class Transformers {

    static final Transformer<Map> MAP_TRANSFORMER = typeTransformer(Map.class);
    static final Transformer<Keyword> KEYWORD_TRANSFORMER = typeTransformer(Keyword.class);

    private Transformers() {
    }

    public static <T> Transformer<T> typeTransformer(final Class<T> type) {
        checkNotNull(type);
        return new Transformer<T>() {
            @Override
            public T transform(Object obj) throws TransformException {
                if (type.isAssignableFrom(obj.getClass())) {
                    return type.cast(obj);
                } else {
                    throw new TransformException(format("Expected a %s but found a %s.",
                            type.getSimpleName(), obj.getClass().getSimpleName()));
                }
            }
        };
    }

    public static Map<?, ?> castToMap(Object obj) throws TransformException {
        return MAP_TRANSFORMER.transform(checkNotNull(obj));
    }

    public static Transformer<String> stringTransformer() {
        return new Transformer<String>() {
            @Override
            public String transform(Object obj) throws TransformException {
                return obj.toString();
            }
        };
    }

    public static Transformer<URI> URITransformer(final URI baseUri) {
        return new Transformer<URI>() {
            @Override
            public URI transform(Object obj) throws TransformException {
                checkNotNull(obj);
                return baseUri.resolve(obj.toString());
            }
        };
    }
}
